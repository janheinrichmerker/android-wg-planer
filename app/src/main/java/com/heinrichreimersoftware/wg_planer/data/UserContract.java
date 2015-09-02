package com.heinrichreimersoftware.wg_planer.data;

import android.net.Uri;

public class UserContract {
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.heinrichreimersoftware.user";
    public static final String CONTENT_TYPE_DIR = "vnd.android.cursor.dir/vnd.heinrichreimersoftware.user";

    public static final String AUTHORITY = "com.heinrichreimersoftware.users.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/users");
}