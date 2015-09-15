package com.heinrichreimersoftware.wg_planer.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TimetableDbHelper extends SQLiteOpenHelper {
    public static final String TIMETABLE_TABLE_NAME = "lessons";
    public static final String TIMETABLE_COL_ID = "_id";
    public static final String TIMETABLE_COL_DAY = "day";
    public static final String TIMETABLE_COL_FIRST_LESSON_NUMBER = "firstLessonNumber";
    public static final String TIMETABLE_COL_LAST_LESSON_NUMBER = "lastLessonNumber";
    public static final String TIMETABLE_COL_SUBJECTS = "subjects";

    public static final String DATABASE_CREATE = "create table " +
            TIMETABLE_TABLE_NAME + "(" +
            TIMETABLE_COL_ID + " integer primary key autoincrement, " +
            TIMETABLE_COL_DAY + " integer, " +
            TIMETABLE_COL_FIRST_LESSON_NUMBER + " integer, " +
            TIMETABLE_COL_LAST_LESSON_NUMBER + " integer, " +
            TIMETABLE_COL_SUBJECTS + " text not null" +
            ");";

    public TimetableDbHelper(Context context) {
        super(context, DbHelper.DATABASE_NAME, null, DbHelper.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
        Log.d("WG-Planer", "Creating database table");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("WG-Planer", "Update from" + oldVersion + " to " + newVersion + ": " +
                "This wipes all old data!");
        db.execSQL("DROP TABLE IF EXISTS " + TIMETABLE_TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TIMETABLE_TABLE_NAME);
        onCreate(db);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean tableExists(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type = 'table' AND name = '" +
                TIMETABLE_TABLE_NAME + "'", null);
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