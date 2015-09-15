package com.heinrichreimersoftware.wg_planer.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.heinrichreimersoftware.wg_planer.Constants;
import com.heinrichreimersoftware.wg_planer.MainActivity;
import com.heinrichreimersoftware.wg_planer.R;

public class TimetableNotification {

    public static void notify(Context context) {
        Log.d(MainActivity.TAG, "Making notification for new timetable");
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Resources resources = context.getResources();

        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.putExtra(MainActivity.EXTRA_SELECTED_ITEM, MainActivity.DRAWER_ID_TIMETABLE);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                Constants.TIMETABLE_NOTIFICATION_PENDING_INTENT_ID, PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
                .setContentTitle(resources.getString(R.string.title_notification_new_timetable))
                .setContentText(resources.getString(R.string.label_notification_new_timetable))
                .setContentIntent(resultPendingIntent)
                .setSmallIcon(R.drawable.ic_notification_timetable)
                .setColor(context.getResources().getColor(R.color.material_green_500))
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setCategory(NotificationCompat.CATEGORY_STATUS);

        notificationManager.notify(Constants.TIMETABLE_NOTIFICATION_ID, notification.build());
    }

    public static void cancel(Context context) {
        Log.d(MainActivity.TAG, "Cancelling notification for new timetable");
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(Constants.TIMETABLE_NOTIFICATION_ID);
    }
}