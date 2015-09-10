package com.heinrichreimersoftware.wg_planer.data;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.heinrichreimersoftware.wg_planer.structure.Representation;
import com.heinrichreimersoftware.wg_planer.utils.CalendarUtils;
import com.heinrichreimersoftware.wg_planer.utils.ClassesUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RepresentationsContentHelper {
    public static List<Representation> getRepresentations(@NonNull Context context) {
        List<Representation> representations = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(RepresentationsContract.CONTENT_URI, null, null, null, RepresentationsDbHelper.REPRESENTATIONS_COL_FIRST_LESSON_NUMBER);

        if (cursor.moveToFirst()) {
            do {
                Representation representation = Representation.fromCursor(cursor);
                if (ClassesUtils.shouldShow(context, representation.getSubject())) {
                    representations.add(representation);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return representations;
    }

    public static List<Representation> getRepresentations(@NonNull Context context, @NonNull Calendar date) {
        List<Representation> representations = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(RepresentationsContract.CONTENT_URI, null, RepresentationsDbHelper.REPRESENTATIONS_COL_DATE + " = '" + date.getTimeInMillis() + "'", null, RepresentationsDbHelper.REPRESENTATIONS_COL_FIRST_LESSON_NUMBER);

        if (cursor.moveToFirst()) {
            do {
                Representation representation = Representation.fromCursor(cursor);
                if (ClassesUtils.shouldShow(context, representation.getSubject())) {
                    representations.add(representation);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return representations;
    }

    public static List<Representation> getRepresentationsToday(@NonNull Context context) {
        return getRepresentations(context, CalendarUtils.today());
    }

    public static List<Representation> getRepresentationsTomorrow(@NonNull Context context) {
        return getRepresentations(context, CalendarUtils.tomorrow());
    }

    public static List<Representation> getRepresentationsNextMonday(@NonNull Context context) {
        return getRepresentations(context, CalendarUtils.nextMonday());
    }

    public static List<Representation> getRepresentations(@NonNull Context context, @NonNull String schoolClasses) {
        List<Representation> representations = new ArrayList<>();

        String selectionSchoolClass = "";
        String[] schoolClassesArray = schoolClasses.split(", *");
        boolean first = true;
        for (String schoolClass : schoolClassesArray) {
            if (first) {
                selectionSchoolClass += RepresentationsDbHelper.REPRESENTATIONS_COL_SCHOOL_CLASS + " LIKE '" + schoolClass + "%'";
                first = false;
            } else {
                selectionSchoolClass += " OR " + RepresentationsDbHelper.REPRESENTATIONS_COL_SCHOOL_CLASS + " LIKE '" + schoolClass + "%'";
            }
        }

        Cursor cursor = context.getContentResolver().query(RepresentationsContract.CONTENT_URI, null, selectionSchoolClass, null, RepresentationsDbHelper.REPRESENTATIONS_COL_FIRST_LESSON_NUMBER);

        if (cursor.moveToFirst()) {
            do {
                Representation representation = Representation.fromCursor(cursor);
                if (ClassesUtils.shouldShow(context, representation.getSubject())) {
                    representations.add(representation);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return representations;
    }

    public static List<Representation> getRepresentations(@NonNull Context context, Calendar date, @NonNull String schoolClasses) {
        List<Representation> representations = new ArrayList<>();

        String selectionSchoolClass = "";
        String[] schoolClassesArray = schoolClasses.split(", *");
        boolean first = true;
        for (String schoolClass : schoolClassesArray) {
            if (first) {
                selectionSchoolClass += RepresentationsDbHelper.REPRESENTATIONS_COL_SCHOOL_CLASS + " LIKE '" + schoolClass + "%'";
                first = false;
            } else {
                selectionSchoolClass += " OR " + RepresentationsDbHelper.REPRESENTATIONS_COL_SCHOOL_CLASS + " LIKE '" + schoolClass + "%'";
            }
        }

        Cursor cursor = context.getContentResolver().query(RepresentationsContract.CONTENT_URI, null, selectionSchoolClass + " AND " + RepresentationsDbHelper.REPRESENTATIONS_COL_DATE + " = '" + date.getTimeInMillis() + "'", null, RepresentationsDbHelper.REPRESENTATIONS_COL_FIRST_LESSON_NUMBER);

        if (cursor.moveToFirst()) {
            do {
                Representation representation = Representation.fromCursor(cursor);
                if (ClassesUtils.shouldShow(context, representation.getSubject())) {
                    representations.add(representation);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return representations;
    }

    public static List<Representation> getRepresentationsToday(@NonNull Context context, @NonNull String schoolClasses) {
        return getRepresentations(context, CalendarUtils.today(), schoolClasses);
    }

    public static List<Representation> getRepresentationsTomorrow(@NonNull Context context, @NonNull String schoolClasses) {
        return getRepresentations(context, CalendarUtils.tomorrow(), schoolClasses);
    }

    public static List<Representation> getRepresentationsNextMonday(@NonNull Context context, @NonNull String schoolClasses) {
        return getRepresentations(context, CalendarUtils.nextMonday(), schoolClasses);
    }

    public static List<Representation> getRepresentationsFuture(@NonNull Context context) {
        List<Representation> representations = new ArrayList<>();
        representations.addAll(getRepresentationsToday(context));
        representations.addAll(getRepresentationsTomorrow(context));

        if (CalendarUtils.isFridayOrSaturday()) {
            representations.addAll(getRepresentationsNextMonday(context));
        }
        return representations;
    }

    public static List<Representation> getRepresentationsFuture(@NonNull Context context, @NonNull String schoolClasses) {
        List<Representation> representations = new ArrayList<>();
        representations.addAll(getRepresentationsToday(context, schoolClasses));
        representations.addAll(getRepresentationsTomorrow(context, schoolClasses));

        if (CalendarUtils.isFridayOrSaturday()) {
            representations.addAll(getRepresentationsNextMonday(context, schoolClasses));
        }
        return representations;
    }

    public static int clearRepresentations(@NonNull Context context) {
        return context.getContentResolver().delete(RepresentationsContract.CONTENT_URI, null, null);
    }
}
