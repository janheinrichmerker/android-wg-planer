package com.heinrichreimersoftware.wg_planer.content;

import android.content.Context;
import android.support.annotation.NonNull;

import com.heinrichreimer.inquiry.Inquiry;
import com.heinrichreimersoftware.wg_planer.Constants;
import com.heinrichreimersoftware.wg_planer.structure.Lesson;
import com.heinrichreimersoftware.wg_planer.utils.ClassesUtils;

public class TimetableContentHelper {
    public static Lesson[] getTimetable(@NonNull Context context) {
        Inquiry.init(context, Constants.DATABASE_NAME, Constants.DATABASE_VERSION);

        Lesson[] lessons = Inquiry.get()
                .select(Lesson.class)
                .sort(Constants.DATABASE_COLUMN_NAME_FIRST_LESSON_NUMBER)
                .all();

        Inquiry.deinit();

        return ClassesUtils.filterLessons(context, lessons);
    }

    public static Lesson[] getTimetable(@NonNull Context context, int day) {
        Inquiry.init(context, Constants.DATABASE_NAME, Constants.DATABASE_VERSION);

        Lesson[] lessons = Inquiry.get()
                .select(Lesson.class)
                .where(Constants.DATABASE_COLUMN_NAME_DAY + " = ?", day)
                .sort(Constants.DATABASE_COLUMN_NAME_FIRST_LESSON_NUMBER)
                .all();

        Inquiry.deinit();

        return ClassesUtils.filterLessons(context, lessons);
    }

    public static void addLesson(@NonNull Context context, Lesson lesson) {
        Inquiry.init(context, Constants.DATABASE_NAME, Constants.DATABASE_VERSION);

        Inquiry.get()
                .insert(Lesson.class)
                .values(lesson)
                .run();

        Inquiry.deinit();
    }

    public static void addLessons(@NonNull Context context, Lesson... lessons) {
        Inquiry.init(context, Constants.DATABASE_NAME, Constants.DATABASE_VERSION);

        Inquiry.get()
                .insert(Lesson.class)
                .values(lessons)
                .run();

        Inquiry.deinit();
    }

    public static void clearTimetable(@NonNull Context context) {
        Inquiry.init(context, Constants.DATABASE_NAME, Constants.DATABASE_VERSION);

        Inquiry.get()
                .delete(Lesson.class)
                .run();

        Inquiry.deinit();
    }
}
