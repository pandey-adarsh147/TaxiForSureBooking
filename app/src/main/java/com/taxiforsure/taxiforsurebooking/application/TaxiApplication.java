package com.taxiforsure.taxiforsurebooking.application;

import android.app.Application;

import com.squareup.otto.Bus;

/**
 * Created by adarshpandey on 11/28/14.
 */
public class TaxiApplication extends Application {

    private static TaxiApplication mApplication;
    private Bus mBus = new Bus();

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
    }

    public static Bus getEventBus() {
        return mApplication.mBus;
    }
}
