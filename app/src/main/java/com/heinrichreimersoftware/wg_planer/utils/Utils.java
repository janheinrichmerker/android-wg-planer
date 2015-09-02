package com.heinrichreimersoftware.wg_planer.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

public class Utils {

    public static final int NETWORK_TIMEOUT = 10 * 1000;

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

    public static boolean hasWifiConnection(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (wifi != null && wifi.isConnected()) {
            return true;
        }
        return false;
    }

    public static String inputStreamToString(InputStream is) {
        String line;
        StringBuilder total = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        try {
            while ((line = rd.readLine()) != null) {
                total.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return total.toString();
    }

    public static InputStream stringToInputStream(String string) {
        InputStream is = new ByteArrayInputStream(string.getBytes());
        return is;
    }

    public static int dpToPx(int dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public static int pxToDp(int px, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    public static boolean isEven(int number) {
        return number % 2 == 0;
    }

    public static String dayToString(int day) {
        switch (day) {
            case Calendar.MONDAY:
                return "Montag";
            case Calendar.TUESDAY:
                return "Dienstag";
            case Calendar.WEDNESDAY:
                return "Mitwoch";
            case Calendar.THURSDAY:
                return "Donnerstag";
            case Calendar.FRIDAY:
                return "Freitag";
            case Calendar.SATURDAY:
                return "Samstag";
            case Calendar.SUNDAY:
                return "Sonntag";
            default:
                return "";
        }
    }

    public static int monthNumberToMonth(int monthNumber) {
        switch (monthNumber) {
            case 1:
                return Calendar.JANUARY;
            case 2:
                return Calendar.FEBRUARY;
            case 3:
                return Calendar.MARCH;
            case 4:
                return Calendar.APRIL;
            case 5:
                return Calendar.MAY;
            case 6:
                return Calendar.JUNE;
            case 7:
                return Calendar.JULY;
            case 8:
                return Calendar.AUGUST;
            case 9:
                return Calendar.SEPTEMBER;
            case 10:
                return Calendar.OCTOBER;
            case 11:
                return Calendar.NOVEMBER;
            case 12:
                return Calendar.DECEMBER;
            default:
                return -1;
        }
    }

    public static int compareCase(char char1, char char2) {
        if ((Character.isUpperCase(char1) && Character.isUpperCase(char2)) ||
                (!Character.isUpperCase(char1) && !Character.isUpperCase(char2))) return 0;
        if (Character.isUpperCase(char1)) return -1;
        if (Character.isUpperCase(char2)) return 1;
        return 0;
    }

    public static String leadingNull(int number) {
        String numberString = "" + number;
        while (numberString.length() < 5) {
            numberString = "0" + numberString;
        }
        return (numberString);
    }
}