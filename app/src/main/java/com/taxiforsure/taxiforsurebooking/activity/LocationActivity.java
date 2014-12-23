package com.taxiforsure.taxiforsurebooking.activity;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.taxiforsure.taxiforsurebooking.BuildConfig;
import com.taxiforsure.taxiforsurebooking.application.TaxiApplication;

/**
 * Created by adarshpandey on 11/28/14.
 *
 */

///////////// Not in use yet
public class LocationActivity extends Activity implements GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {
    private static final String TAG = LocationActivity.class.getName();

    private LocationClient mLocationClient;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mLocationClient == null) {
            mLocationClient = new LocationClient(this, this, this);
            mLocationRequest = LocationRequest.create();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(5000);
            mLocationRequest.setFastestInterval(100);
            mLocationRequest.setSmallestDisplacement(5.0f);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mLocationClient != null) {
            mLocationClient.connect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mLocationClient != null) {
            mLocationClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationClient.requestLocationUpdates(mLocationRequest, this);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Location listener attached");
        }

        Location location = mLocationClient.getLastLocation();

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "First attempt to get location" + location);
        }


    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onLocationChanged(Location location) {

        TaxiApplication.getEventBus().post(new LocationEvent(location));

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static class LocationEvent {
        public Location location;

        public LocationEvent(Location location) {
            this.location = location;
        }
    }
}
