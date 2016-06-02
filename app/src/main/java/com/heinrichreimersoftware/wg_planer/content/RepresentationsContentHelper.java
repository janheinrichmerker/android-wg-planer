package com.heinrichreimersoftware.wg_planer.content;

import android.content.Context;
import android.support.annotation.NonNull;

import com.heinrichreimer.inquiry.Inquiry;
import com.heinrichreimersoftware.wg_planer.Constants;
import com.heinrichreimersoftware.wg_planer.structure.Representation;
import com.heinrichreimersoftware.wg_planer.utils.CalendarUtils;
import com.heinrichreimersoftware.wg_planer.utils.ClassesUtils;

import java.util.Calendar;

public class RepresentationsContentHelper {
    public static Representation[] getRepresentations(@NonNull Context context) {
        Inquiry.init(context, Constants.DATABASE_NAME, Constants.DATABASE_VERSION);

        Representation[] representations = Inquiry.get()
                .select(Representation.class)
                .sort(Constants.DATABASE_COLUMN_NAME_FIRST_LESSON_NUMBER)
                .all();

        Inquiry.deinit();

        return ClassesUtils.filterRepresentations(context, representations);
    }

    public static Representation[] getRepresentations(@NonNull Context context, @NonNull Calendar date) {
        Inquiry.init(context, Constants.DATABASE_NAME, Constants.DATABASE_VERSION);

        Representation[] representations = Inquiry.get()
                .select(Representation.class)
                .all();

        Inquiry.deinit();

        return ClassesUtils.filterRepresentations(context, representations, date);
    }

    public static Representation[] getRepresentationsToday(@NonNull Context context) {
        return getRepresentations(context, CalendarUtils.today());
    }

    public static Representation[] getRepresentationsTomorrow(@NonNull Context context) {
        return getRepresentations(context, CalendarUtils.tomorrow());
    }

    public static Representation[] getRepresentationsNextMonday(@NonNull Context context) {
        return getRepresentations(context, CalendarUtils.nextMonday());
    }

    public static Representation[] getRepresentations(@NonNull Context context, @NonNull String[] schoolClasses) {
        Inquiry.init(context, Constants.DATABASE_NAME, Constants.DATABASE_VERSION);

        Representation[] representations = Inquiry.get()
                .select(Representation.class)
                .whereIn(Constants.DATABASE_COLUMN_NAME_SCHOOL_CLASS, schoolClasses)
                .sort(Constants.DATABASE_COLUMN_NAME_FIRST_LESSON_NUMBER)
                .all();

        Inquiry.deinit();

        return ClassesUtils.filterRepresentations(context, representations);
    }

    public static Representation[] getRepresentations(@NonNull Context context, Calendar date, @NonNull String[] schoolClasses) {
        Inquiry.init(context, Constants.DATABASE_NAME, Constants.DATABASE_VERSION);

        Representation[] representations = Inquiry.get()
                .select(Representation.class)
                .whereIn(Constants.DATABASE_COLUMN_NAME_SCHOOL_CLASS, schoolClasses)
                .sort(Constants.DATABASE_COLUMN_NAME_FIRST_LESSON_NUMBER)
                .all();

        Inquiry.deinit();

        return ClassesUtils.filterRepresentations(context, representations, date);
    }

    public static Representation[] getRepresentationsToday(@NonNull Context context, @NonNull String[] schoolClasses) {
        return getRepresentations(context, CalendarUtils.today(), schoolClasses);
    }

    public static Representation[] getRepresentationsTomorrow(@NonNull Context context, @NonNull String[] schoolClasses) {
        return getRepresentations(context, CalendarUtils.tomorrow(), schoolClasses);
    }

    public static Representation[] getRepresentationsNextMonday(@NonNull Context context, @NonNull String[] schoolClasses) {
        return getRepresentations(context, CalendarUtils.nextMonday(), schoolClasses);
    }

    public static Representation[] getRepresentationsFuture(@NonNull Context context) {
        Representation[] representationsToday = getRepresentationsToday(context);
        Representation[] representationsTomorrow = getRepresentationsTomorrow(context);
        Representation[] representationsNextMonday = new Representation[0];
        if (CalendarUtils.isFridayOrSaturday()) {
            representationsNextMonday = getRepresentationsNextMonday(context);
        }

        Representation[] representations = new Representation[representationsToday.length +
                representationsTomorrow.length + representationsNextMonday.length];

        System.arraycopy(representationsToday, 0, representations, 0, representationsToday.length);
        System.arraycopy(representationsTomorrow, 0, representations, representationsToday.length, representationsTomorrow.length);
        System.arraycopy(representationsNextMonday, 0, representations, representationsToday.length + representationsTomorrow.length, representationsNextMonday.length);

        return representations;
    }

    public static Representation[] getRepresentationsFuture(@NonNull Context context, @NonNull String[] schoolClasses) {
        Representation[] representationsToday = getRepresentationsToday(context, schoolClasses);
        Representation[] representationsTomorrow = getRepresentationsTomorrow(context, schoolClasses);
        Representation[] representationsNextMonday = new Representation[0];
        if (CalendarUtils.isFridayOrSaturday()) {
            representationsNextMonday = getRepresentationsNextMonday(context, schoolClasses);
        }

        Representation[] representations = new Representation[representationsToday.length +
                representationsTomorrow.length + representationsNextMonday.length];

        System.arraycopy(representationsToday, 0, representations, 0, representationsToday.length);
        System.arraycopy(representationsTomorrow, 0, representations, representationsToday.length, representationsTomorrow.length);
        System.arraycopy(representationsNextMonday, 0, representations, representationsToday.length + representationsTomorrow.length, representationsNextMonday.length);

        return representations;
    }

    public static void addRepresentation(@NonNull Context context, Representation representation) {
        Inquiry.init(context, Constants.DATABASE_NAME, Constants.DATABASE_VERSION);

        Inquiry.get()
                .insert(Representation.class)
                .values(representation)
                .run();

        Inquiry.deinit();
    }

    public static void addRepresentations(@NonNull Context context, Representation... representations) {
        Inquiry.init(context, Constants.DATABASE_NAME, Constants.DATABASE_VERSION);

        Inquiry.get()
                .insert(Representation.class)
                .values(representations)
                .run();

        Inquiry.deinit();
    }

    public static void clearRepresentations(@NonNull Context context) {
        Inquiry.init(context, Constants.DATABASE_NAME, Constants.DATABASE_VERSION);

        Inquiry.get()
                .delete(Representation.class)
                .run();

        Inquiry.deinit();
    }
}
