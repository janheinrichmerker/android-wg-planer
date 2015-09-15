package com.heinrichreimersoftware.wg_planer.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDbHelper extends SQLiteOpenHelper {
    public static final String USER_TABLE_NAME = "users";
    public static final String USER_COL_ID = "_id";
    public static final String USER_COL_USERNAME = "username";
    public static final String USER_COL_PASSWORD = "password";
    public static final String USER_COL_IMAGE_URL = "imageUrl";
    public static final String USER_COL_TITLE = "title";
    public static final String USER_COL_FIRST_NAME = "firstName";
    public static final String USER_COL_LAST_NAME = "lastName";
    public static final String USER_COL_COMPANY = "company";
    public static final String USER_COL_BIRTHDAY = "birthday";
    public static final String USER_COL_NICKNAME = "nickname";
    public static final String USER_COL_SCHOOL_CLASS = "schoolClass";
    public static final String USER_COL_STREET = "street";
    public static final String USER_COL_ZIP_CODE = "zipCode";
    public static final String USER_COL_CITY = "city";
    public static final String USER_COL_COUNTRY = "country";
    public static final String USER_COL_PHONE = "phone";
    public static final String USER_COL_MOBILE_PHONE = "mobilePhone";
    public static final String USER_COL_FAX = "fax";
    public static final String USER_COL_MAIL = "mail";
    public static final String USER_COL_HOMEPAGE = "homepage";
    public static final String USER_COL_ICQ = "icq";
    public static final String USER_COL_JABBER = "jabber";
    public static final String USER_COL_MSN = "msn";
    public static final String USER_COL_SKYPE = "skype";
    public static final String USER_COL_OID = "oid";
    public static final String USER_COL_AUTH_TOKEN = "authToken";
    public static final String USER_COL_LOCAL_IMAGE_FILE_NAME = "localImageFileName";

    public static final String DATABASE_CREATE = "create table " +
            USER_TABLE_NAME + "(" +
            USER_COL_ID + " integer primary key autoincrement, " +
            USER_COL_USERNAME + " text not null, " +
            USER_COL_PASSWORD + " text not null, " +
            USER_COL_IMAGE_URL + " text, " +
            USER_COL_TITLE + " text, " +
            USER_COL_FIRST_NAME + " text, " +
            USER_COL_LAST_NAME + " text, " +
            USER_COL_COMPANY + " text, " +
            USER_COL_BIRTHDAY + " text, " +
            USER_COL_NICKNAME + " text, " +
            USER_COL_SCHOOL_CLASS + " text, " +
            USER_COL_STREET + " text, " +
            USER_COL_ZIP_CODE + " text, " +
            USER_COL_CITY + " text, " +
            USER_COL_COUNTRY + " text, " +
            USER_COL_PHONE + " text, " +
            USER_COL_MOBILE_PHONE + " text, " +
            USER_COL_FAX + " text, " +
            USER_COL_MAIL + " text, " +
            USER_COL_HOMEPAGE + " text, " +
            USER_COL_ICQ + " text, " +
            USER_COL_JABBER + " text, " +
            USER_COL_MSN + " text, " +
            USER_COL_SKYPE + " text, " +
            USER_COL_OID + " text not null, " +
            USER_COL_AUTH_TOKEN + " text not null, " +
            USER_COL_LOCAL_IMAGE_FILE_NAME + " text" +
            ");";

    public UserDbHelper(Context context) {
        super(context, DbHelper.DATABASE_NAME, null, DbHelper.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        onCreate(db);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean tableExists(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type = 'table' AND name = '" + USER_TABLE_NAME + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }
}