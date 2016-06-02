package com.heinrichreimersoftware.wg_planer.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.heinrichreimersoftware.wg_planer.Constants;
import com.heinrichreimersoftware.wg_planer.MainActivity;
import com.heinrichreimersoftware.wg_planer.R;
import com.heinrichreimersoftware.wg_planer.content.RepresentationsContentHelper;
import com.heinrichreimersoftware.wg_planer.content.UserContentHelper;
import com.heinrichreimersoftware.wg_planer.structure.Representation;
import com.heinrichreimersoftware.wg_planer.structure.User;

import java.util.ArrayList;
import java.util.List;

public class RepresentationsNotification {

    public static void notify(Context context) {
        Log.d(MainActivity.TAG, "Making notification for new representations");

        User user = UserContentHelper.getUser(context);

        Representation[] allRepresentations;

        if (user != null && user.getSchoolClasses() != null && user.getSchoolClasses().length > 0) {
            allRepresentations = RepresentationsContentHelper.getRepresentationsFuture(context, user.getSchoolClasses());
        } else {
            allRepresentations = RepresentationsContentHelper.getRepresentationsFuture(context);
        }

        List<Representation> representations = new ArrayList<>();
        for (Representation representation : allRepresentations) {
            if (!representation.isOver()) {
                representations.add(representation);
            }
        }

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (representations.size() > 0) {
            NotificationCompat.Builder notification = new NotificationCompat.Builder(context);

            if (representations.size() > 1) {
                String contentTitle = context.getResources().getQuantityString(
                        R.plurals.label_representations_count, representations.size(), representations.size()
                );
                String contentText = "";
                boolean first = true;
                for (Representation representation : representations) {
                    if (!first) {
                        contentText += ", ";
                    }
                    contentText += Representation.Formatter.subject(context, representation);
                    first = false;
                }

                NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
                style.setBigContentTitle(contentTitle);
                for (Representation representation : representations) {
                    style.addLine(Representation.Formatter.summary(context, representation));
                }

                notification.setContentTitle(contentTitle)
                        .setContentText(contentText)
                        .setSmallIcon(R.drawable.ic_notification_representations)
                        .setTicker(contentTitle + ": " + contentText)
                        .setNumber(representations.size())
                        .setStyle(style);
            } else {
                Representation representation = representations.get(0);

                String contentText = Representation.Formatter.description(context, representation);

                NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle()
                        .bigText(contentText);

                notification.setContentTitle(Representation.Formatter.summary(context, representation))
                        .setContentText(contentText)
                        .setSmallIcon(R.drawable.ic_notification_representation)
                        .setColor(representation.getSubject().getColor())
                        .setTicker(contentText)
                        .setStyle(style);
            }

            Intent resultIntent = new Intent(context, MainActivity.class);
            resultIntent.putExtra(MainActivity.EXTRA_SELECTED_ITEM, MainActivity.DRAWER_ID_REPRESENTATIONS);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(resultIntent);

            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                    Constants.REPRESENTATIONS_NOTIFICATION_PENDING_INTENT_ID, PendingIntent.FLAG_UPDATE_CURRENT
            );

            notification.setContentIntent(resultPendingIntent)
                    .setColor(ContextCompat.getColor(context, R.color.material_green_500))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_EVENT)
                    .setLights(Constants.NOTIFICATION_LIGHTS_COLOR, Constants.NOTIFICATION_LIGHTS_ON_MS, Constants.NOTIFICATION_LIGHTS_OFF_MS);

            notificationManager.notify(Constants.REPRESENTATIONS_NOTIFICATION_ID, notification.build());
            Log.d(MainActivity.TAG, representations.size() + " representations. Notifying");
        } else {
            notificationManager.cancel(Constants.REPRESENTATIONS_NOTIFICATION_ID);
            Log.d(MainActivity.TAG, "No representations. Cancelling notification");
        }
    }

    public static void cancel(Context context) {
        Log.d(MainActivity.TAG, "Cancelling notification for new representations");
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(Constants.REPRESENTATIONS_NOTIFICATION_ID);
    }
}