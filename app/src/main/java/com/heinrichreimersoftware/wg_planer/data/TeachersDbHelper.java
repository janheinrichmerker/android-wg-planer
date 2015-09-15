package com.heinrichreimersoftware.wg_planer.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TeachersDbHelper extends SQLiteOpenHelper {
    public static final String TEACHERS_TABLE_NAME = "teachers";
    public static final String TEACHERS_COL_ID = "_id";
    public static final String TEACHERS_COL_SHORTHAND = "shorthand";
    public static final String TEACHERS_COL_FIRST_NAME = "firstName";
    public static final String TEACHERS_COL_LAST_NAME = "lastName";
    public static final String TEACHERS_COL_WEB_LINK = "webLink";

    public static final String DATABASE_CREATE = "create table " +
            TEACHERS_TABLE_NAME + "(" +
            TEACHERS_COL_ID + " integer primary key autoincrement, " +
            TEACHERS_COL_SHORTHAND + " text not null unique, " +
            TEACHERS_COL_FIRST_NAME + " text not null, " +
            TEACHERS_COL_LAST_NAME + " text not null, " +
            TEACHERS_COL_WEB_LINK + " text not null" +
            ");";

    public TeachersDbHelper(Context context) {
        super(context, DbHelper.DATABASE_NAME, null, DbHelper.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TEACHERS_TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TEACHERS_TABLE_NAME);
        onCreate(db);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean tableExists(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type = 'table' AND name = '" +
                TEACHERS_TABLE_NAME + "'", null);
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