package com.lmwdelivery.common;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;


import com.lmwdelivery.app.AppConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class CurrentLocation extends IntentService {
    private static final String IDENTIFIER = "GetAddressIntentService";
    private ResultReceiver addressResultReceiver;

    public CurrentLocation() {
        super(IDENTIFIER);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            String msg = "";
            addressResultReceiver = Objects.requireNonNull(intent).getParcelableExtra(AppConfig.RECEIVER);
            Location location = intent.getParcelableExtra(AppConfig.LOCATION_DATA_EXTRA);
            if (location == null) {
                msg = "No location, can't go further without location";
                sendResultsToReceiver(AppConfig.FAILURE_RESULT, msg);
                return;
            }
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            } catch (Exception ioException) {
                Log.e("", "Error in getting address for the location");
            }

            if (addresses == null || addresses.size() == 0) {
                msg = "No address found for the location";
                sendResultsToReceiver(AppConfig.FAILURE_RESULT, msg);
            } else {
                Address address = addresses.get(0);
                ArrayList<String> addressFragments = new ArrayList<>();
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressFragments.add(address.getAddressLine(i));
                }

                String addressLine =  address.getAddressLine(0);
                //String addressLine2 =  address.getAddressLine(1);
                String postCode =  address.getPostalCode() ;
                String subdomain =  address.getSubLocality() ;
                String city = address.getLocality();  /////get city
                String state = address.getAdminArea();  ///get state

                sendResultsToReceiver(AppConfig.SUCCESS_RESULT, TextUtils.join(Objects.requireNonNull(System.getProperty("line.separator")), addressFragments));
               // sendResultsToReceiver(Constants.POSTCODE_SUCCESS_RESULT, postCode);
               // sendResultsToReceiver(Constants.STATE_SUCCESS_RESULT, state);
               // sendResultsToReceiver(Constants.CITY_SUCCESS_RESULT, city);
               sendResultsToReceiver(AppConfig.ADDRESS_LINE_SUCCESS_RESULT, addressLine);
               sendResultsToReceiver(AppConfig.SUB_DOMAIN, subdomain);
               sendResultsToReceiver(AppConfig.PIN_CODE, postCode);
            }
        }
        if (addressResultReceiver == null) {
            Log.e("GetAddressIntentService", "No receiver, not processing the request further");
        }

    }

    private void sendResultsToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(AppConfig.RESULT_DATA_KEY, message);
        addressResultReceiver.send(resultCode, bundle);
    }
}