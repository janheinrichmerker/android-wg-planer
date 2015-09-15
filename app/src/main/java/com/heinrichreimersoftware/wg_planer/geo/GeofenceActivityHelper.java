package com.heinrichreimersoftware.wg_planer.geo;

import android.app.Activity;
import android.app.IntentService;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.heinrichreimersoftware.wg_planer.Constants;
import com.heinrichreimersoftware.wg_planer.MainActivity;
import com.heinrichreimersoftware.wg_planer.R;

import java.util.Arrays;
import java.util.List;

public class GeofenceActivityHelper extends GeofenceHelper {

    Activity activity;

    public GeofenceActivityHelper(Activity activity, List<Geofence> geofences, Class<? extends IntentService> intentService, @InitialTrigger int initialTrigger) {
        super(activity, geofences, intentService, initialTrigger);
        this.activity = activity;
        update();
    }

    public GeofenceActivityHelper(Activity activity, List<Geofence> geofences, Class<? extends IntentService> intentService) {
        super(activity, geofences, intentService);
        this.activity = activity;
        update();
    }


    public void update() {
        Log.d(MainActivity.TAG, "update()");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        boolean geoNotificationsEnabled = sharedPreferences.getBoolean(activity.getString(R.string.key_preference_geo_notifications), false);
        boolean geoSilentEnabled = sharedPreferences.getBoolean(activity.getString(R.string.key_preference_geo_silent), false);

        if (geoNotificationsEnabled || geoSilentEnabled) {
            connect();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (activity.checkSelfPermission(Constants.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    startMonitoring();
                } else if (activity.shouldShowRequestPermissionRationale(Constants.ACCESS_FINE_LOCATION)) {
                    //TODO dialog instead of toast
                    Toast.makeText(activity, "\"Wilhelm-Gymnasium\" braucht diese Berechtigung, um Dir standortbasierte Benachrichtigungen zu bieten.", Toast.LENGTH_LONG).show(); //TODO string resource
                } else {
                    activity.requestPermissions(new String[]{Constants.ACCESS_FINE_LOCATION}, Constants.WG_GEOFENCE_REQUEST_CODE);
                }
            } else {
                startMonitoring();
            }
        } else {
            stopMonitoring();
            disconnect();
        }
    }

    public void checkPermission(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(MainActivity.TAG, "onConnectionFailed(" + requestCode + ", " + Arrays.toString(permissions) + ", " + Arrays.toString(grantResults) + ")");
        if (requestCode == Constants.WG_GEOFENCE_REQUEST_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Constants.ACCESS_FINE_LOCATION) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    update();
                }
            }
        }
    }
}
