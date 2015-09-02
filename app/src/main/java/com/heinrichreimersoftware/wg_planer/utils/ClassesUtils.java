package com.heinrichreimersoftware.wg_planer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.heinrichreimersoftware.wg_planer.R;
import com.heinrichreimersoftware.wg_planer.structure.Lesson;
import com.heinrichreimersoftware.wg_planer.structure.Representation;
import com.heinrichreimersoftware.wg_planer.structure.Subject;
import com.heinrichreimersoftware.wg_planer.structure.TeacherSubject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClassesUtils {

    public static boolean shouldShow(Context context, Subject subject) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        if (!sharedPreferences.getBoolean(context.getString(R.string.key_preference_classes_general), false))
            return true;

        Set<String> classes = sharedPreferences.getStringSet(context.getString(R.string.key_preference_classes_list), new HashSet<String>());
        return classes == null || classes.isEmpty() || classes.contains(subject.getShorthand());
    }

    public static List<TeacherSubject> filterSubjects(Context context, List<TeacherSubject> subjects) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        if (!sharedPreferences.getBoolean(context.getString(R.string.key_preference_classes_general), false))
            return subjects;

        Set<String> classes = sharedPreferences.getStringSet(context.getString(R.string.key_preference_classes_list), new HashSet<String>());

        for (int i = subjects.size() - 1; i >= 0; i--) {
            if (classes != null && !classes.isEmpty() && !classes.contains(subjects.get(i).getShorthand())) {
                subjects.remove(i);
            }
        }

        return subjects;
    }

    public static List<Lesson> filterLessons(Context context, List<Lesson> lessons) {
        List<Lesson> filteredLessons = new ArrayList<>();

        for (Lesson lesson : lessons) {
            lesson.setSubjects(filterSubjects(context, lesson.getSubjects()));
            if (!lesson.getSubjects().isEmpty()) {
                filteredLessons.add(lesson);
            }
        }
        return filteredLessons;
    }

    public static List<Representation> filterRepresentations(Context context, List<Representation> representations) {
        List<Representation> filteredRepresentations = new ArrayList<>();

        for (Representation representation : representations) {
            if (shouldShow(context, representation.getRepresentedSubject())) {
                filteredRepresentations.add(representation);
            }
        }
        return filteredRepresentations;
    }

    public static List<Lesson> mergeLessons(List<Lesson> lessonList1, List<Lesson> lessonList2) {
        lessonList1.addAll(lessonList2);

        for (int i = 0; i < lessonList1.size(); i++) {
            Lesson lesson1 = lessonList1.get(i);
            for (int j = i + 1; j < lessonList1.size(); j++) {
                Lesson lesson2 = lessonList1.get(j);
                if (lesson1.getDay() == lesson2.getDay() &&
                        lesson1.getFirstLessonNumber() == lesson2.getFirstLessonNumber() &&
                        lesson1.getLastLessonNumber() == lesson2.getLastLessonNumber()) {
                    lesson1.mergeWith(lesson2);
                    lessonList1.remove(j);
                }
            }
        }

        return lessonList1;
    }
}
