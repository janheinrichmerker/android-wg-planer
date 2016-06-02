package com.heinrichreimersoftware.wg_planer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.heinrichreimersoftware.wg_planer.R;
import com.heinrichreimersoftware.wg_planer.structure.Lesson;
import com.heinrichreimersoftware.wg_planer.structure.Representation;
import com.heinrichreimersoftware.wg_planer.structure.TeacherSubject;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClassesUtils {

    @NonNull
    public static Lesson[] filterLessons(@NonNull Context context, @NonNull Lesson[] lessons) {
        List<Lesson> filteredLessons = filterLessons(context, Arrays.asList(lessons));
        return filteredLessons.toArray(new Lesson[filteredLessons.size()]);
    }

    @NonNull
    public static Lesson[] filterLessons(@NonNull Context context, @NonNull Lesson[] lessons, @Nullable Calendar date) {
        List<Lesson> filteredLessons = filterLessons(context, Arrays.asList(lessons), date);
        return filteredLessons.toArray(new Lesson[filteredLessons.size()]);
    }

    public static List<Lesson> filterLessons(@NonNull Context context, @NonNull List<Lesson> lessons) {
        return filterLessons(context, lessons, null);
    }

    public static List<Lesson> filterLessons(@NonNull Context context, @NonNull List<Lesson> lessons, @Nullable Calendar date) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        if (!sharedPreferences.getBoolean(context.getString(R.string.key_preference_classes_general), false))
            return lessons;

        Set<String> classes = sharedPreferences.getStringSet(context.getString(R.string.key_preference_classes_list), new HashSet<String>());

        if (classes.isEmpty())
            return lessons;

        for (Lesson lesson : lessons) {
            List<TeacherSubject> subjects = Arrays.asList(lesson.getSubjects());

            for (TeacherSubject subject : subjects) {
                if (!classes.contains(subject.getShorthand())) {
                    subjects.remove(subject);
                }
            }

            if (subjects.isEmpty()) {
                lessons.remove(lesson);
            } else {
                lesson.setSubjects(subjects.toArray(new TeacherSubject[subjects.size()]));
            }
        }
        return lessons;
    }

    @NonNull
    public static Representation[] filterRepresentations(@NonNull Context context, @NonNull Representation[] representations) {
        List<Representation> filteredRepresentations = filterRepresentations(context, Arrays.asList(representations));
        return filteredRepresentations.toArray(new Representation[filteredRepresentations.size()]);
    }

    @NonNull
    public static Representation[] filterRepresentations(@NonNull Context context, @NonNull Representation[] representations, @Nullable Calendar date) {
        List<Representation> filteredRepresentations = filterRepresentations(context, Arrays.asList(representations), date);
        return filteredRepresentations.toArray(new Representation[filteredRepresentations.size()]);
    }

    public static List<Representation> filterRepresentations(@NonNull Context context, @NonNull List<Representation> representations) {
        return filterRepresentations(context, representations, null);
    }

    public static List<Representation> filterRepresentations(@NonNull Context context, @NonNull List<Representation> representations, @Nullable Calendar date) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        if (!sharedPreferences.getBoolean(context.getString(R.string.key_preference_classes_general), false))
            return filterRepresentations(representations, date);

        Set<String> classes = sharedPreferences.getStringSet(context.getString(R.string.key_preference_classes_list), new HashSet<String>());

        if (classes.isEmpty())
            return filterRepresentations(representations, date);

        for (Representation representation : representations) {
            if (!classes.contains(representation.getSubject().getShorthand())) {
                representations.remove(representation);
            }
        }

        return filterRepresentations(representations, date);
    }

    public static List<Representation> filterRepresentations(@NonNull List<Representation> representations, @Nullable Calendar date) {
        if (date == null)
            return representations;

        for (Representation representation : representations) {
            if (representation.getFrom().getDate().getTimeInMillis() != date.getTimeInMillis() &&
                    representation.getTo().getDate().getTimeInMillis() != date.getTimeInMillis()) {
                representations.remove(representation);
            }
        }

        return representations;
    }
}
