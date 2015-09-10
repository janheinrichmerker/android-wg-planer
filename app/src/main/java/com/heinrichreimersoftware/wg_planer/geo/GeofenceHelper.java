package com.heinrichreimersoftware.wg_planer.geo;

import android.app.Activity;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.heinrichreimersoftware.wg_planer.MainActivity;
import com.heinrichreimersoftware.wg_planer.play.PlayServicesHelper;

import java.util.ArrayList;
import java.util.List;

public class GeofenceHelper extends PlayServicesHelper {

    public static final int INITIAL_TRIGGER_DWELL = GeofencingRequest.INITIAL_TRIGGER_DWELL;
    public static final int INITIAL_TRIGGER_ENTER = GeofencingRequest.INITIAL_TRIGGER_ENTER;
    public static final int INITIAL_TRIGGER_EXIT = GeofencingRequest.INITIAL_TRIGGER_EXIT;
    public static final int INITIAL_TRIGGER_NONE = 0;
    private final List<Geofence> mGeofences;
    private Class<? extends IntentService> mIntentService = null;
    private PendingIntent mGeofencePendingIntent = null;
    @InitialTrigger
    private int mInitialTrigger = INITIAL_TRIGGER_NONE;
    private boolean shouldMonitor = false;

    public GeofenceHelper(Context context, List<Geofence> geofences, Class<? extends IntentService> intentService, @InitialTrigger int initialTrigger) {
        super(context);
        mGeofences = new ArrayList<>(geofences);
        mIntentService = intentService;
        mInitialTrigger = initialTrigger;
    }

    public GeofenceHelper(Context context, List<Geofence> geofences, Class<? extends IntentService> intentService) {
        this(context, geofences, intentService, INITIAL_TRIGGER_NONE);
    }

    private void updateMonitoring() {
        Log.d(MainActivity.TAG, "GeofenceHelper updating monitoring");
        Toast.makeText(getContext(), "Updating monitoring", Toast.LENGTH_SHORT).show();
        if (getGoogleApiClient().isConnected()) {
            if (shouldMonitor) {
                LocationServices.GeofencingApi.addGeofences(
                        getGoogleApiClient(),
                        getGeoFencingRequest(),
                        getGeofencePendingIntent()
                ).setResultCallback(new AddGeofenceResultCallback());

                Location location = LocationServices.FusedLocationApi.getLastLocation(getGoogleApiClient());
                Log.d(MainActivity.TAG, "GeofenceHelper starting monitoring at {lat=" + location.getLatitude() +
                        "; long=" + location.getLongitude() + "; alt=" + location.getAltitude() +
                        "; accuracy=" + location.getAccuracy() + "}");
                for (Geofence geofence : getGeoFencingRequest().getGeofences()) {
                    Log.d(MainActivity.TAG, "GeofenceHelper monitoring geofence: " + geofence.getRequestId());
                }
                Toast.makeText(getContext(), "Starting monitoring", Toast.LENGTH_SHORT).show();
            } else {
                LocationServices.GeofencingApi.removeGeofences(
                        getGoogleApiClient(),
                        getGeofencePendingIntent()
                ).setResultCallback(new RemoveGeofenceResultCallback());
                Log.d(MainActivity.TAG, "GeofenceHelper stopping monitoring");
                Toast.makeText(getContext(), "Stopping monitoring", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void startMonitoring() {
        Log.d(MainActivity.TAG, "GeofenceHelper trying to start monitoring");

        shouldMonitor = true;
        updateMonitoring();
    }

    public void stopMonitoring() {
        Log.d(MainActivity.TAG, "GeofenceHelper trying to stop monitoring");

        shouldMonitor = false;
        updateMonitoring();
    }

    private GeofencingRequest getGeoFencingRequest() {
        return new GeofencingRequest.Builder()
                .addGeofences(mGeofences)
                .setInitialTrigger(mInitialTrigger)
                .build();
    }

    private PendingIntent getGeofencePendingIntent() {
        Log.d(MainActivity.TAG, "GeofenceHelper get geofence pending intent");
        if (mGeofencePendingIntent == null) {
            Log.d(MainActivity.TAG, "GeofenceHelper create geofence pending intent");
            Intent intent = new Intent(getContext(), mIntentService);
            mGeofencePendingIntent = PendingIntent.getService(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        Log.d(MainActivity.TAG, "GeofenceHelper geofence pending intent: " + mGeofencePendingIntent.toString());
        return mGeofencePendingIntent;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult((Activity) getContext(),
                        getConnectionFailureResolutionRequestCode());
            } catch (IntentSender.SendIntentException e) {
                Log.e(MainActivity.TAG, "Exception while resolving connection error.", e);
            }
        } else {
            int errorCode = connectionResult.getErrorCode();
            Log.e(MainActivity.TAG, "Connection to Google Play services failed with error code " + errorCode);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        super.onConnected(bundle);
        Log.d(MainActivity.TAG, "GeofenceHelper connected");
        updateMonitoring();
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (null != mGeofencePendingIntent) {
            updateMonitoring();
        }
    }

    @IntDef(
            flag = true,
            value = {INITIAL_TRIGGER_DWELL, INITIAL_TRIGGER_ENTER, INITIAL_TRIGGER_EXIT, INITIAL_TRIGGER_NONE})
    public @interface InitialTrigger {
    }

    private class AddGeofenceResultCallback implements ResultCallback<Status> {
        @Override
        public void onResult(Status status) {
            if (status.isSuccess()) {
                Log.d(MainActivity.TAG, "GeofenceHelper Adding geofence succeeded");
            } else {
                Log.d(MainActivity.TAG, "GeofenceHelper Adding geofence failed: " + status);
            }
        }
    }

    private class RemoveGeofenceResultCallback implements ResultCallback<Status> {
        @Override
        public void onResult(Status status) {
            if (status.isSuccess()) {
                Log.d(MainActivity.TAG, "GeofenceHelper Removing geofence succeeded");
            } else {
                Log.d(MainActivity.TAG, "GeofenceHelper Removing geofence failed: " + status);
            }
        }
    }
}
