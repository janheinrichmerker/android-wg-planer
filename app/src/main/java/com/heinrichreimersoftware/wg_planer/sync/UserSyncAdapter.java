package com.heinrichreimersoftware.wg_planer.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.RemoteException;

import com.heinrichreimersoftware.wg_planer.authentication.AccountGeneral;
import com.heinrichreimersoftware.wg_planer.data.UserContract;
import com.heinrichreimersoftware.wg_planer.structure.User;

import java.io.IOException;

public class UserSyncAdapter extends AbstractThreadedSyncAdapter {

    private final AccountManager mAccountManager;

    public UserSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mAccountManager = AccountManager.get(context);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        try {
            String authToken = mAccountManager.blockingGetAuthToken(account, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, true);

            SyncServerInterface parseComService = new SyncServerInterface(getContext());
            User user = parseComService.getUserInfo(authToken);

            if (user != null) {
                if (user.getUsername() != null && !user.getUsername().equals("")
                        && user.getPassword() != null && !user.getPassword().equals("")
                        && user.getOid() != null && !user.getOid().equals("")) {

                    int deletedRows = provider.delete(UserContract.CONTENT_URI, null, null);
                    syncResult.stats.numDeletes += deletedRows;

                    ContentValues userValues[] = new ContentValues[1];
                    userValues[0] = user.getContentValues(getContext());

                    int insertedRows = provider.bulkInsert(UserContract.CONTENT_URI, userValues);
                    syncResult.stats.numInserts += insertedRows;
                } else {
                    syncResult.stats.numParseExceptions++;
                }
            } else {
                syncResult.stats.numParseExceptions++;
            }
        } catch (OperationCanceledException | RemoteException | IOException | AuthenticatorException e) {
            e.printStackTrace();
            syncResult.stats.numParseExceptions++;
        }
    }
}
