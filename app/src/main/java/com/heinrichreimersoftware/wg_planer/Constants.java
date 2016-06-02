package com.heinrichreimersoftware.wg_planer;

import android.graphics.Color;

public class Constants {
    /**
     * Prefixes
     */
    private static final String PREFIX = "com.heinrichreimersoftware.wg_planer";

    /**
     * Times
     */
    public static final long FIVE_MINUTES = 5 * 60 * 1000L;
    public static final long TEN_MINUTES = 10 * 60 * 1000L;
    public static final long FIFTEEN_MINUTES = 15 * 60 * 1000L;
    public static final long ONE_DAY = 24 * 60 * 60 * 1000L;

    /**
     * Notification light settings for representation notification
     */
    public static final int NOTIFICATION_LIGHTS_COLOR = Color.GREEN;
    public static final int NOTIFICATION_LIGHTS_ON_MS = 1500;
    public static final int NOTIFICATION_LIGHTS_OFF_MS = 2500;

    /**
     * Permissions
     */
    public static final String ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";

    /**
     * URLs
     */
    public static final String API_URL = "http://heinrichreimersoftware.com/api/wgplaner/";


    /**
     * Database
     */
    public static final String DATABASE_NAME = PREFIX + ".db";
    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_TABLE_NAME_LESSONS = "wg_planer_lessons";
    public static final String DATABASE_TABLE_NAME_REPRESENTATIONS = "wg_planer_representations";
    public static final String DATABASE_TABLE_NAME_FROM_TOS = "wg_planer_from_tos";
    public static final String DATABASE_TABLE_NAME_SUBJECTS = "wg_planer_subjects";
    public static final String DATABASE_TABLE_NAME_TEACHER_SUBJECTS = "wg_planer_teacher_subjects";
    public static final String DATABASE_TABLE_NAME_TEACHERS = "wg_planer_teachers";
    public static final String DATABASE_TABLE_NAME_USERS = "wg_planer_users";
    public static final String DATABASE_COLUMN_NAME_DAY = "wg_planer_day";
    public static final String DATABASE_COLUMN_NAME_FIRST_LESSON_NUMBER = "wg_planer_first_lesson_number";
    public static final String DATABASE_COLUMN_NAME_LAST_LESSON_NUMBER = "wg_planer_last_lesson_number";
    public static final String DATABASE_COLUMN_NAME_SUBJECTS = "wg_planer_subjects";
    public static final String DATABASE_COLUMN_NAME_SCHOOL_CLASS = "wg_planer_school_class";
    public static final String DATABASE_COLUMN_NAME_SUBJECT = "wg_planer_subject";
    public static final String DATABASE_COLUMN_NAME_FROM = "wg_planer_from";
    public static final String DATABASE_COLUMN_NAME_TO = "wg_planer_to";
    public static final String DATABASE_COLUMN_NAME_DESCRIPTION = "wg_planer_description";
    public static final String DATABASE_COLUMN_NAME_SHORTHAND = "wg_planer_shorthand";
    public static final String DATABASE_COLUMN_NAME_FULL_NAME = "wg_planer_full_name";
    public static final String DATABASE_COLUMN_NAME_COLOR = "wg_planer_color";
    public static final String DATABASE_COLUMN_NAME_TITLE = "wg_planer_title";
    public static final String DATABASE_COLUMN_NAME_FIRST_NAME = "wg_planer_first_name";
    public static final String DATABASE_COLUMN_NAME_LAST_NAME = "wg_planer_last_name";
    public static final String DATABASE_COLUMN_NAME_URL = "wg_planer_url";
    public static final String DATABASE_COLUMN_NAME_IMG_URL = "wg_planer_img_url";
    public static final String DATABASE_COLUMN_NAME_TEACHER = "wg_planer_teacher";
    public static final String DATABASE_COLUMN_NAME_USERNAME = "wg_planer_username";
    public static final String DATABASE_COLUMN_NAME_NICKNAME = "wg_planer_nickname";
    public static final String DATABASE_COLUMN_NAME_SCHOOL_CLASSES = "wg_planer_school_classes";
    public static final String DATABASE_COLUMN_NAME_EMAIL = "wg_planer_email";
    public static final String DATABASE_COLUMN_NAME_DATE = "wg_planer_date";
    public static final String DATABASE_COLUMN_NAME_ROOM = "wg_planer_room";


    /**
     * JSON
     */
    public static final String JSON_KEY_USERNAME = "username";
    public static final String JSON_KEY_FULL_NAME = "fullName";
    public static final String JSON_KEY_EMAIL = "email";

    /**
     * Shared preferences
     */
    public static final String PREFERENCES_LOGIN = PREFIX + ".LoginPreferences";
    public static final String PREFERENCES_COOKIE = PREFIX + ".CookiePrefsFile";
    public static final String PREFERENCES_KEY_HAS_SET_DEFAULT = PREFIX + ".HAS_SET_DEFAULT";

    /**
     * Notification IDs
     */
    private static final int NOTIFICATION_ID = 655800; /* Random number */
    public static final int REPRESENTATIONS_NOTIFICATION_ID = NOTIFICATION_ID + 1;
    public static final int TIMETABLE_NOTIFICATION_ID = NOTIFICATION_ID + 2;
    public static final int GEOFENCE_NOTIFICATION_ID = NOTIFICATION_ID + 3;

    /**
     * Pending intent IDs
     */
    private static final int PENDING_INTENT_ID = 346700; /* Random number */
    public static final int REPRESENTATIONS_NOTIFICATION_PENDING_INTENT_ID = PENDING_INTENT_ID + 1;
    public static final int TIMETABLE_NOTIFICATION_PENDING_INTENT_ID = PENDING_INTENT_ID + 2;
    public static final int GEOFENCE_NOTIFICATION_PENDING_INTENT_ID = PENDING_INTENT_ID + 3;

    /**
     * Geofence request codes
     */
    private static final int GEOFENCE_REQUEST_CODE = 781400;
    public static final int WG_GEOFENCE_REQUEST_CODE = GEOFENCE_REQUEST_CODE + 1;

    /**
     * Content authorities
     */
    private static final String CONTENT_AUTHORITY = PREFIX + ".content";
    public static final String CONTENT_AUTHORITY_REPRESENTATIONS = CONTENT_AUTHORITY + ".representations";
    public static final String CONTENT_AUTHORITY_TEACHERS = CONTENT_AUTHORITY + ".teachers";
    public static final String CONTENT_AUTHORITY_TIMETABLE = CONTENT_AUTHORITY + ".lessons";
    public static final String CONTENT_AUTHORITY_USERS = CONTENT_AUTHORITY + ".users";
}
