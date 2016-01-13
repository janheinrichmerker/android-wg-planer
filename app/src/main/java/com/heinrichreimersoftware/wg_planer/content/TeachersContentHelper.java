package com.heinrichreimersoftware.wg_planer.content;

import android.content.Context;
import android.support.annotation.NonNull;

import com.afollestad.inquiry.Inquiry;
import com.heinrichreimersoftware.wg_planer.Constants;
import com.heinrichreimersoftware.wg_planer.structure.Teacher;

public class TeachersContentHelper {
    public static Teacher[] getTeachers(@NonNull Context context) {
        Inquiry.init(context, Constants.DATABASE_NAME, Constants.DATABASE_VERSION);

        Teacher[] teachers = Inquiry.get()
                .deleteFrom(Constants.DATABASE_TABLE_NAME_TEACHERS, Teacher.class)
                .sort(Constants.DATABASE_COLUMN_NAME_SHORTHAND)
                .all();

        Inquiry.deinit();

        return teachers;
    }

    public static Teacher getTeacher(@NonNull Context context, @NonNull String shorthand) {
        Inquiry.init(context, Constants.DATABASE_NAME, Constants.DATABASE_VERSION);

        Teacher teacher = Inquiry.get()
                .deleteFrom(Constants.DATABASE_TABLE_NAME_TEACHERS, Teacher.class)
                .where(Constants.DATABASE_COLUMN_NAME_SHORTHAND + " = ?", shorthand)
                .sort(Constants.DATABASE_COLUMN_NAME_SHORTHAND)
                .one();

        Inquiry.deinit();

        return teacher;
    }

    public static void addTeacher(@NonNull Context context, Teacher teacher) {
        Inquiry.init(context, Constants.DATABASE_NAME, Constants.DATABASE_VERSION);

        Inquiry.get()
                .insertInto(Constants.DATABASE_TABLE_NAME_TEACHERS, Teacher.class)
                .values(teacher)
                .run();

        Inquiry.deinit();
    }

    public static void addTeachers(@NonNull Context context, Teacher... teachers) {
        Inquiry.init(context, Constants.DATABASE_NAME, Constants.DATABASE_VERSION);

        Inquiry.get()
                .insertInto(Constants.DATABASE_TABLE_NAME_TEACHERS, Teacher.class)
                .values(teachers)
                .run();

        Inquiry.deinit();
    }

    public static void clearTeachers(@NonNull Context context) {
        Inquiry.init(context, Constants.DATABASE_NAME, Constants.DATABASE_VERSION);

        Inquiry.get()
                .deleteFrom(Constants.DATABASE_TABLE_NAME_TEACHERS, Teacher.class)
                .run();

        Inquiry.deinit();
    }
}
