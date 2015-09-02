package com.heinrichreimersoftware.wg_planer.structure;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.heinrichreimersoftware.wg_planer.data.TimetableDbHelper;

import java.lang.reflect.Type;
import java.util.List;

public class Lesson {

    private int day;
    private int firstLessonNumber;
    private int lastLessonNumber;
    private List<TeacherSubject> subjects;

    public Lesson(int day,
                  int firstLessonNumber,
                  int lastLessonNumber,
                  List<TeacherSubject> subjects) {
        this.day = day;
        this.firstLessonNumber = firstLessonNumber;
        this.lastLessonNumber = lastLessonNumber;
        this.subjects = subjects;
    }

    public static Lesson fromCursor(Cursor curRepresentations) {
        int day = curRepresentations.getInt(curRepresentations.getColumnIndex(TimetableDbHelper.TIMETABLE_COL_DAY));
        int firstLessonNumber = curRepresentations.getInt(curRepresentations.getColumnIndex(TimetableDbHelper.TIMETABLE_COL_FIRST_LESSON_NUMBER));
        int lastLessonNumber = curRepresentations.getInt(curRepresentations.getColumnIndex(TimetableDbHelper.TIMETABLE_COL_LAST_LESSON_NUMBER));
        String subjectsJson = curRepresentations.getString(curRepresentations.getColumnIndex(TimetableDbHelper.TIMETABLE_COL_SUBJECTS));


        Gson gson = new Gson();
        Type type = new TypeToken<List<TeacherSubject>>() {
        }.getType();
        List<TeacherSubject> subjects = gson.fromJson(subjectsJson, type);

        return new Lesson(
                day,
                firstLessonNumber,
                lastLessonNumber,
                subjects
        );
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getFirstLessonNumber() {
        return firstLessonNumber;
    }

    public void setFirstLessonNumber(int firstLessonNumber) {
        this.firstLessonNumber = firstLessonNumber;
    }

    public int getLastLessonNumber() {
        return lastLessonNumber;
    }

    public void setLastLessonNumber(int lastLessonNumber) {
        this.lastLessonNumber = lastLessonNumber;
    }

    public List<TeacherSubject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<TeacherSubject> subjects) {
        this.subjects = subjects;
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(TimetableDbHelper.TIMETABLE_COL_DAY, day);
        values.put(TimetableDbHelper.TIMETABLE_COL_FIRST_LESSON_NUMBER, firstLessonNumber);
        values.put(TimetableDbHelper.TIMETABLE_COL_LAST_LESSON_NUMBER, lastLessonNumber);

        Gson gson = new Gson();
        values.put(TimetableDbHelper.TIMETABLE_COL_SUBJECTS, gson.toJson(subjects));
        return values;
    }

    public void mergeWith(Lesson lesson) {
        subjects.addAll(lesson.getSubjects());

        for (int i = 0; i < subjects.size(); i++) {
            TeacherSubject subject1 = subjects.get(i);
            for (int j = i + 1; j < subjects.size(); j++) {
                TeacherSubject subject2 = subjects.get(j);
                if (subject1.equals(subject2)) {
                    subjects.remove(j);
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Lesson representation = (Lesson) o;

        if (day != representation.day) return false;
        if (firstLessonNumber != representation.firstLessonNumber) return false;
        if (lastLessonNumber != representation.lastLessonNumber) return false;
        if (!subjects.equals(representation.subjects)) return false;
        return true;
    }

    @Override
    public String toString() {
        return firstLessonNumber + ".-" + lastLessonNumber + ". Stunde (" + day + ")";
    }

}