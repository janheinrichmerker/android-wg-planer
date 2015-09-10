package com.heinrichreimersoftware.wg_planer.geo;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.heinrichreimersoftware.wg_planer.MainActivity;
import com.heinrichreimersoftware.wg_planer.R;
import com.heinrichreimersoftware.wg_planer.data.TimetableContentHelper;
import com.heinrichreimersoftware.wg_planer.structure.Lesson;
import com.heinrichreimersoftware.wg_planer.structure.LessonTimeFactory;

import java.util.List;

public class GeofenceTransitionsIntentService extends IntentService {

    public static final int GEOFENCE_NOTIFICATION_ID = 3;
    private static final long FIVE_MINUTES = 5 * 60 * 1000;
    private static final String EXTRA_TRIGGERED_SELF = "EXTRA_TRIGGERED_SELF";

    public GeofenceTransitionsIntentService() {
        this(GeofenceTransitionsIntentService.class.getSimpleName());
    }

    public GeofenceTransitionsIntentService(String name) {
        super(name);
        Log.d(MainActivity.TAG, "GeofenceTransitionsIntentService constructor: name=" + name);
    }

    protected void onHandleIntent(Intent intent) {
        Lesson nearestLesson = getNearestLesson();
        if (intent.hasExtra(EXTRA_TRIGGERED_SELF) && intent.getBooleanExtra(EXTRA_TRIGGERED_SELF, false)) {
            Log.d(MainActivity.TAG, "GeofenceTransitionsIntentService started from alarm manager");
            long nearestLessonStartTime = LessonTimeFactory.fromLesson(nearestLesson).getStartTimeMillis();
            if (System.currentTimeMillis() >= nearestLessonStartTime - FIVE_MINUTES) {
                sendNotification(nearestLesson);
            } else {
                delaySelf(nearestLesson);
            }
        } else {
            GeofencingEvent geoFenceEvent = GeofencingEvent.fromIntent(intent);
            if (geoFenceEvent.hasError()) {
                int errorCode = geoFenceEvent.getErrorCode();
                Log.e(MainActivity.TAG, "Location Services error: " + errorCode);
            } else {
                Log.d(MainActivity.TAG, "GeofenceTransitionsIntentService started from geofence");
                int transitionType = geoFenceEvent.getGeofenceTransition();
                if (transitionType == Geofence.GEOFENCE_TRANSITION_ENTER) {
                    Log.d(MainActivity.TAG, "GeofenceTransitionsIntentService transition type: GEOFENCE_TRANSITION_ENTER");
                    delaySelf(nearestLesson);
                } else if (transitionType == Geofence.GEOFENCE_TRANSITION_EXIT) {
                    Log.d(MainActivity.TAG, "GeofenceTransitionsIntentService transition type: GEOFENCE_TRANSITION_EXIT");
                    cancelSelf();
                }
            }
        }
    }

    private Lesson getNearestLesson() {
        List<Lesson> lessons = TimetableContentHelper.getTimetable(getApplicationContext());
        Lesson nearestLesson = null;
        long nearestLessonStartTime = Long.MAX_VALUE;
        Log.d(MainActivity.TAG, "GeofenceTransitionsIntentService current time: " + System.currentTimeMillis());
        Log.d(MainActivity.TAG, "GeofenceTransitionsIntentService nearest lesson start time: " + nearestLessonStartTime);
        for (Lesson lesson : lessons) {
            long lessonStartTime = LessonTimeFactory.fromLesson(lesson).getStartTimeMillis();
            Log.d(MainActivity.TAG, "GeofenceTransitionsIntentService possible nearest lesson start time: " + lessonStartTime);
            if (lessonStartTime >= System.currentTimeMillis() && lessonStartTime < nearestLessonStartTime) {
                nearestLesson = lesson;
                nearestLessonStartTime = lessonStartTime;
                Log.d(MainActivity.TAG, "GeofenceTransitionsIntentService nearest lesson start time: " + nearestLessonStartTime);
            }
        }
        Log.d(MainActivity.TAG, "GeofenceTransitionsIntentService nearest lesson: " + nearestLesson);
        return nearestLesson;
    }

    private void delaySelf(Lesson nearestLesson) {
        long nearestLessonStartTime = LessonTimeFactory.fromLesson(nearestLesson).getStartTimeMillis();
        Log.d(MainActivity.TAG, "GeofenceTransitionsIntentService delaying self to " + (nearestLessonStartTime - FIVE_MINUTES));

        Intent retryIntent = new Intent(getApplicationContext(), this.getClass());
        retryIntent.putExtra(EXTRA_TRIGGERED_SELF, true);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, retryIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager =
                (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setWindow(AlarmManager.RTC, nearestLessonStartTime - 2 * FIVE_MINUTES, nearestLessonStartTime, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC, nearestLessonStartTime - FIVE_MINUTES, pendingIntent);
        }
    }

    private void cancelSelf() {
        Log.d(MainActivity.TAG, "GeofenceTransitionsIntentService canceling self");
        Intent retryIntent = new Intent(getApplicationContext(), this.getClass());
        retryIntent.putExtra(EXTRA_TRIGGERED_SELF, true);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, retryIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager =
                (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(GEOFENCE_NOTIFICATION_ID);
    }

    private void sendNotification(Lesson lesson) {
        Log.d(MainActivity.TAG, "GeofenceTransitionsIntentService sending out notification");

        Lesson.Formatter formatter = lesson.getFormatter(getApplicationContext());

        NotificationManager notificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle(formatter.subjects())
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setSmallIcon(android.R.drawable.stat_notify_sync)
                .setColor(getApplicationContext().getResources().getColor(R.color.material_green_500))
                .setContentText(formatter.rooms());
        notificationManager.notify(GEOFENCE_NOTIFICATION_ID, notification.build());
    }
}
