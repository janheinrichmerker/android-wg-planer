package com.heinrichreimersoftware.wg_planer.geo;

import android.app.Activity;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.util.Log;

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
        Log.d(MainActivity.TAG, "updateMonitoring()");
        if (getGoogleApiClient().isConnected()) {
            if (shouldMonitor) {
                LocationServices.GeofencingApi.addGeofences(
                        getGoogleApiClient(),
                        getGeoFencingRequest(),
                        getGeofencePendingIntent()
                ).setResultCallback(new AddGeofenceResultCallback());
            } else {
                LocationServices.GeofencingApi.removeGeofences(
                        getGoogleApiClient(),
                        getGeofencePendingIntent()
                ).setResultCallback(new RemoveGeofenceResultCallback());
            }
        }
    }

    public void startMonitoring() {
        Log.d(MainActivity.TAG, "startMonitoring()");
        shouldMonitor = true;
        updateMonitoring();
    }

    public void stopMonitoring() {
        Log.d(MainActivity.TAG, "stopMonitoring()");
        shouldMonitor = false;
        updateMonitoring();
    }

    private GeofencingRequest getGeoFencingRequest() {
        Log.d(MainActivity.TAG, "getGeoFencingRequest()");
        return new GeofencingRequest.Builder()
                .addGeofences(mGeofences)
                .setInitialTrigger(mInitialTrigger)
                .build();
    }

    private PendingIntent getGeofencePendingIntent() {
        Log.d(MainActivity.TAG, "getGeofencePendingIntent()");
        if (mGeofencePendingIntent == null) {
            Intent intent = new Intent(getContext(), mIntentService);
            mGeofencePendingIntent = PendingIntent.getService(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        return mGeofencePendingIntent;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(MainActivity.TAG, "onConnectionFailed(" + connectionResult + ")");
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
        Log.d(MainActivity.TAG, "onConnected(" + bundle + ")");
        super.onConnected(bundle);
        updateMonitoring();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(MainActivity.TAG, "onConnectionSuspended(" + i + ")");
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
                Log.d(MainActivity.TAG, "GeofenceHelper Adding geofence(s) succeeded");
            } else {
                Log.d(MainActivity.TAG, "GeofenceHelper Adding geofence(s) failed: " + status);
            }
        }
    }

    private class RemoveGeofenceResultCallback implements ResultCallback<Status> {
        @Override
        public void onResult(Status status) {
            if (status.isSuccess()) {
                Log.d(MainActivity.TAG, "GeofenceHelper Removing geofence(s) succeeded");
            } else {
                Log.d(MainActivity.TAG, "GeofenceHelper Removing geofence(s) failed: " + status);
            }
        }
    }
}
