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
import com.heinrichreimersoftware.wg_planer.data.TimetableContentHelper;
import com.heinrichreimersoftware.wg_planer.data.TimetableContract;
import com.heinrichreimersoftware.wg_planer.notifications.NotificationTimetable;
import com.heinrichreimersoftware.wg_planer.structure.Lesson;
import com.heinrichreimersoftware.wg_planer.utils.ClassesUtils;

import java.io.IOException;
import java.util.List;

public class TimetableSyncAdapter extends AbstractThreadedSyncAdapter {

    private final AccountManager mAccountManager;

    public TimetableSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mAccountManager = AccountManager.get(context);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        try {
            String authToken = mAccountManager.blockingGetAuthToken(account, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, true);

            ParseComServerAccessor parseComService = new ParseComServerAccessor(getContext());
            List<Lesson> lessons = parseComService.getTimetable(authToken);

            if (lessons != null) {
                int deletedRows = provider.delete(TimetableContract.CONTENT_URI, null, null);
                syncResult.stats.numDeletes += deletedRows;

                if (lessons.size() > 0) {
                    int i = 0;
                    ContentValues lessonsValues[] = new ContentValues[lessons.size()];
                    for (Lesson lesson : lessons) {
                        lessonsValues[i] = lesson.getContentValues();
                        i++;
                    }
                    int insertedRows = provider.bulkInsert(TimetableContract.CONTENT_URI, lessonsValues);
                    syncResult.stats.numInserts += insertedRows;

                    List<Lesson> oldLessons = TimetableContentHelper.getTimetable(getContext());
                    List<Lesson> filteredLessons = ClassesUtils.filterLessons(getContext(), lessons);
                    if (filteredLessons.size() != oldLessons.size() || !filteredLessons.containsAll(oldLessons)) {
                        NotificationTimetable.makeNotification(getContext());
                    }
                }
            } else {
                syncResult.stats.numParseExceptions++;
            }
        } catch (AuthenticatorException e) {
            e.printStackTrace();
            syncResult.stats.numAuthExceptions++;
        } catch (OperationCanceledException | RemoteException | IOException e) {
            e.printStackTrace();
            syncResult.stats.numIoExceptions++;
        }
    }
}
