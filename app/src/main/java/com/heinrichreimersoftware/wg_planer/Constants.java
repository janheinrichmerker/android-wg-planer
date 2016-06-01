package com.heinrichreimersoftware.wg_planer;

import android.graphics.Color;

public class Constants {
    /**
     * Times
     */
    public static final long FIVE_MINUTES = 5 * 60 * 1000;
    public static final long TEN_MINUTES = 10 * 60 * 1000;
    public static final long FIFTEEN_MINUTES = 15 * 60 * 1000;

    /** Notification light settings for representation notification */
    public static final int NOTIFICATION_LIGHTS_COLOR = Color.GREEN;
    public static final int NOTIFICATION_LIGHTS_ON_MS = 1500;
    public static final int NOTIFICATION_LIGHTS_OFF_MS = 2500;

    /** Permissions */
    public static final String ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";

    /** Files */
    public static final String PROFILE_IMAGE = "profile.png";

    /**
     * URLs
     */
    public static final String API_URL = "http://heinrichreimersoftware.com/api/wgplaner/";
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_TABLE_NAME_LESSONS = "lessons";
    public static final String DATABASE_TABLE_NAME_REPRESENTATIONS = "representations";
    public static final String DATABASE_TABLE_NAME_FROM_TOS = "from_tos";
    public static final String DATABASE_TABLE_NAME_SUBJECTS = "subjects";
    public static final String DATABASE_TABLE_NAME_TEACHER_SUBJECTS = "teacher_subjects";
    public static final String DATABASE_TABLE_NAME_TEACHERS = "teachers";
    public static final String DATABASE_TABLE_NAME_USERS = "users";
    public static final String DATABASE_COLUMN_NAME_ID = "_id";
    public static final String DATABASE_COLUMN_NAME_DAY = "day";
    public static final String DATABASE_COLUMN_NAME_FIRST_LESSON_NUMBER = "first_lesson_number";
    public static final String DATABASE_COLUMN_NAME_LAST_LESSON_NUMBER = "last_lesson_number";
    public static final String DATABASE_COLUMN_NAME_SUBJECTS = "subjects";
    public static final String DATABASE_COLUMN_NAME_SCHOOL_CLASS = "school_class";
    public static final String DATABASE_COLUMN_NAME_SUBJECT = "subject";
    public static final String DATABASE_COLUMN_NAME_FROM = "from";
    public static final String DATABASE_COLUMN_NAME_TO = "to";
    public static final String DATABASE_COLUMN_NAME_DESCRIPTION = "description";
    public static final String DATABASE_COLUMN_NAME_SHORTHAND = "shorthand";
    public static final String DATABASE_COLUMN_NAME_FULL_NAME = "full_name";
    public static final String DATABASE_COLUMN_NAME_COLOR = "color";
    public static final String DATABASE_COLUMN_NAME_TITLE = "title";
    public static final String DATABASE_COLUMN_NAME_FIRST_NAME = "first_name";
    public static final String DATABASE_COLUMN_NAME_LAST_NAME = "last_name";
    public static final String DATABASE_COLUMN_NAME_URL = "url";
    public static final String DATABASE_COLUMN_NAME_IMG_URL = "img_url";
    public static final String DATABASE_COLUMN_NAME_TEACHER = "teacher";
    public static final String DATABASE_COLUMN_NAME_USERNAME = "username";
    public static final String DATABASE_COLUMN_NAME_NICKNAME = "nickname";
    public static final String DATABASE_COLUMN_NAME_SCHOOL_CLASSES = "school_classes";
    public static final String DATABASE_COLUMN_NAME_EMAIL = "email";
    public static final String DATABASE_COLUMN_NAME_DATE = "date";
    public static final String DATABASE_COLUMN_NAME_ROOM = "room";
    /** Prefixes */
    private static final String PREFIX = "com.heinrichreimersoftware.wg_planer";
    /**
     * Shared preferences
     */
    public static final String PREFERENCES_LOGIN = PREFIX + ".LoginPreferences";
    public static final String PREFERENCES_COOKIE = PREFIX + ".CookiePrefsFile";
    public static final String PREFERENCES_KEY_HAS_SET_DEFAULT = PREFIX + ".HAS_SET_DEFAULT";
    /**
     * Database
     */
    public static final String DATABASE_NAME = PREFIX + ".db";
    /** Notification IDs */
    private static final int NOTIFICATION_ID = 655800; /* Random number */
    public static final int REPRESENTATIONS_NOTIFICATION_ID = NOTIFICATION_ID + 1;
    public static final int TIMETABLE_NOTIFICATION_ID = NOTIFICATION_ID + 2;
    public static final int GEOFENCE_NOTIFICATION_ID = NOTIFICATION_ID + 3;
    /** Pending intent IDs */
    private static final int PENDING_INTENT_ID = 346700; /* Random number */
    public static final int REPRESENTATIONS_NOTIFICATION_PENDING_INTENT_ID = PENDING_INTENT_ID + 1;
    public static final int TIMETABLE_NOTIFICATION_PENDING_INTENT_ID = PENDING_INTENT_ID + 2;
    public static final int GEOFENCE_NOTIFICATION_PENDING_INTENT_ID = PENDING_INTENT_ID + 3;
    /** Geofence request codes */
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
