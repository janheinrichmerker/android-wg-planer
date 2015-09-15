package com.heinrichreimersoftware.wg_planer.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncInfo;
import android.content.SyncStatusObserver;
import android.os.Build;
import android.util.Log;

import com.heinrichreimersoftware.wg_planer.MainActivity;
import com.heinrichreimersoftware.wg_planer.authentication.AccountGeneral;

import java.util.ArrayList;
import java.util.List;

public class SyncStatusManager {

    public static final int STATE_NOW_SYNCING = 1;
    public static final int STATE_NO_LONGER_SYNCING = 2;
    public static final int STATE_NOW_PENDING = 3;
    public static final int STATE_NO_LONGER_PENDING = 4;
    private final SyncStatusObserver syncObserver;
    private Context mContext;
    private Object handleSyncObserver;

    private String mAuthority;

    private boolean syncActive = false;

    private List<OnSyncStatusChangedListener> listeners = new ArrayList<>();

    public SyncStatusManager(Context context, String authority) {
        mContext = context;
        mAuthority = authority;
        syncObserver = new SyncStatusObserver() {
            @Override
            public void onStatusChanged(final int which) {
                AccountManager accountManager = AccountManager.get(mContext);
                if (accountManager != null) {
                    Account[] accounts = accountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);
                    if (accounts.length > 0) {
                        Account account = accounts[0];


                        int state = -1;
                        if (which == ContentResolver.SYNC_OBSERVER_TYPE_PENDING) {
                            // 'Pending' state changed.
                            if (ContentResolver.isSyncPending(account, mAuthority)) {
                                // There is now a pending sync.
                                state = STATE_NOW_PENDING;
                            } else {
                                // There is no longer a pending sync.
                                state = STATE_NO_LONGER_PENDING;
                            }
                        } else if (which == ContentResolver.SYNC_OBSERVER_TYPE_ACTIVE) {
                            // 'Active' state changed.
                            if (ContentResolver.isSyncActive(account, mAuthority)) {
                                // There is now an active sync.
                                state = STATE_NOW_SYNCING;
                            } else {
                                // There is no longer an active sync.
                                state = STATE_NO_LONGER_SYNCING;
                            }
                        }

                        Log.d(MainActivity.TAG, "Notifying sync status: " + state);
                        for (OnSyncStatusChangedListener listener : listeners) {
                            listener.onStatusChanged(state, mAuthority);
                        }
                    }
                }
            }
        };
    }

    @SuppressWarnings("deprecation")
    private static boolean isSyncActive(Account account, String authority) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            for (SyncInfo syncInfo : ContentResolver.getCurrentSyncs()) {
                if (syncInfo.account.equals(account) && syncInfo.authority.equals(authority)) {
                    return true;
                }
            }
            return false;
        } else {
            SyncInfo currentSync = ContentResolver.getCurrentSync();
            return currentSync != null && currentSync.account.equals(account) && currentSync.authority.equals(authority);
        }
    }

    public String getAuthoritiy() {
        return mAuthority;
    }

    public SyncStatusManager setAuthoritiy(String authority) {
        mAuthority = authority;
        return this;
    }

    public SyncStatusManager addOnSyncStatusChangedListener(OnSyncStatusChangedListener listener) {
        listeners.add(listener);
        return this;
    }

    public SyncStatusManager removeOnSyncStatusChangedListener(OnSyncStatusChangedListener listener) {
        listeners.remove(listener);
        return this;
    }

    public SyncStatusManager start() {
        handleSyncObserver = ContentResolver.addStatusChangeListener(ContentResolver.SYNC_OBSERVER_TYPE_ACTIVE | ContentResolver.SYNC_OBSERVER_TYPE_PENDING, syncObserver);
        return this;
    }

    public SyncStatusManager stop() {
        if (handleSyncObserver != null) {
            ContentResolver.removeStatusChangeListener(handleSyncObserver);
        }
        return this;
    }

    public interface OnSyncStatusChangedListener {
        void onStatusChanged(int state, String affectedAuthority);
    }
}
