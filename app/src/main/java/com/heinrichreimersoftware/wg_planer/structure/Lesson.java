package com.heinrichreimersoftware.wg_planer.structure;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.heinrichreimersoftware.wg_planer.R;
import com.heinrichreimersoftware.wg_planer.data.TeachersContentHelper;
import com.heinrichreimersoftware.wg_planer.data.TimetableDbHelper;
import com.heinrichreimersoftware.wg_planer.utils.ColorUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Lesson {

    private int day;
    private int firstLessonNumber;
    private int lastLessonNumber;
    private List<TeacherSubject> subjects;

    public static Lesson fromCursor(Cursor curRepresentations) {
        List<TeacherSubject> subjects = new Gson().fromJson(
                curRepresentations.getString(curRepresentations.getColumnIndex(
                        TimetableDbHelper.TIMETABLE_COL_SUBJECTS)),
                new TypeToken<List<TeacherSubject>>() {
                }.getType());
        return new Builder()
                .day(curRepresentations.getInt(curRepresentations.getColumnIndex(
                        TimetableDbHelper.TIMETABLE_COL_DAY)))
                .firstLessonNumber(curRepresentations.getInt(curRepresentations.getColumnIndex(
                        TimetableDbHelper.TIMETABLE_COL_FIRST_LESSON_NUMBER)))
                .lastLessonNumber(curRepresentations.getInt(curRepresentations.getColumnIndex(
                        TimetableDbHelper.TIMETABLE_COL_LAST_LESSON_NUMBER)))
                .subjects(subjects)
                .build();
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
        values.put(TimetableDbHelper.TIMETABLE_COL_SUBJECTS, new Gson().toJson(subjects));
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

    public boolean isOver() {
        Calendar currentTime = new GregorianCalendar();
        Calendar lessonEndTime = new GregorianCalendar();
        Calendar time = LessonTimeFactory.fromLesson(this).getEndTime();

        lessonEndTime.set(Calendar.DAY_OF_WEEK, day);
        lessonEndTime.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
        lessonEndTime.set(Calendar.MINUTE, time.get(Calendar.MINUTE));
        lessonEndTime.set(Calendar.SECOND, time.get(Calendar.SECOND));
        //lessonEndTime.add(Calendar.MILLISECOND, -currentTime.getTimeZone().getOffset(currentTime.getTimeInMillis()));
        if (currentTime.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || currentTime.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            lessonEndTime.add(Calendar.WEEK_OF_YEAR, 1);
        }
        return lessonEndTime.before(currentTime);
    }

    @SuppressWarnings("RedundantIfStatement")
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

    public Formatter getFormatter(Context context) {
        return new Formatter(context, this);
    }

    public static class Builder {

        private int day;
        private int firstLessonNumber;
        private int lastLessonNumber;
        private List<TeacherSubject> subjects;

        public int day() {
            return day;
        }

        public Builder day(int day) {
            this.day = day;
            return this;
        }

        public int firstLessonNumber() {
            return firstLessonNumber;
        }

        public Builder firstLessonNumber(int firstLessonNumber) {
            this.firstLessonNumber = firstLessonNumber;
            return this;
        }

        public int lastLessonNumber() {
            return lastLessonNumber;
        }

        public Builder lastLessonNumber(int lastLessonNumber) {
            this.lastLessonNumber = lastLessonNumber;
            return this;
        }

        public List<TeacherSubject> subjects() {
            return subjects;
        }

        public Builder subjects(TeacherSubject... subjects) {
            return subjects(new ArrayList<>(Arrays.asList(subjects)));
        }

        public Builder subjects(List<TeacherSubject> subjects) {
            this.subjects = subjects;
            return this;
        }

        public Lesson build() {
            Lesson lesson = new Lesson();

            lesson.setDay(day);
            lesson.setFirstLessonNumber(firstLessonNumber);
            lesson.setLastLessonNumber(lastLessonNumber);
            lesson.setSubjects(subjects);

            return lesson;
        }
    }

    public class Formatter {

        private Context context;
        private Lesson lesson;

        private Formatter(Context context, Lesson lesson) {
            this.context = context;
            this.lesson = lesson;
        }

        public String time() {
            if (lesson.getFirstLessonNumber() == lesson.getLastLessonNumber()) {
                return context.getString(R.string.format_lesson_time_1, lesson.getFirstLessonNumber(), LessonTimeFactory.fromLesson(lesson));
            } else {
                return context.getString(R.string.format_lesson_time_2, lesson.getFirstLessonNumber(), lesson.getLastLessonNumber(), LessonTimeFactory.fromLesson(lesson));
            }
        }

        public int color() {
            List<TeacherSubject> subjects = lesson.getSubjects();
            int color;

            if (subjects.size() > 1) {
                List<Integer> colors = new ArrayList<>();
                for (TeacherSubject subject : subjects) {
                    colors.add(subject.getColor());
                }
                color = ColorUtils.averageColor(colors);
            } else {
                color = subjects.get(0).getColor();
            }

            if (lesson.isOver()) {
                return ColorUtils.grey(color);
            } else {
                return color;
            }
        }

        public String teachers() {
            String teachersText = "";
            if (subjects.size() > 1) {

                for (int i = 0; i < subjects.size(); i++) {
                    if (i != 0) {
                        teachersText += ", ";
                    }
                    teachersText += subjects.get(i).getTeacher();
                }
            } else {
                Teacher teacher = TeachersContentHelper.getTeacher(context, subjects.get(0).getTeacher());
                if (teacher != null && !teacher.getLastName().equals("")) {
                    teachersText = teacher.getLastName();
                } else {
                    teachersText = subjects.get(0).getTeacher();
                }
            }
            return teachersText;
        }

        public String subjects() {
            if (subjects.size() > 1) {
                String subjectsText = "";

                for (int i = 0; i < subjects.size(); i++) {
                    if (i != 0) {
                        subjectsText += ", ";
                    }
                    subjectsText += subjects.get(i).getShorthand();
                }

                return subjectsText;
            } else {
                return subjects.get(0).getFullName();
            }
        }

        public String rooms() {
            return rooms(false);
        }

        public String rooms(boolean withBuilding) {
            if (subjects.size() > 1) {
                String roomsText = "";

                for (int i = 0; i < subjects.size(); i++) {
                    if (i != 0) {
                        roomsText += ", ";
                    }
                    String room = subjects.get(i).getRoom();
                    if (withBuilding) {
                        String building = null;
                        if (room.startsWith("A"))
                            building = context.getString(R.string.main_building);
                        if (room.startsWith("B"))
                            building = context.getString(R.string.branch_office);
                        roomsText += room + (building == null ? "" : " (" + building + ")");
                    } else {
                        roomsText += room;
                    }
                }

                return roomsText;
            } else {
                return subjects.get(0).getRoom();
            }
        }
    }
}