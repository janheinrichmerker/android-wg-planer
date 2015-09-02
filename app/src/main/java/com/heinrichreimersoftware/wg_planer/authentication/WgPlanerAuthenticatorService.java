package com.heinrichreimersoftware.wg_planer.authentication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class WgPlanerAuthenticatorService extends Service {
    @Override
    public IBinder onBind(Intent intent) {

        WgPlanerAuthenticator authenticator = new WgPlanerAuthenticator(this);
        return authenticator.getIBinder();
    }
}