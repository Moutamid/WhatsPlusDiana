package com.moutamid.rurovision.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.moutamid.rurovision.BuildConfig;


public class Logger {

    public static void AppLog(String key, String data) {
        if (BuildConfig.DEBUG) {
            Log.e(key, data);
        }
    }

    public static void Toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
