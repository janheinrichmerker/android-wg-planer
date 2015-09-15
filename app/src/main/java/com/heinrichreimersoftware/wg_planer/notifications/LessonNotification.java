package com.heinrichreimersoftware.wg_planer.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.Html;
import android.util.Log;

import com.heinrichreimersoftware.wg_planer.Constants;
import com.heinrichreimersoftware.wg_planer.MainActivity;
import com.heinrichreimersoftware.wg_planer.R;
import com.heinrichreimersoftware.wg_planer.structure.Lesson;
import com.heinrichreimersoftware.wg_planer.structure.LessonTimeFactory;

public class LessonNotification {
    public static void notify(Context context, Lesson lesson) {
        Log.d(MainActivity.TAG, "Making notification for lesson: " + lesson);
        Lesson.Formatter formatter = lesson.getFormatter(context);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.putExtra(MainActivity.EXTRA_SELECTED_ITEM, MainActivity.DRAWER_ID_TIMETABLE);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                Constants.GEOFENCE_NOTIFICATION_PENDING_INTENT_ID, PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle()
                .bigText(Html.fromHtml(context.getString(R.string.text_notification_lesson, formatter.rooms(true), formatter.time(false), formatter.teachers())));

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
                .setContentTitle(formatter.subjects())
                .setContentText(formatter.rooms(true))
                .setContentIntent(resultPendingIntent)
                .setSmallIcon(R.drawable.ic_notification_timetable)
                .setColor(lesson.getSubjects().size() <= 1 ? formatter.color() : context.getResources().getColor(R.color.material_green_500))
                .setWhen(LessonTimeFactory.fromLesson(lesson).getStartTimeMillis())
                .setCategory(NotificationCompat.CATEGORY_EVENT)
                .setStyle(style)
                .setOnlyAlertOnce(true)
                .setAutoCancel(false);
        if (lesson.getSubjects().size() > 1) {
            notification.setNumber(lesson.getSubjects().size());
        }
        notificationManager.notify(Constants.GEOFENCE_NOTIFICATION_ID, notification.build());
    }

    public static void cancel(Context context) {
        Log.d(MainActivity.TAG, "Cancelling notification for lesson");
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(Constants.GEOFENCE_NOTIFICATION_ID);
    }
}
