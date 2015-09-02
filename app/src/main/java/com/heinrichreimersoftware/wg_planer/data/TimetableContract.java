package com.heinrichreimersoftware.wg_planer.data;

import android.net.Uri;

public class TimetableContract {
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.heinrichreimersoftware.lesson";
    public static final String CONTENT_TYPE_DIR = "vnd.android.cursor.dir/vnd.heinrichreimersoftware.lesson";

    public static final String AUTHORITY = "com.heinrichreimersoftware.lessons.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/lessons");
}