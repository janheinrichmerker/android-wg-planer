package com.heinrichreimersoftware.wg_planer.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.heinrichreimersoftware.wg_planer.MainActivity;
import com.heinrichreimersoftware.wg_planer.R;

public class NotificationTimetable {
    public static final int TIMETABLE_NOTIFICATION_ID = 2;
    public static final int TIMETABLE_NOTIFICATION_PENDING_INTENT_ID = 6;

    public static void makeNotification(Context context) {
        Log.d(MainActivity.TAG, "Making notification for new timetable");
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder timetableNotificationBuilder = new NotificationCompat.Builder(context);
        timetableNotificationBuilder.setPriority(NotificationCompat.PRIORITY_LOW);
        timetableNotificationBuilder.setSmallIcon(android.R.drawable.stat_sys_warning);
        timetableNotificationBuilder.setColor(context.getResources().getColor(R.color.material_green_500));
        timetableNotificationBuilder.setContentTitle(
                context.getResources().getString(R.string.title_notification_new_timetable)
        );
        timetableNotificationBuilder.setContentText(
                context.getResources().getString(R.string.label_notification_new_timetable)
        );

        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.putExtra(MainActivity.INTENT_EXTRA_SELECTED_ITEM, MainActivity.DRAWER_ID_TIMETABLE);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                TIMETABLE_NOTIFICATION_PENDING_INTENT_ID, PendingIntent.FLAG_UPDATE_CURRENT
        );
        timetableNotificationBuilder.setContentIntent(resultPendingIntent);
        timetableNotificationBuilder.setLights(MainActivity.NOTIFICATION_LIGHTS_COLOR, MainActivity.NOTIFICATION_LIGHTS_ON_MS, MainActivity.NOTIFICATION_LIGHTS_OFF_MS);

        timetableNotificationBuilder.setDefaults(Notification.DEFAULT_SOUND);
        timetableNotificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);

        notificationManager.notify(TIMETABLE_NOTIFICATION_ID, timetableNotificationBuilder.build());
    }

    public static void cancelNotification(Context context) {
        Log.d(MainActivity.TAG, "Cancelling notification for new timetable");
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(TIMETABLE_NOTIFICATION_ID);
    }
}