package com.heinrichreimersoftware.wg_planer.data;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.heinrichreimersoftware.wg_planer.structure.Teacher;

import java.util.ArrayList;
import java.util.List;

public class TeachersContentHelper {
    public static List<Teacher> getTeachers(@NonNull Context context) {
        List<Teacher> teachers = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(TeachersContract.CONTENT_URI, null, null, null, TeachersDbHelper.TEACHERS_COL_SHORTHAND);
        if (cursor.moveToFirst()) {
            do {
                Teacher teacher = Teacher.fromCursor(cursor);
                teachers.add(teacher);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return teachers;
    }

    public static Teacher getTeacher(@NonNull Context context, @NonNull String shorthand) {
        Cursor cursor = context.getContentResolver().query(TeachersContract.CONTENT_URI, null, TeachersDbHelper.TEACHERS_COL_SHORTHAND + " = '" + shorthand + "'", null, TeachersDbHelper.TEACHERS_COL_SHORTHAND);
        if (cursor.moveToFirst()) {
            do {
                Teacher teacher = Teacher.fromCursor(cursor);
                if (teacher != null) {
                    cursor.close();
                    return teacher;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return null;
    }

    public static int clearTeachers(@NonNull Context context) {
        return context.getContentResolver().delete(TeachersContract.CONTENT_URI, null, null);
    }
}
