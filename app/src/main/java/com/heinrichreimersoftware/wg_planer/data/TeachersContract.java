package com.heinrichreimersoftware.wg_planer.data;

import android.net.Uri;

public class TeachersContract {
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.heinrichreimersoftware.teacher";
    public static final String CONTENT_TYPE_DIR = "vnd.android.cursor.dir/vnd.heinrichreimersoftware.teacher";

    public static final String AUTHORITY = "com.heinrichreimersoftware.teachers.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/teachers");
}