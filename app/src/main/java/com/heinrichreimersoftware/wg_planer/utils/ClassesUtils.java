package com.heinrichreimersoftware.wg_planer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.heinrichreimersoftware.wg_planer.R;
import com.heinrichreimersoftware.wg_planer.structure.Lesson;
import com.heinrichreimersoftware.wg_planer.structure.Representation;
import com.heinrichreimersoftware.wg_planer.structure.TeacherSubject;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class ClassesUtils {

    public static Lesson[] filterLessons(Context context, Lesson[] lessons) {
        if (lessons == null) {
            return null;
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        if (!sharedPreferences.getBoolean(context.getString(R.string.key_preference_classes_general), false))
            return lessons;

        Set<String> classes = sharedPreferences.getStringSet(context.getString(R.string.key_preference_classes_list), new HashSet<String>());

        if (classes.isEmpty())
            return lessons;


        Vector<Lesson> vector = new Vector<>(lessons.length);


        for (Lesson lesson : lessons) {
            List<TeacherSubject> subjects = Arrays.asList(lesson.getSubjects());

            for (TeacherSubject subject : subjects) {
                if (!classes.contains(subject.getShorthand())) {
                    subjects.remove(subject);
                }
            }

            if (!subjects.isEmpty()) {
                lesson.setSubjects(subjects.toArray(new TeacherSubject[subjects.size()]));
                vector.add(lesson);
            }
        }

        Lesson[] filteredLessons = new Lesson[vector.size()];
        vector.copyInto(filteredLessons);
        return filteredLessons;
    }

    public static List<Lesson> filterLessons(Context context, List<Lesson> lessons) {
        if (lessons == null) {
            return null;
        }

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

    public static Representation[] filterRepresentations(Context context, Representation[] representations) {
        if (representations == null)
            return null;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        if (!sharedPreferences.getBoolean(context.getString(R.string.key_preference_classes_general), false))
            return representations;

        Set<String> classes = sharedPreferences.getStringSet(context.getString(R.string.key_preference_classes_list), new HashSet<String>());

        if (classes.isEmpty())
            return representations;

        Vector<Representation> vector = new Vector<>(representations.length);

        for (Representation representation : representations) {
            if (classes.contains(representation.getSubject().getShorthand())) {
                vector.add(representation);
            }
        }

        Representation[] filteredRepresentations = new Representation[vector.size()];
        vector.copyInto(filteredRepresentations);
        return filteredRepresentations;
    }

    public static List<Representation> filterRepresentations(Context context, List<Representation> representations) {
        if (representations == null)
            return null;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        if (!sharedPreferences.getBoolean(context.getString(R.string.key_preference_classes_general), false))
            return representations;

        Set<String> classes = sharedPreferences.getStringSet(context.getString(R.string.key_preference_classes_list), new HashSet<String>());

        if (classes.isEmpty())
            return representations;

        for (Representation representation : representations) {
            if (!classes.contains(representation.getSubject().getShorthand())) {
                representations.remove(representation);
            }
        }

        return representations;
    }
}
