package com.taxiforsure.taxiforsurebooking.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.otto.Subscribe;
import com.taxiforsure.taxiforsurebooking.R;
import com.taxiforsure.taxiforsurebooking.application.TaxiApplication;
import com.taxiforsure.taxiforsurebooking.components.TravellerInfo;
import com.taxiforsure.taxiforsurebooking.fragment.DateTimePickerDialog;
import com.taxiforsure.taxiforsurebooking.fragment.LocationSearchDialog;
import com.taxiforsure.taxiforsurebooking.fragment.TravellerInfoDialog;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class HomeActivity extends Activity {
    private static final String TAG = HomeActivity.class.getName();
    private GoogleMap mMap;

    private Button mSourceBtn;
    private Button mDestinationBtn;
    private Button mNowBtn;
    private Button mLaterBtn;
    private RadioGroup mRadioGroup;

    private TextView mSourceLocation;
    private TextView mDestinationLocation;

    private Calendar pickTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mSourceBtn = (Button) findViewById(R.id.pickup_button);
        mDestinationBtn = (Button) findViewById(R.id.drop_button);
        mNowBtn = (Button) findViewById(R.id.now_btn);
        mLaterBtn = (Button) findViewById(R.id.later_btn);
        mRadioGroup = (RadioGroup) findViewById(R.id.mode_of_commute_group);

        mSourceLocation = (TextView) findViewById(R.id.source_location);
        mDestinationLocation = (TextView) findViewById(R.id.destination_location);

        mSourceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(LocationSearchDialog.AddressType.PICK);
            }
        });

        mDestinationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(LocationSearchDialog.AddressType.DROP);
            }
        });

        mNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAddressFilled()) {
                    pickTime = Calendar.getInstance();

                    showTravellerInfoDialog();
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter address", Toast.LENGTH_LONG).show();
                }
            }
        });

        mLaterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAddressFilled()) {
                    DateTimePickerDialog dateTimePickerDialog = new DateTimePickerDialog();
                    dateTimePickerDialog.show(getFragmentManager(), "dateTimePicker");
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter address", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private boolean isAddressFilled() {
        boolean isAddressFilled = false;
        if (!"".equals(mSourceLocation.getText().toString()) && !"".equals(mDestinationLocation.getText().toString())) {
            isAddressFilled = true;
        }

        return isAddressFilled;
    }

    private void showDialog(LocationSearchDialog.AddressType addressType) {
        LocationSearchDialog locationSearchDialog = new LocationSearchDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable(LocationSearchDialog.ADDRESS_TYPE, addressType);
        locationSearchDialog.setArguments(bundle);

        locationSearchDialog.show(getFragmentManager(), "locationSearchDialog");
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        TaxiApplication.getEventBus().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        TaxiApplication.getEventBus().unregister(this);
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                // The Map is verified. It is now safe to manipulate the map.
                mMap.setMyLocationEnabled(true);
                mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                    @Override
                    public void onMyLocationChange(Location location) {
                        if (location != null) {
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, mMap.getMaxZoomLevel() - 2));
                            mMap.setOnMyLocationChangeListener(null);
                            GetAddressTask getAddressTask = new GetAddressTask(HomeActivity.this);
                            getAddressTask.execute(location);
                        }
                    }
                });

            }
        }
    }

    private void showTravellerInfoDialog() {
        TravellerInfoDialog travellerInfoDialog = new TravellerInfoDialog();
        travellerInfoDialog.show(getFragmentManager(), "travellerInfoDialog");
    }

    @Subscribe
    public void onAddressSearched(LocationSearchDialog.AddressSubmitEvent addressSubmitEvent) {
        Toast.makeText(this, "Address submitted", Toast.LENGTH_LONG).show();

        if (addressSubmitEvent.addressType == LocationSearchDialog.AddressType.DROP) {
            mDestinationLocation.setText(addressSubmitEvent.address);
        } else {
            mSourceLocation.setText(addressSubmitEvent.address);
        }

    }

    @Subscribe
    public void onDateTimePicked(DateTimePickerDialog.DateTimeEvent dateTimeEvent) {
        pickTime = dateTimeEvent.calendar;

        showTravellerInfoDialog();
    }

    @Subscribe
    public void onTravellerInfoSubmitted(TravellerInfoDialog.TravellerInfoEvent travellerInfoEvent) {
        TravellerInfo travellerInfo = new TravellerInfo();
        travellerInfo.contactNumber = travellerInfoEvent.contactNumber;
        travellerInfo.name = travellerInfoEvent.name;
        travellerInfo.time = pickTime;
        travellerInfo.dropAddress = mDestinationLocation.getText().toString();
        travellerInfo.pickAddress = mSourceLocation.getText().toString();
        travellerInfo.carType = (String)((RadioButton) findViewById(mRadioGroup.getCheckedRadioButtonId())).getTag();



        Intent intent = new Intent(this, TravellerDetailsActivity.class);
        intent.putExtra(TravellerDetailsActivity.TRAVELLER_INFO, travellerInfo);

        startActivity(intent);
    }


    private class GetAddressTask extends
            AsyncTask<Location, Void, String> {
        Context mContext;

        public GetAddressTask(Context context) {
            super();
            mContext = context;
        }

        /**
         * Get a Geocoder instance, get the latitude and longitude
         * look up the address, and return it
         *
         * @return A string containing the address of the current
         * location, or an empty string if no address can be found,
         * or an error message
         * @params params One or more Location objects
         */
        @Override
        protected String doInBackground(Location... params) {
            Geocoder geocoder =
                    new Geocoder(mContext, Locale.getDefault());
            // Get the current location from the input parameter list
            Location loc = params[0];
            // Create a list to contain the result address
            List<Address> addresses = null;
            try {
                /*
                 * Return 1 address.
                 */
                addresses = geocoder.getFromLocation(loc.getLatitude(),
                        loc.getLongitude(), 1);
            } catch (IOException e1) {
                Log.e(TAG,
                        "IO Exception in getFromLocation()");
                e1.printStackTrace();
                return ("IO Exception trying to get address");
            } catch (IllegalArgumentException e2) {
                // Error message to post in the log
                String errorString = "Illegal arguments " +
                        Double.toString(loc.getLatitude()) +
                        " , " +
                        Double.toString(loc.getLongitude()) +
                        " passed to address service";
                Log.e("LocationSampleActivity", errorString);
                e2.printStackTrace();
                return errorString;
            }
            // If the reverse geocode returned an address
            if (addresses != null && addresses.size() > 0) {
                // Get the first address
                Address address = addresses.get(0);
                /*
                 * Format the first line of address (if available),
                 * city, and country name.
                 */
                String addressText = String.format(
                        "%s, %s, %s",
                        // If there's a street address, add it
                        address.getMaxAddressLineIndex() > 0 ?
                                address.getAddressLine(0) : "",
                        // Locality is usually a city
                        address.getLocality(),
                        // The country of the address
                        address.getCountryName());
                // Return the text
                return addressText;
            } else {
                return "No address found";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            mSourceLocation.setText(s);
        }
    }
}