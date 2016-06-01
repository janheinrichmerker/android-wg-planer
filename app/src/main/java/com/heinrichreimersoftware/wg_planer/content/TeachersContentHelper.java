package com.heinrichreimersoftware.wg_planer.content;

import android.content.Context;
import android.support.annotation.NonNull;

import com.heinrichreimer.inquiry.Inquiry;
import com.heinrichreimersoftware.wg_planer.Constants;
import com.heinrichreimersoftware.wg_planer.structure.Teacher;

public class TeachersContentHelper {
    public static Teacher[] getTeachers(@NonNull Context context) {
        Inquiry.init(context, Constants.DATABASE_NAME, Constants.DATABASE_VERSION);

        Teacher[] teachers = Inquiry.get()
                .delete(Teacher.class)
                .sort(Constants.DATABASE_COLUMN_NAME_SHORTHAND)
                .all();

        Inquiry.deinit();

        return teachers;
    }

    public static Teacher getTeacher(@NonNull Context context, @NonNull String shorthand) {
        Inquiry.init(context, Constants.DATABASE_NAME, Constants.DATABASE_VERSION);

        Teacher teacher = Inquiry.get()
                .delete(Teacher.class)
                .where(Constants.DATABASE_COLUMN_NAME_SHORTHAND + " = ?", shorthand)
                .sort(Constants.DATABASE_COLUMN_NAME_SHORTHAND)
                .one();

        Inquiry.deinit();

        return teacher;
    }

    public static void addTeacher(@NonNull Context context, Teacher teacher) {
        Inquiry.init(context, Constants.DATABASE_NAME, Constants.DATABASE_VERSION);

        Inquiry.get()
                .insert(Teacher.class)
                .values(teacher)
                .run();

        Inquiry.deinit();
    }

    public static void addTeachers(@NonNull Context context, Teacher... teachers) {
        Inquiry.init(context, Constants.DATABASE_NAME, Constants.DATABASE_VERSION);

        Inquiry.get()
                .insert(Teacher.class)
                .values(teachers)
                .run();

        Inquiry.deinit();
    }

    public static void clearTeachers(@NonNull Context context) {
        Inquiry.init(context, Constants.DATABASE_NAME, Constants.DATABASE_VERSION);

        Inquiry.get()
                .delete(Teacher.class)
                .run();

        Inquiry.deinit();
    }
}
