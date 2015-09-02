package com.heinrichreimersoftware.wg_planer.geo;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.heinrichreimersoftware.wg_planer.MainActivity;
import com.heinrichreimersoftware.wg_planer.play.PlayServicesHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GeofenceHelper extends PlayServicesHelper implements ResultCallback<Status> {

    private PendingIntent mGeofencePendingIntent;
    private List<Geofence> mGeofences;

    private boolean shouldMonitor = false;

    public GeofenceHelper(Context context, List<Geofence> geofences) {
        super(context);
        mGeofences = geofences;

    }

    public GeofenceHelper(Context context, Geofence... geofences) {
        this(context, new ArrayList<>(Arrays.asList(geofences)));
    }

    private void updateMonitoring() {
        Log.d(MainActivity.TAG, "GeofenceHelper updating monitoring");
        Toast.makeText(getContext(), "Updating monitoring", Toast.LENGTH_SHORT).show();
        if (getGoogleApiClient().isConnected()) {
            if (shouldMonitor) {
                LocationServices.GeofencingApi.addGeofences(
                        getGoogleApiClient(),
                        getGeofencingRequest(),
                        getGeofencePendingIntent()
                ).setResultCallback(this);
                Log.d(MainActivity.TAG, "GeofenceHelper starting monitoring");
                Toast.makeText(getContext(), "Starting monitoring", Toast.LENGTH_SHORT).show();
            } else {
                LocationServices.GeofencingApi.removeGeofences(
                        getGoogleApiClient(),
                        getGeofencePendingIntent()
                ).setResultCallback(this);
                Log.d(MainActivity.TAG, "GeofenceHelper stopping monitoring");
                Toast.makeText(getContext(), "Stopping monitoring", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void startMonitoring() {
        Log.d(MainActivity.TAG, "GeofenceHelper trying to start monitoring");
        Toast.makeText(getContext(), "Trying to start monitoring", Toast.LENGTH_SHORT).show();

        shouldMonitor = true;
        updateMonitoring();
    }

    public void stopMonitoring() {
        Log.d(MainActivity.TAG, "GeofenceHelper trying to stop monitoring");
        Toast.makeText(getContext(), "Trying to stop monitoring", Toast.LENGTH_SHORT).show();

        shouldMonitor = false;
        updateMonitoring();
    }

    private PendingIntent getGeofencePendingIntent() {
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(getContext(), GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        return PendingIntent.getService(getContext(), 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofences);
        return builder.build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        super.onConnected(bundle);
        Log.d(MainActivity.TAG, "GeofenceHelper connected");
        updateMonitoring();
    }

    @Override
    public void onResult(Status status) {
        Log.d(MainActivity.TAG, "GeofenceHelper status: " + status);
    }
}
