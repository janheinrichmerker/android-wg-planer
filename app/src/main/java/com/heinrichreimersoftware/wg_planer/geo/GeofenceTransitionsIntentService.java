package com.heinrichreimersoftware.wg_planer.geo;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.heinrichreimersoftware.wg_planer.MainActivity;

public class GeofenceTransitionsIntentService extends IntentService {

    public static final int GEOFENCE_NOTIFICATION_ID = 3;

    public GeofenceTransitionsIntentService(String name) {
        super(name);
    }

    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            Log.e(MainActivity.TAG, "Geofence error: " + geofencingEvent.getErrorCode());
            return;
        }

        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            Location location = geofencingEvent.getTriggeringLocation();
            String log = "Geofence action: {transitionType=" + geofenceTransition + "; location={lat=" + location.getLatitude() +
                    "; long=" + location.getLongitude() + "; alt=" + location.getAltitude() +
                    "; accuracy=" + location.getAccuracy() + "}; geofences=[" + geofencingEvent.getTriggeringGeofences().toString() + "]}";

            Log.e(MainActivity.TAG, log);
            Toast.makeText(getApplicationContext(), log, Toast.LENGTH_SHORT).show();
        }
    }
}
