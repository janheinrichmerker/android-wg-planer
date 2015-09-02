package com.heinrichreimersoftware.wg_planer.structure;

import android.content.Context;

import com.heinrichreimersoftware.wg_planer.R;
import com.heinrichreimersoftware.wg_planer.data.TeachersContentHelper;
import com.heinrichreimersoftware.wg_planer.utils.ColorUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Formatter {

    public static String formatLessonTime(Context context, Lesson lesson) {
        if (lesson.getFirstLessonNumber() == lesson.getLastLessonNumber()) {
            return context.getString(R.string.format_lesson_1, lesson.getFirstLessonNumber(), LessonTimeFactory.fromLesson(lesson));
        } else {
            return context.getString(R.string.format_lesson_2, lesson.getFirstLessonNumber(), lesson.getLastLessonNumber(), LessonTimeFactory.fromLesson(lesson));
        }
    }

    public static String formatLessonTime(Context context, Representation representation) {
        if (representation.getFirstLessonNumber() == representation.getLastLessonNumber()) {
            return context.getString(R.string.format_lesson_1, representation.getFirstLessonNumber(), LessonTimeFactory.fromRepresentation(representation));
        } else {
            return context.getString(R.string.format_lesson_2, representation.getFirstLessonNumber(), representation.getLastLessonNumber(), LessonTimeFactory.fromRepresentation(representation));
        }
    }

    public static int formatLessonColor(Lesson lesson) {
        Calendar currentTime = new GregorianCalendar();
        currentTime.setFirstDayOfWeek(Calendar.SATURDAY);

        Calendar lessonEndTime = new GregorianCalendar();
        lessonEndTime.setFirstDayOfWeek(Calendar.SATURDAY);
        lessonEndTime.set(Calendar.WEEK_OF_YEAR, currentTime.get(Calendar.WEEK_OF_YEAR));
        lessonEndTime.set(Calendar.DAY_OF_WEEK, lesson.getDay());

        Calendar time = LessonTimeFactory.fromLesson(lesson).endTime;
        lessonEndTime.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
        lessonEndTime.set(Calendar.MINUTE, time.get(Calendar.MINUTE));
        lessonEndTime.set(Calendar.SECOND, time.get(Calendar.SECOND));
        lessonEndTime.add(Calendar.HOUR, -1);

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

        if (lessonEndTime.before(currentTime)) {
            return ColorUtils.grey(color);
        } else {
            return color;
        }
    }

    public static int formatLessonColor(Representation representation) {
        Calendar currentTime = new GregorianCalendar();
        currentTime.setFirstDayOfWeek(Calendar.MONDAY);

        Calendar representationEndTime = representation.getDate();
        representationEndTime.setFirstDayOfWeek(Calendar.MONDAY);
        Calendar time = LessonTimeFactory.fromRepresentation(representation).endTime;
        representationEndTime.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
        representationEndTime.set(Calendar.MINUTE, time.get(Calendar.MINUTE));
        representationEndTime.set(Calendar.SECOND, time.get(Calendar.SECOND));
        representationEndTime.add(Calendar.HOUR, -1);

        if (representationEndTime.before(currentTime)) {
            return ColorUtils.grey(representation.getRepresentedSubject().getColor());
        } else {
            return representation.getRepresentedSubject().getColor();
        }
    }

    public static String formatTeachers(Context context, Lesson lesson) {
        List<TeacherSubject> subjects = lesson.getSubjects();
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

    public static String formatSubjects(Lesson lesson) {
        List<TeacherSubject> subjects = lesson.getSubjects();

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

    public static String formatRooms(Lesson lesson) {
        List<TeacherSubject> subjects = lesson.getSubjects();

        if (subjects.size() > 1) {
            String roomsText = "";

            for (int i = 0; i < subjects.size(); i++) {
                if (i != 0) {
                    roomsText += ", ";
                }
                roomsText += subjects.get(i).getRoom();
            }

            return roomsText;
        } else {
            return subjects.get(0).getRoom();
        }
    }

    public static String formatRepresentationInfo(Context context, Representation representation) {
        if (!representation.getRepresentedTo().equals("") && !representation.getRepresentedTo().equals("\u00A0")) {
            return "";
        } else {
            String info = "";
            info += "in " + representation.getRepresentingRoom();

            Teacher teacher = TeachersContentHelper.getTeacher(context, representation.getRepresentingTeacher());
            if (teacher != null && !teacher.getLastName().equals("")) {
                info += " (" + teacher.getLastName() + ")";
            } else {
                info += " (" + representation.getRepresentingTeacher() + ")";
            }
            return info;
        }
    }

    public static String formatRepresentationType(Representation representation) {
        String type = "";

        if (representation.getRepresentedTo().equals("Entfall")) {
            type += representation.getRepresentedTo();
        } else if (!representation.getRepresentedFrom().equals("") && !representation.getRepresentedFrom().equals("\u00A0")) {
            type += "Vertretung von " + representation.getRepresentedFrom();
            if (!representation.getRepresentedTo().equals("") && !representation.getRepresentedTo().equals("\u00A0") && !representation.getRepresentedFrom().equals("") && !representation.getRepresentedFrom().equals("\u00A0")) {
                type += "\nVerlegt nach " + representation.getRepresentedTo();
            }
        } else if (!representation.getRepresentedTo().equals("") && !representation.getRepresentedTo().equals("\u00A0")) {
            type += "Verlegt nach " + representation.getRepresentedTo();
        } else {
            type += "Vertretung";
        }

        return type;
    }

    public static String formatRepresentationText(Representation representation) {
        if (!representation.getRepresentationText().equals("") && !representation.getRepresentationText().equals("\u00A0")) {
            return "(" + representation.getRepresentationText() + ")";
        } else {
            return "";
        }
    }
}
