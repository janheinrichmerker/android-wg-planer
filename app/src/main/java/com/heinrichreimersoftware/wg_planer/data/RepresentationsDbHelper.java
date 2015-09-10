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
    public static final String REPRESENTATIONS_COL_SUBJECT = "subject";
    public static final String REPRESENTATIONS_COL_FROM_TEACHER = "fromTeacher";
    public static final String REPRESENTATIONS_COL_FROM_ROOM = "fromRoom";
    public static final String REPRESENTATIONS_COL_FROM = "fromLesson";
    public static final String REPRESENTATIONS_COL_TO_TEACHER = "toTeacher";
    public static final String REPRESENTATIONS_COL_TO_ROOM = "toRoom";
    public static final String REPRESENTATIONS_COL_TO = "toLesson";
    public static final String REPRESENTATIONS_COL_DESCRIPTION = "description";

    public static final String DATABASE_CREATE = "create table " +
            REPRESENTATIONS_TABLE_NAME + "(" +
            REPRESENTATIONS_COL_ID + " integer primary key autoincrement, " +
            REPRESENTATIONS_COL_SCHOOL_CLASS + " text not null, " +
            REPRESENTATIONS_COL_DATE + " integer, " +
            REPRESENTATIONS_COL_FIRST_LESSON_NUMBER + " integer, " +
            REPRESENTATIONS_COL_LAST_LESSON_NUMBER + " integer, " +
            REPRESENTATIONS_COL_SUBJECT + " text not null, " +
            REPRESENTATIONS_COL_FROM_TEACHER + " text not null, " +
            REPRESENTATIONS_COL_FROM_ROOM + " text not null, " +
            REPRESENTATIONS_COL_FROM + " text not null, " +
            REPRESENTATIONS_COL_TO_TEACHER + " text not null, " +
            REPRESENTATIONS_COL_TO_ROOM + " text not null, " +
            REPRESENTATIONS_COL_TO + " text not null, " +
            REPRESENTATIONS_COL_DESCRIPTION + " text not null" +
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