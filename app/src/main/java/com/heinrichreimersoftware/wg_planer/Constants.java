package com.heinrichreimersoftware.wg_planer;

import android.graphics.Color;

public class Constants {
    /* Times */
    public static final long FIVE_MINUTES = 5 * 60 * 1000;
    public static final long TEN_MINUTES = 10 * 60 * 1000;
    public static final long FIFTEEN_MINUTES = 15 * 60 * 1000;
    /* Notification light settings for representation notification */
    public static final int NOTIFICATION_LIGHTS_COLOR = Color.GREEN;
    public static final int NOTIFICATION_LIGHTS_ON_MS = 1500;
    public static final int NOTIFICATION_LIGHTS_OFF_MS = 2500;
    /* Permissions */
    public static final String ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";
    /* Files */
    public static final String PROFILE_IMAGE = "profile.png";
    /* URLs */
    public static final String IDESK_URL = "https://wilhelm-gym.net/idesk/";
    public static final String INFO_PANEL_URL = IDESK_URL + "infodisplay/mods/link.local.php/";
    public static final String USER_INFO_URL = IDESK_URL + "addr/my.php/";
    public static final String REPRESENTATIONS_URL_TODAY = INFO_PANEL_URL + "panelId=28/Vtr/Internet2/Klassen/f1/subst_001.htm";
    public static final String REPRESENTATIONS_URL_TOMORROW = INFO_PANEL_URL + "panelId=32/Vtr/Internet2/Klassen/f2/subst_001.htm";
    public static final String TIMETABLE_BASE_URL = INFO_PANEL_URL + "panelId=40/Stundenplan/Klassen/";
    public static final String TIMETABLE_NAV_URL = TIMETABLE_BASE_URL + "frames/navbar.htm";
    /* Prefixes */
    private static final String PREFIX = "com.heinrichreimersoftware.wg_planer";
    /* Shared preferences */
    public static final String PREFERENCES_LOGIN = PREFIX + "LoginPreferences";
    public static final String PREFERENCES_COOKIE = PREFIX + "CookiePrefsFile";
    public static final String PREFERENCES_KEY_HAS_SET_DEFAULT = PREFIX + "HAS_SET_DEFAULT";
    /* Notification IDs */
    private static final int NOTIFICATION_ID = 655800; /* Random number */
    public static final int REPRESENTATIONS_NOTIFICATION_ID = NOTIFICATION_ID + 1;
    public static final int TIMETABLE_NOTIFICATION_ID = NOTIFICATION_ID + 2;
    public static final int GEOFENCE_NOTIFICATION_ID = NOTIFICATION_ID + 3;
    /* Pending intent IDs */
    private static final int PENDING_INTENT_ID = 346700; /* Random number */
    public static final int REPRESENTATIONS_NOTIFICATION_PENDING_INTENT_ID = PENDING_INTENT_ID + 1;
    public static final int TIMETABLE_NOTIFICATION_PENDING_INTENT_ID = PENDING_INTENT_ID + 2;
    public static final int GEOFENCE_NOTIFICATION_PENDING_INTENT_ID = PENDING_INTENT_ID + 3;
    /* Geofence request codes */
    private static final int GEOFENCE_REQUEST_CODE = 781400;
    public static final int WG_GEOFENCE_REQUEST_CODE = GEOFENCE_REQUEST_CODE + 1;
}
