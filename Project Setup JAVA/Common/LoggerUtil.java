package com.lmwdelivery.common;

import android.util.Log;


import com.google.gson.Gson;
import com.lmwdelivery.BuildConfig;


/**
 * Created by Abhishek on 25/5/18.
 */

public class LoggerUtil {
    private static final String TAG = "OUTPUT";

    public static void logItem(Object src) {
        Gson gson = new Gson();
        if (BuildConfig.DEBUG)
            Log.e(TAG, "====:> " + gson.toJson(src));
    }
}
