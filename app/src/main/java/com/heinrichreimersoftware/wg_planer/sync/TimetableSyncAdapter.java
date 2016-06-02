package com.heinrichreimersoftware.wg_planer.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import com.heinrichreimersoftware.wg_planer.content.TimetableContentHelper;
import com.heinrichreimersoftware.wg_planer.notifications.TimetableNotification;
import com.heinrichreimersoftware.wg_planer.structure.Lesson;
import com.heinrichreimersoftware.wg_planer.utils.ClassesUtils;
import com.heinrichreimersoftware.wg_planer.utils.Utils;

public class TimetableSyncAdapter extends AbstractThreadedSyncAdapter {

    public TimetableSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    public TimetableSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Lesson[] oldLessons = TimetableContentHelper.getTimetable(getContext());
        Lesson[] lessons = new SyncServerInterface(getContext()).getTimetable();

        lessons = ClassesUtils.filterLessons(getContext(), lessons);

        if (lessons.length != oldLessons.length || !Utils.containsAll(lessons, oldLessons)) {
            TimetableNotification.notify(getContext());
        }

        TimetableContentHelper.clearTimetable(getContext());

        if (lessons.length > 0) {
            TimetableContentHelper.addLessons(getContext(), lessons);
        }
    }
}
