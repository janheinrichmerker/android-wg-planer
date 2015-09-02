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
import com.heinrichreimersoftware.wg_planer.data.TeachersContract;
import com.heinrichreimersoftware.wg_planer.structure.Teacher;

import java.io.IOException;
import java.util.List;

public class TeachersSyncAdapter extends AbstractThreadedSyncAdapter {

    private final AccountManager mAccountManager;

    public TeachersSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mAccountManager = AccountManager.get(context);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        try {
            String authToken = mAccountManager.blockingGetAuthToken(account, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, true);

            ParseComServerAccessor parseComService = new ParseComServerAccessor(getContext());
            List<Teacher> teachers = parseComService.getTeachers(authToken);

            int deletedRows = provider.delete(TeachersContract.CONTENT_URI, null, null);
            syncResult.stats.numDeletes += deletedRows;

            if (teachers != null && teachers.size() > 0) {
                int i = 0;
                ContentValues teachersValues[] = new ContentValues[teachers.size()];
                for (Teacher teacher : teachers) {

                    teachersValues[i] = teacher.getContentValues();
                    i++;
                }
                int insertedRows = provider.bulkInsert(TeachersContract.CONTENT_URI, teachersValues);
                syncResult.stats.numInserts += insertedRows;
            }
        } catch (RemoteException | AuthenticatorException | OperationCanceledException | IOException e) {
            e.printStackTrace();
        }
    }
}
