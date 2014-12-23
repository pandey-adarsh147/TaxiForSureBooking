package com.taxiforsure.taxiforsurebooking.fragment;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.TextView;

import com.taxiforsure.taxiforsurebooking.R;
import com.taxiforsure.taxiforsurebooking.application.TaxiApplication;
import com.taxiforsure.taxiforsurebooking.service.ApiClient;
import com.taxiforsure.taxiforsurebooking.service.response.AddressList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by adarshpandey on 11/28/14.
 */
public class LocationSearchDialog extends DialogFragment {
    public static final String ADDRESS_TYPE = "addressType";
    public enum AddressType {
        PICK, DROP
    }

    private AutoCompleteTextView autoCompleteTextView;
    private Button mSubmit;


    private AddressType addressType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.location_search_screen, null);



        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getView() != null) {
            Bundle bundle = getArguments();
            addressType = (AddressType) bundle.getSerializable(ADDRESS_TYPE);
            mSubmit = (Button) getView().findViewById(R.id.submit);
            autoCompleteTextView = (AutoCompleteTextView) getView().findViewById(R.id.location_search);

            autoCompleteTextView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            suggestionAdapter = new SuggestionAdapter(getActivity(), R.layout.search_item);
            autoCompleteTextView.setAdapter(suggestionAdapter);
            mSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TaxiApplication.getEventBus().post(new AddressSubmitEvent(addressType, autoCompleteTextView.getText().toString()));
                    dismiss();
                }
            });
        }
    }

    private SuggestionAdapter suggestionAdapter;

//    private ArrayAdapter<AddressList.AddressInfo> searchAdapter
//            = new ArrayAdapter<AddressList.AddressInfo>(getActivity(), R.layout.search_item)

    public class SuggestionAdapter extends ArrayAdapter<AddressList.AddressInfo> {
        private AddressFilter filter;

        public SuggestionAdapter(Context context, int resource) {
            super(context, resource);
        }

        @Override
        public AddressFilter getFilter() {
            if (filter == null) {
                filter = new AddressFilter();
            }
            return filter;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.search_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.venueAddress = (TextView) convertView
                        .findViewById(R.id.search_item_venue_address);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            AddressList.AddressInfo addressInfo = this.getItem(position);

            viewHolder.venueAddress.setText(addressInfo.address);

            return convertView;
        }
    };

    private static class ViewHolder {
        TextView venueAddress;
    }

    public static class AddressSubmitEvent {
        public AddressType addressType;
        public String address;

        public AddressSubmitEvent(AddressType addressType, String address) {
            this.address = address;
            this.addressType = addressType;
        }
    }

    private class AddressFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();

            Map<String, String> queryMap = new HashMap<String, String>();
            queryMap.put("key", getString(R.string.suggestion_key));
            queryMap.put("input", constraint.toString());

            AddressList addressList = ApiClient.loadSearchData(queryMap);
            if (addressList != null && addressList.predictions != null) {
                filterResults.count = addressList.predictions.size();
                filterResults.values = addressList.predictions;
            }


            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (suggestionAdapter != null) {
                suggestionAdapter.clear();

                if (results.count > 0) {
                    for (AddressList.AddressInfo addressInfo : (List<AddressList.AddressInfo>)results.values) {
                        suggestionAdapter.add(addressInfo);
                    }
                }
            }
        }
    }
}
