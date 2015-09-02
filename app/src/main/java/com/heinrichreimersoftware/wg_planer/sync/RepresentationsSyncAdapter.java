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
import com.heinrichreimersoftware.wg_planer.data.RepresentationsContentHelper;
import com.heinrichreimersoftware.wg_planer.data.RepresentationsContract;
import com.heinrichreimersoftware.wg_planer.notifications.NotificationRepresentations;
import com.heinrichreimersoftware.wg_planer.structure.Representation;
import com.heinrichreimersoftware.wg_planer.utils.ClassesUtils;

import java.io.IOException;
import java.util.List;

public class RepresentationsSyncAdapter extends AbstractThreadedSyncAdapter {

    private final AccountManager mAccountManager;

    public RepresentationsSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mAccountManager = AccountManager.get(context);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        try {
            String authToken = mAccountManager.blockingGetAuthToken(account, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, true);

            ParseComServerAccessor parseComService = new ParseComServerAccessor(getContext());
            List<Representation> representations = parseComService.getRepresentations(authToken);

            int deletedRows = provider.delete(RepresentationsContract.CONTENT_URI, null, null);
            syncResult.stats.numDeletes += deletedRows;

            if (representations != null && representations.size() > 0) {
                int i = 0;
                ContentValues representationsValues[] = new ContentValues[representations.size()];
                for (Representation representation : representations) {
                    representationsValues[i] = representation.getContentValues();
                    i++;
                }
                provider.bulkInsert(RepresentationsContract.CONTENT_URI, representationsValues);

                List<Representation> oldRepresentations = RepresentationsContentHelper.getRepresentations(getContext());
                List<Representation> filteredRepresentations = ClassesUtils.filterRepresentations(getContext(), representations);
                if (filteredRepresentations.size() != oldRepresentations.size()/* || !representations.containsAll(oldRepresentations)*/) {
                    NotificationRepresentations.makeNotification(getContext());
                }
            }
        } catch (OperationCanceledException | RemoteException | AuthenticatorException | IOException e) {
            e.printStackTrace();
        }
    }
}
