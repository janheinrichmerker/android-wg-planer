package com.heinrichreimersoftware.wg_planer.play;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.Random;

public abstract class PlayServicesHelper implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private Context mContext;
    private GoogleApiClient mGoogleApiClient;
    private int connectionFailureResolutionRequestCode = -1;

    public PlayServicesHelper(Context context) {
        mContext = context;
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }

    protected int getConnectionFailureResolutionRequestCode() {
        if (connectionFailureResolutionRequestCode < 0) {
            Random generator = new Random();
            connectionFailureResolutionRequestCode = generator.nextInt(Integer.MAX_VALUE);
        }
        return connectionFailureResolutionRequestCode;
    }

    protected int getConnectionTimeOut() {
        return 1000;
    }

    protected Context getContext() {
        return mContext;
    }

    protected GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    protected boolean hasGoogleApiClient() {
        return mGoogleApiClient != null;
    }

    public void connect() {
        mGoogleApiClient.connect();
    }

    public void disconnect() {
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
