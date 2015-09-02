package com.heinrichreimersoftware.wg_planer.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class UserSyncService extends Service {

    private static final Object sSyncAdapterLock = new Object();
    private static UserSyncAdapter sSyncAdapter = null;

    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new UserSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}