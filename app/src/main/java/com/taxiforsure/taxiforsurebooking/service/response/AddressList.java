package com.taxiforsure.taxiforsurebooking.service.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by adarshpandey on 11/29/14.
 */
public class AddressList {

    public List<AddressInfo> predictions;


    public static class AddressInfo {
        public String name;
        @SerializedName("description")
        public String address;

        @Override
        public String toString() {
            return address;
        }
    }
}
