package com.heinrichreimersoftware.wg_planer.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.heinrichreimersoftware.wg_planer.R;
import com.heinrichreimersoftware.wg_planer.authentication.AccountGeneral;
import com.heinrichreimersoftware.wg_planer.data.RepresentationsContract;
import com.heinrichreimersoftware.wg_planer.data.TeachersContract;
import com.heinrichreimersoftware.wg_planer.data.TimetableContract;
import com.heinrichreimersoftware.wg_planer.data.UserContract;

public class SyncUtils {

    public static final long SECONDS_PER_MINUTE = 60L;
    public static final long MINUTES_PER_HOUR = 60L;

    public static final long SYNC_INTERVAL_USER = 24 * MINUTES_PER_HOUR * SECONDS_PER_MINUTE;
    public static final long SYNC_INTERVAL_REPRESENTATIONS = 15 * SECONDS_PER_MINUTE;
    public static final long SYNC_INTERVAL_TIMETABLE = 24 * MINUTES_PER_HOUR * SECONDS_PER_MINUTE;
    public static final long SYNC_INTERVAL_TEACHERS = 24 * MINUTES_PER_HOUR * SECONDS_PER_MINUTE;

    public static void initialize(@NonNull Activity activity) {
        AccountManager accountManager = AccountManager.get(activity);
        Account[] accounts = accountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);
        if (accounts.length > 0) {
            Account account = accounts[0];
            setupSyncAdapterIfNeeded(activity, account);
            return;
        }
        accountManager.addAccount(AccountGeneral.ACCOUNT_TYPE, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, null, null, activity, null, null);
        activity.finish();
    }

    public static void setupSyncAdapterIfNeeded(Activity activity, Account account) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        boolean shouldSyncAutomatically = sharedPreferences.getBoolean(activity.getString(R.string.key_preference_automatic_sync), true);

        String[] authorities = {
                UserContract.AUTHORITY,
                RepresentationsContract.AUTHORITY,
                TimetableContract.AUTHORITY,
                TeachersContract.AUTHORITY
        };

        for (String authority : authorities) {
            ContentResolver.setIsSyncable(account, authority, 1);

            if (shouldSyncAutomatically) {
                ContentResolver.setSyncAutomatically(account, authority, true);

                Bundle extras = new Bundle();

                long interval = -1;
                switch (authority) {
                    case UserContract.AUTHORITY:
                        interval = SYNC_INTERVAL_USER;
                        break;
                    case RepresentationsContract.AUTHORITY:
                        interval = SYNC_INTERVAL_REPRESENTATIONS;
                        break;
                    case TimetableContract.AUTHORITY:
                        interval = SYNC_INTERVAL_TIMETABLE;
                        break;
                    case TeachersContract.AUTHORITY:
                        interval = SYNC_INTERVAL_TEACHERS;
                        break;
                }

                ContentResolver.addPeriodicSync(account, authority, extras, interval);
            } else {
                ContentResolver.setSyncAutomatically(account, authority, false);
            }
        }
    }
}
