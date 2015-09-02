package com.heinrichreimersoftware.wg_planer.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class RepresentationsSyncService extends Service {

    private static final Object sSyncAdapterLock = new Object();
    private static RepresentationsSyncAdapter sSyncAdapter = null;

    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new RepresentationsSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}