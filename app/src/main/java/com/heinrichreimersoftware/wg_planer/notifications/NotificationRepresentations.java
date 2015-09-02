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
import com.heinrichreimersoftware.wg_planer.data.RepresentationsContentHelper;
import com.heinrichreimersoftware.wg_planer.data.UserContentHelper;
import com.heinrichreimersoftware.wg_planer.structure.LessonTimeFactory;
import com.heinrichreimersoftware.wg_planer.structure.Representation;
import com.heinrichreimersoftware.wg_planer.structure.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class NotificationRepresentations {
    public static final int REPRESENTATIONS_NOTIFICATION_ID = 1;
    public static final int REPRESENTATIONS_NOTIFICATION_PENDING_INTENT_ID = 5;

    public static void makeNotification(Context context) {
        Log.d(MainActivity.TAG, "Making notification for new representations");

        User user = UserContentHelper.getUser(context);

        List<Representation> allRepresentations;

        if (user != null && !user.getSchoolClass().equals("")) {
            allRepresentations = RepresentationsContentHelper.getRepresentationsFuture(context, user.getSchoolClass());
        } else {
            allRepresentations = RepresentationsContentHelper.getRepresentationsFuture(context);
        }

        Calendar currentTime = new GregorianCalendar();

        List<Representation> representations = new ArrayList<>();
        for (Representation representation : allRepresentations) {
            Calendar representationEndTime = representation.getDate();
            Calendar time = LessonTimeFactory.fromRepresentation(representation).endTime;
            representationEndTime.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
            representationEndTime.set(Calendar.MINUTE, time.get(Calendar.MINUTE));
            representationEndTime.set(Calendar.SECOND, time.get(Calendar.SECOND));
            representationEndTime.add(Calendar.MINUTE, 5);
            representationEndTime.add(Calendar.HOUR, -1);
            if (representationEndTime.after(currentTime)) {
                representations.add(representation);
            }
        }

        int representationsCount = representations.size();

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (representationsCount > 0) {
            NotificationCompat.Builder representationNotificationBuilder = new NotificationCompat.Builder(context);
            representationNotificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
            representationNotificationBuilder.setColor(context.getResources().getColor(R.color.material_green_500));

            if (representationsCount > 1) {
                representationNotificationBuilder.setSmallIcon(
                        R.drawable.ic_notification_representations
                );
                String contentTitle = context.getResources().getQuantityString(
                        R.plurals.label_representations_count, representationsCount, representationsCount
                );
                representationNotificationBuilder.setContentTitle(contentTitle);

                String contentText = "";
                boolean first = true;
                for (Representation representation : representations) {
                    if (!first) {
                        contentText += ", ";
                    }
                    contentText += representation.getRepresentedSubjectText();
                    first = false;
                }
                representationNotificationBuilder.setContentText(contentText);
                representationNotificationBuilder.setTicker(contentTitle + ": " + contentText);

                NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
                inboxStyle.setBigContentTitle(contentTitle);
                for (Representation representation : representations) {
                    inboxStyle.addLine(representation.getSummary());
                }
                representationNotificationBuilder.setStyle(inboxStyle);
                representationNotificationBuilder.setNumber(representationsCount);
            } else {
                Representation representation = representations.get(0);

                representationNotificationBuilder.setColor(representation.getRepresentedSubject().getColor());
                representationNotificationBuilder.setSmallIcon(R.drawable.ic_notification_representation);
                representationNotificationBuilder.setContentTitle(
                        representation.getSummary()
                );
                String contentText = representation.getDescription(context);
                representationNotificationBuilder.setContentText(contentText);
                representationNotificationBuilder.setTicker(contentText);

                NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
                bigTextStyle.setBigContentTitle(
                        representation.getSummary()
                );
                bigTextStyle.bigText(contentText);

                representationNotificationBuilder.setStyle(bigTextStyle);
            }

            Intent resultIntent = new Intent(context, MainActivity.class);
            resultIntent.putExtra(MainActivity.INTENT_EXTRA_SELECTED_ITEM, MainActivity.DRAWER_ID_REPRESENTATIONS);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(resultIntent);

            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                    REPRESENTATIONS_NOTIFICATION_PENDING_INTENT_ID, PendingIntent.FLAG_UPDATE_CURRENT
            );
            representationNotificationBuilder.setContentIntent(resultPendingIntent);
            representationNotificationBuilder.setLights(MainActivity.NOTIFICATION_LIGHTS_COLOR, MainActivity.NOTIFICATION_LIGHTS_ON_MS, MainActivity.NOTIFICATION_LIGHTS_OFF_MS);

            representationNotificationBuilder.setDefaults(Notification.DEFAULT_SOUND);
            representationNotificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);

            notificationManager.notify(REPRESENTATIONS_NOTIFICATION_ID, representationNotificationBuilder.build());
            Log.d(MainActivity.TAG, representationsCount + " representations. Notifying");
        } else {
            notificationManager.cancel(REPRESENTATIONS_NOTIFICATION_ID);
            Log.d(MainActivity.TAG, "No representations. Cancelling notification");
        }
    }

    public static void cancelNotification(Context context) {
        Log.d(MainActivity.TAG, "Cancelling notification for new representations");
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(REPRESENTATIONS_NOTIFICATION_ID);
    }
}