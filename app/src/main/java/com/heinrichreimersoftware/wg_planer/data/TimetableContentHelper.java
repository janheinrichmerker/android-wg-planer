package com.heinrichreimersoftware.wg_planer.data;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.heinrichreimersoftware.wg_planer.structure.Lesson;
import com.heinrichreimersoftware.wg_planer.utils.ClassesUtils;

import java.util.ArrayList;
import java.util.List;

public class TimetableContentHelper {
    public static List<Lesson> getTimetable(@NonNull Context context) {
        List<Lesson> lessons = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(
                TimetableContract.CONTENT_URI,
                null,
                null,
                null,
                TimetableDbHelper.TIMETABLE_COL_FIRST_LESSON_NUMBER);

        if (cursor == null) return new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Lesson lesson = Lesson.fromCursor(cursor);
                lesson.setSubjects(ClassesUtils.filterSubjects(context, lesson.getSubjects()));
                if (!lesson.getSubjects().isEmpty()) {
                    lessons.add(lesson);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lessons;
    }

    public static List<Lesson> getTimetable(@NonNull Context context, int day) {
        List<Lesson> lessons = new ArrayList<>();

        if (context.getContentResolver() == null) {
            return new ArrayList<>();
        }

        Cursor cursor = context.getContentResolver().query(
                TimetableContract.CONTENT_URI,
                null,
                TimetableDbHelper.TIMETABLE_COL_DAY + " = '" + day + "'",
                null,
                TimetableDbHelper.TIMETABLE_COL_FIRST_LESSON_NUMBER);

        if (cursor == null) return new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Lesson lesson = Lesson.fromCursor(cursor);
                lesson.setSubjects(ClassesUtils.filterSubjects(context, lesson.getSubjects()));
                if (!lesson.getSubjects().isEmpty()) {
                    lessons.add(lesson);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lessons;
    }

    public static int clearTimetable(@NonNull Context context) {
        return context.getContentResolver().delete(TimetableContract.CONTENT_URI, null, null);
    }
}
