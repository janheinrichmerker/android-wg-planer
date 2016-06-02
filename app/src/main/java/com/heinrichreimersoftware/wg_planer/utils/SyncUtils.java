package com.heinrichreimersoftware.wg_planer.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.heinrichreimersoftware.wg_planer.Constants;
import com.heinrichreimersoftware.wg_planer.R;
import com.heinrichreimersoftware.wg_planer.authentication.AccountGeneral;

public class SyncUtils {

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
                Constants.CONTENT_AUTHORITY_USERS,
                Constants.CONTENT_AUTHORITY_REPRESENTATIONS,
                Constants.CONTENT_AUTHORITY_TIMETABLE,
                Constants.CONTENT_AUTHORITY_TEACHERS
        };

        for (String authority : authorities) {
            ContentResolver.setIsSyncable(account, authority, 1);

            if (shouldSyncAutomatically) {
                ContentResolver.setSyncAutomatically(account, authority, true);

                Bundle extras = new Bundle();

                long interval = -1;
                switch (authority) {
                    case Constants.CONTENT_AUTHORITY_USERS:
                        interval = Constants.ONE_DAY;
                        break;
                    case Constants.CONTENT_AUTHORITY_REPRESENTATIONS:
                        interval = Constants.FIFTEEN_MINUTES;
                        break;
                    case Constants.CONTENT_AUTHORITY_TIMETABLE:
                        interval = Constants.ONE_DAY;
                        break;
                    case Constants.CONTENT_AUTHORITY_TEACHERS:
                        interval = Constants.ONE_DAY;
                        break;
                }

                ContentResolver.addPeriodicSync(account, authority, extras, interval);
            } else {
                ContentResolver.setSyncAutomatically(account, authority, false);
            }
        }
    }
}
