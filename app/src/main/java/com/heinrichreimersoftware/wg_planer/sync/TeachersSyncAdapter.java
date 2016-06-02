package com.heinrichreimersoftware.wg_planer.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import com.heinrichreimersoftware.wg_planer.content.TeachersContentHelper;
import com.heinrichreimersoftware.wg_planer.structure.Teacher;

public class TeachersSyncAdapter extends AbstractThreadedSyncAdapter {

    public TeachersSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    public TeachersSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Teacher[] teachers = new SyncServerInterface(getContext()).getTeachers();

        TeachersContentHelper.clearTeachers(getContext());

        if (teachers.length > 0) {
            TeachersContentHelper.addTeachers(getContext(), teachers);
        }
    }
}
