package com.taxiforsure.taxiforsurebooking.service;

import com.taxiforsure.taxiforsurebooking.service.network.GoogleService;
import com.taxiforsure.taxiforsurebooking.service.response.AddressList;

import java.util.Map;

import retrofit.RestAdapter;

/**
 * Created by adarshpandey on 11/24/14.
 */
public class ApiClient {
    public static final String URL = "https://maps.googleapis.com";


    public static AddressList loadSearchData(Map<String, String> queryMap) {

        GoogleService service = SnapSearchRestAdapter.createRestAdapter().create(GoogleService.class);


        return service.getSuggestions(queryMap);
    }



    public static class SnapSearchRestAdapter {
        private static RestAdapter restAdapter;

        private static synchronized RestAdapter createRestAdapter() {
            if (restAdapter == null) {
                /*Gson gson = new GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                        .registerTypeAdapter(Boolean.class, new DateTypeAdapter())
                        .create();*/

                restAdapter = new RestAdapter.Builder()
                        .setLogLevel(RestAdapter.LogLevel.FULL)
                        .setEndpoint(URL).build();


            }

            return restAdapter;
        }
    }

}
