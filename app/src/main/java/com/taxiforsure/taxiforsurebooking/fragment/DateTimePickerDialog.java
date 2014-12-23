package com.taxiforsure.taxiforsurebooking.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.taxiforsure.taxiforsurebooking.R;
import com.taxiforsure.taxiforsurebooking.application.TaxiApplication;

import java.util.Calendar;

/**
 * Created by adarshpandey on 11/29/14.
 */
public class DateTimePickerDialog extends DialogFragment {

    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button submit;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.date_time_picker, null);



        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getView() != null) {
            datePicker = (DatePicker) getView().findViewById(R.id.date_picker);
            timePicker = (TimePicker) getView().findViewById(R.id.time_picker);

            submit = (Button) getView().findViewById(R.id.submit);
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int dayOfMonth = datePicker.getDayOfMonth();
                    int month = datePicker.getMonth();
                    int year = datePicker.getYear();
                    int hourOfDay = timePicker.getCurrentHour();
                    int minutes = timePicker.getCurrentMinute();

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, dayOfMonth, hourOfDay, minutes);

                    TaxiApplication.getEventBus().post(new DateTimeEvent(calendar));
                    dismiss();
                }
            });

        }
    }

    public static class DateTimeEvent {
        public Calendar calendar;

        public DateTimeEvent(Calendar calendar) {
            this.calendar = calendar;
        }
    }
}
