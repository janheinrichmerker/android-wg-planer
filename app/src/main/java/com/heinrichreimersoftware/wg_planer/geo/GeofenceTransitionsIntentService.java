package com.heinrichreimersoftware.wg_planer.geo;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.heinrichreimersoftware.wg_planer.Constants;
import com.heinrichreimersoftware.wg_planer.MainActivity;
import com.heinrichreimersoftware.wg_planer.R;
import com.heinrichreimersoftware.wg_planer.data.TimetableContentHelper;
import com.heinrichreimersoftware.wg_planer.notifications.LessonNotification;
import com.heinrichreimersoftware.wg_planer.structure.Lesson;
import com.heinrichreimersoftware.wg_planer.structure.LessonTimeFactory;

import java.util.List;

public class GeofenceTransitionsIntentService extends IntentService {

    private static final String EXTRA_TRIGGERED_SELF = "EXTRA_TRIGGERED_SELF";
    private static final String EXTRA_TRANSITION = "com.google.android.location.intent.extra.transition";
    private static final String EXTRA_GEOFENCE_LIST = "com.google.android.location.intent.extra.geofence_list";
    private static final String EXTRA_TRIGGERING_LOCATION = "com.google.android.location.intent.extra.triggering_location";

    public GeofenceTransitionsIntentService() {
        this(GeofenceTransitionsIntentService.class.getSimpleName());
    }

    public GeofenceTransitionsIntentService(String name) {
        super(name);
    }

    protected void onHandleIntent(Intent intent) {
        Log.d(MainActivity.TAG, "onHandleIntent(" + intent + ")");
        Lesson nearestLesson = getNearestLesson(true);
        if (intent.hasExtra(EXTRA_TRANSITION) && intent.hasExtra(EXTRA_GEOFENCE_LIST) && intent.hasExtra(EXTRA_TRIGGERING_LOCATION)) {
            GeofencingEvent geoFenceEvent = GeofencingEvent.fromIntent(intent);
            if (geoFenceEvent.hasError()) {
                int errorCode = geoFenceEvent.getErrorCode();
                Log.e(MainActivity.TAG, "Location Services error: " + errorCode);
                cancelSelf();
                setSilent(false);
            } else {
                int transitionType = geoFenceEvent.getGeofenceTransition();
                if (transitionType == Geofence.GEOFENCE_TRANSITION_ENTER) {
                    setSilent(true);
                    sendNotificationIfNeeded(nearestLesson);
                } else if (transitionType == Geofence.GEOFENCE_TRANSITION_EXIT) {
                    cancelSelf();
                    setSilent(false);
                }
            }
        } else if (intent.hasExtra(EXTRA_TRIGGERED_SELF) && intent.getBooleanExtra(EXTRA_TRIGGERED_SELF, false)) {
            sendNotificationIfNeeded(nearestLesson);
        } else {
            cancelSelf();
            setSilent(false);
        }
    }

    private Lesson getNearestLesson(boolean allowCurrentLesson) {
        Log.d(MainActivity.TAG, "getNearestLesson(" + allowCurrentLesson + ")");
        List<Lesson> lessons = TimetableContentHelper.getTimetable(getBaseContext());
        Lesson nearestLesson = null;
        long nearestLessonStartTime = Long.MAX_VALUE;
        for (Lesson lesson : lessons) {
            long lessonStartTime = LessonTimeFactory.fromLesson(lesson).getStartTimeMillis();
            long lessonEndTime = LessonTimeFactory.fromLesson(lesson).getEndTimeMillis();
            if ((lessonStartTime >= System.currentTimeMillis() || (allowCurrentLesson && lessonEndTime >= System.currentTimeMillis())) && lessonStartTime < nearestLessonStartTime) {
                nearestLesson = lesson;
                nearestLessonStartTime = lessonStartTime;
            }
        }
        Log.d(MainActivity.TAG, "nearestLesson: " + (nearestLesson == null ? "null" : nearestLesson.getFormatter(getBaseContext()).subjects() + "; " + nearestLesson.getFormatter(getBaseContext()).time()));
        return nearestLesson;
    }

    private void cancelSelf() {
        Log.d(MainActivity.TAG, "cancelSelf()");

        Intent retryIntent = new Intent(getBaseContext(), this.getClass());
        retryIntent.putExtra(EXTRA_TRIGGERED_SELF, true);
        PendingIntent pendingIntent = PendingIntent.getService(getBaseContext(), 0, retryIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        LessonNotification.cancel(getBaseContext());
    }

    private void sendNotificationIfNeeded(Lesson lesson) {
        Log.d(MainActivity.TAG, "sendNotificationIfNeeded(" + (lesson == null ? "null" : lesson.getFormatter(getBaseContext()).subjects() + "; " + lesson.getFormatter(getBaseContext()).time()) + ")");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean geoNotificationsEnabled = sharedPreferences.getBoolean(getBaseContext().getString(R.string.key_preference_geo_notifications), true);

        if (lesson == null || !geoNotificationsEnabled) {
            cancelSelf();
            return;
        }
        long nearestLessonStartTime = LessonTimeFactory.fromLesson(lesson).getStartTimeMillis();
        if (System.currentTimeMillis() >= nearestLessonStartTime - 2 * Constants.FIVE_MINUTES) {
            sendNotification(lesson);
        } else {
            Intent retryIntent = new Intent(getBaseContext(), GeofenceTransitionsIntentService.class);
            retryIntent.putExtra(EXTRA_TRIGGERED_SELF, true);
            PendingIntent pendingIntent = PendingIntent.getService(this, 0, retryIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setWindow(AlarmManager.RTC, nearestLessonStartTime - 2 * Constants.FIVE_MINUTES, nearestLessonStartTime, pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC, nearestLessonStartTime - Constants.FIVE_MINUTES, pendingIntent);
            }
        }
    }

    private void sendNotification(Lesson lesson) {
        Log.d(MainActivity.TAG, "sendNotification(" + lesson + ")");
        LessonNotification.notify(getBaseContext(), lesson);

        Lesson nearestLesson = getNearestLesson(false);
        sendNotificationIfNeeded(nearestLesson);
    }

    private void setSilent(boolean silent) {
        Log.d(MainActivity.TAG, "setSilent(" + silent + ")");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean geoSilentEnabled = sharedPreferences.getBoolean(getBaseContext().getString(R.string.key_preference_geo_silent), true);
        if (geoSilentEnabled) {
            AudioManager mode = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            mode.setRingerMode(silent ? AudioManager.RINGER_MODE_SILENT : AudioManager.RINGER_MODE_NORMAL);
        }
    }
}
