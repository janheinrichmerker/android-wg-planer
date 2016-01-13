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

import java.util.Arrays;
import java.util.List;

public class TimetableSyncAdapter extends AbstractThreadedSyncAdapter {

    public TimetableSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    public TimetableSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        List<Lesson> oldLessons = Arrays.asList(TimetableContentHelper.getTimetable(getContext()));

        SyncServerInterface serverInterface = new SyncServerInterface(getContext());
        List<Lesson> lessons = serverInterface.getTimetable();

        lessons = ClassesUtils.filterLessons(getContext(), lessons);

        if (lessons.size() != oldLessons.size() || !lessons.containsAll(oldLessons)) {
            TimetableNotification.notify(getContext());
        }

        TimetableContentHelper.clearTimetable(getContext());

        if (lessons.size() > 0) {
            TimetableContentHelper.addLessons(getContext(), lessons.toArray(new Lesson[lessons.size()]));
        }
    }
}
