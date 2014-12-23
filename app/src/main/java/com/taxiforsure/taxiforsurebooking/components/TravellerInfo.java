package com.taxiforsure.taxiforsurebooking.components;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by adarshpandey on 11/29/14.
 */
public class TravellerInfo implements Serializable {
    public String name;
    public String pickAddress;
    public String dropAddress;
    public String contactNumber;
    public Calendar time;
    public String carType;
}
