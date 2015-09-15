package com.heinrichreimersoftware.wg_planer.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class Utils {

    public static boolean hasActiveInternetConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            Log.d("WG-Planer", "Network available!");
            return true;
        }
        Log.e("WG-Planer", "No network available!");
        return false;
    }

    public static InputStream stringToInputStream(String string) {
        return new ByteArrayInputStream(string.getBytes());
    }

    public static int dpToPx(int dp, Context context) {
        return Math.round(dp * (context.getResources().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int compareCase(char char1, char char2) {
        if ((Character.isUpperCase(char1) && Character.isUpperCase(char2)) ||
                (!Character.isUpperCase(char1) && !Character.isUpperCase(char2))) return 0;
        if (Character.isUpperCase(char1)) return -1;
        if (Character.isUpperCase(char2)) return 1;
        return 0;
    }

    public static String leadingNull(int number, int places) {
        String numberString = "" + number;
        while (numberString.length() < places) {
            numberString = "0" + numberString;
        }
        return (numberString);
    }
}