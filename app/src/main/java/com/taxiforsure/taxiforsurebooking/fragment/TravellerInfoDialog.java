package com.taxiforsure.taxiforsurebooking.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.taxiforsure.taxiforsurebooking.R;
import com.taxiforsure.taxiforsurebooking.application.TaxiApplication;

/**
 * Created by adarshpandey on 11/29/14.
 */
public class TravellerInfoDialog extends DialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.traveller_info_screen, null);



        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getView() != null) {
            Button button = (Button) getView().findViewById(R.id.submit);
            final EditText name = (EditText) getView().findViewById(R.id.name);
            final EditText mobile = (EditText) getView().findViewById(R.id.mobile_number);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!"".equals(name.getText().toString()) && !"".equals(mobile.getText().toString())) {
                        TaxiApplication.getEventBus().post(new TravellerInfoEvent(name.getText().toString(), mobile.getText().toString()));
                        dismiss();
                    } else {
                        Toast.makeText(getActivity(), "Please enter informations", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public static class TravellerInfoEvent {
        public String name;
        public String contactNumber;

        public TravellerInfoEvent(String name, String number) {
            this.name = name;
            this.contactNumber = number;
        }
    }
}
