package com.taxiforsure.taxiforsurebooking.service.network;

import com.taxiforsure.taxiforsurebooking.service.response.AddressList;

import java.util.Map;

import retrofit.http.GET;
import retrofit.http.QueryMap;

/**
 * Created by adarshpandey on 11/29/14.
 */
public interface GoogleService {

    @GET("/maps/api/place/autocomplete/json")
    public AddressList getSuggestions(@QueryMap Map<String, String > queryMap);
}
