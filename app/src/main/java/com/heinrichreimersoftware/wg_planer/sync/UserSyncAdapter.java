package com.heinrichreimersoftware.wg_planer.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import com.heinrichreimersoftware.wg_planer.authentication.AccountGeneral;
import com.heinrichreimersoftware.wg_planer.content.UserContentHelper;
import com.heinrichreimersoftware.wg_planer.structure.User;

import java.io.IOException;

public class UserSyncAdapter extends AbstractThreadedSyncAdapter {

    private final AccountManager mAccountManager;

    public UserSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mAccountManager = AccountManager.get(context);
    }

    public UserSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mAccountManager = AccountManager.get(context);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        try {
            String username = mAccountManager.blockingGetAuthToken(account, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, true);

            SyncServerInterface serverInterface = new SyncServerInterface(getContext());
            User user = serverInterface.getUserInfo(username);

            UserContentHelper.clearUsers(getContext());

            if (user != null) {
                UserContentHelper.addUser(getContext(), user);
            }
        } catch (OperationCanceledException | IOException | AuthenticatorException e) {
            e.printStackTrace();
            syncResult.stats.numParseExceptions++;
        }
    }
}
