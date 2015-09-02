package com.heinrichreimersoftware.wg_planer.data;

import android.net.Uri;

public class RepresentationsContract {

    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.heinrichreimersoftware.representation";
    public static final String CONTENT_TYPE_DIR = "vnd.android.cursor.dir/vnd.heinrichreimersoftware.representation";

    public static final String AUTHORITY = "com.heinrichreimersoftware.representations.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/representations");
}