package com.taxiforsure.taxiforsurebooking.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.taxiforsure.taxiforsurebooking.R;
import com.taxiforsure.taxiforsurebooking.components.TravellerInfo;

import java.text.SimpleDateFormat;

/**
 * Created by adarshpandey on 11/29/14.
 */
public class TravellerDetailsActivity extends Activity {

    public static final String TRAVELLER_INFO = "travellerInfo";

    private TravellerInfo info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.traverller_details_screen);

        Intent intent = getIntent();
        info = (TravellerInfo) intent.getSerializableExtra(TRAVELLER_INFO);

        if (info != null) {
            TextView name = (TextView) findViewById(R.id.traveller);
            TextView contact = (TextView) findViewById(R.id.contact);
            TextView pick = (TextView) findViewById(R.id.pick_address);
            TextView carType = (TextView) findViewById(R.id.car);
            TextView time = (TextView) findViewById(R.id.time);
            TextView drop = (TextView) findViewById(R.id.drop);
            SimpleDateFormat ft =
                    new SimpleDateFormat ("E yyyy/MM/dd 'at' hh:mm:ss a zzz");
            name.setText(info.name);
            contact.setText(info.contactNumber);
            pick.setText(info.pickAddress);
            time.setText(ft.format(info.time.getTime()));
            drop.setText(info.dropAddress);
            carType.setText(info.carType);

            Button ride = (Button) findViewById(R.id.ride);
            Button cancel = (Button) findViewById(R.id.cancel);

            ride.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // save info
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }
}
