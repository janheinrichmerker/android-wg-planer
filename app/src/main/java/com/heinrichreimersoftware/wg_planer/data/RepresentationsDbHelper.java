package com.heinrichreimersoftware.wg_planer.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RepresentationsDbHelper extends SQLiteOpenHelper {
    public static final String REPRESENTATIONS_TABLE_NAME = "representations";
    public static final String REPRESENTATIONS_COL_ID = "_id";
    public static final String REPRESENTATIONS_COL_SCHOOL_CLASS = "schoolClass";
    public static final String REPRESENTATIONS_COL_DATE = "date";
    public static final String REPRESENTATIONS_COL_FIRST_LESSON_NUMBER = "firstLessonNumber";
    public static final String REPRESENTATIONS_COL_LAST_LESSON_NUMBER = "lastLessonNumber";
    public static final String REPRESENTATIONS_COL_REPRESENTED_TEACHER = "representedTeacher";
    public static final String REPRESENTATIONS_COL_REPRESENTED_SUBJECT = "representedSubject";
    public static final String REPRESENTATIONS_COL_REPRESENTING_TEACHER = "representingTeacher";
    public static final String REPRESENTATIONS_COL_REPRESENTED_ROOM = "representedRoom";
    public static final String REPRESENTATIONS_COL_REPRESENTING_ROOM = "representingRoom";
    public static final String REPRESENTATIONS_COL_REPRESENTED_FROM = "representedFrom";
    public static final String REPRESENTATIONS_COL_REPRESENTED_TO = "representedTo";
    public static final String REPRESENTATIONS_COL_REPRESENTATION_TEXT = "representationText";

    public static final String DATABASE_CREATE = "create table " +
            REPRESENTATIONS_TABLE_NAME + "(" +
            REPRESENTATIONS_COL_ID + " integer primary key autoincrement, " +
            REPRESENTATIONS_COL_SCHOOL_CLASS + " text not null, " +
            REPRESENTATIONS_COL_DATE + " integer, " +
            REPRESENTATIONS_COL_FIRST_LESSON_NUMBER + " integer, " +
            REPRESENTATIONS_COL_LAST_LESSON_NUMBER + " integer, " +
            REPRESENTATIONS_COL_REPRESENTED_TEACHER + " text not null, " +
            REPRESENTATIONS_COL_REPRESENTED_SUBJECT + " text not null, " +
            REPRESENTATIONS_COL_REPRESENTING_TEACHER + " text not null, " +
            REPRESENTATIONS_COL_REPRESENTED_ROOM + " text not null, " +
            REPRESENTATIONS_COL_REPRESENTING_ROOM + " text not null, " +
            REPRESENTATIONS_COL_REPRESENTED_FROM + " text not null, " +
            REPRESENTATIONS_COL_REPRESENTED_TO + " text not null, " +
            REPRESENTATIONS_COL_REPRESENTATION_TEXT + " text not null" +
            ");";

    public RepresentationsDbHelper(Context context) {
        super(context, DbHelper.DATABASE_NAME, null, DbHelper.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + REPRESENTATIONS_TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + REPRESENTATIONS_TABLE_NAME);
        onCreate(db);
    }

    public boolean tableExists(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type = 'table' AND name = '" +
                REPRESENTATIONS_TABLE_NAME + "'", null);
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