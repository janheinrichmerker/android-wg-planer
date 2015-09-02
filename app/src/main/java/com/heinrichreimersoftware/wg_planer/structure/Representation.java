package com.heinrichreimersoftware.wg_planer.structure;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.heinrichreimersoftware.wg_planer.data.RepresentationsDbHelper;
import com.heinrichreimersoftware.wg_planer.data.TeachersContentHelper;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Representation {

    private String schoolClass;
    private Calendar date;
    private int firstLessonNumber;
    private int lastLessonNumber;
    private String representedTeacher;
    private Subject representedSubject;
    private String representingTeacher;
    private String representedRoom;
    private String representingRoom;
    private String representedFrom;
    private String representedTo;
    private String representationText;

    public Representation(String schoolClass,
                          Calendar date,
                          int firstLessonNumber,
                          int lastLessonNumber,
                          String representedTeacher,
                          Subject representedSubject,
                          String representingTeacher,
                          String representedRoom,
                          String representingRoom,
                          String representedFrom,
                          String representedTo,
                          String representationText) {
        this.schoolClass = schoolClass;
        this.date = date;
        this.firstLessonNumber = firstLessonNumber;
        this.lastLessonNumber = lastLessonNumber;
        this.representedTeacher = representedTeacher;
        this.representedSubject = representedSubject;
        this.representingTeacher = representingTeacher;
        this.representedRoom = representedRoom;
        this.representingRoom = representingRoom;
        this.representedFrom = representedFrom;
        this.representedTo = representedTo;
        this.representationText = representationText;
    }

    public static Representation fromCursor(Cursor curRepresentations) {
        String schoolClass = curRepresentations.getString(curRepresentations.getColumnIndex(RepresentationsDbHelper.REPRESENTATIONS_COL_SCHOOL_CLASS));
        Calendar date = new GregorianCalendar();
        date.setTimeInMillis(curRepresentations.getLong(curRepresentations.getColumnIndex(RepresentationsDbHelper.REPRESENTATIONS_COL_DATE)));
        int firstLessonNumber = curRepresentations.getInt(curRepresentations.getColumnIndex(RepresentationsDbHelper.REPRESENTATIONS_COL_FIRST_LESSON_NUMBER));
        int lastLessonNumber = curRepresentations.getInt(curRepresentations.getColumnIndex(RepresentationsDbHelper.REPRESENTATIONS_COL_LAST_LESSON_NUMBER));
        String representedTeacher = curRepresentations.getString(curRepresentations.getColumnIndex(RepresentationsDbHelper.REPRESENTATIONS_COL_REPRESENTED_TEACHER));

        SubjectFactory subjectFactory = new SubjectFactory();
        Subject representedSubject = subjectFactory.fromShorthand(curRepresentations.getString(curRepresentations.getColumnIndex(RepresentationsDbHelper.REPRESENTATIONS_COL_REPRESENTED_SUBJECT)));

        String representingTeacher = curRepresentations.getString(curRepresentations.getColumnIndex(RepresentationsDbHelper.REPRESENTATIONS_COL_REPRESENTING_TEACHER));
        String representedRoom = curRepresentations.getString(curRepresentations.getColumnIndex(RepresentationsDbHelper.REPRESENTATIONS_COL_REPRESENTED_ROOM));
        String representingRoom = curRepresentations.getString(curRepresentations.getColumnIndex(RepresentationsDbHelper.REPRESENTATIONS_COL_REPRESENTING_ROOM));
        String representedFrom = curRepresentations.getString(curRepresentations.getColumnIndex(RepresentationsDbHelper.REPRESENTATIONS_COL_REPRESENTED_FROM));
        String representedTo = curRepresentations.getString(curRepresentations.getColumnIndex(RepresentationsDbHelper.REPRESENTATIONS_COL_REPRESENTED_TO));
        String representationText = curRepresentations.getString(curRepresentations.getColumnIndex(RepresentationsDbHelper.REPRESENTATIONS_COL_REPRESENTATION_TEXT));

        return new Representation(schoolClass,
                date,
                firstLessonNumber,
                lastLessonNumber,
                representedTeacher,
                representedSubject,
                representingTeacher,
                representedRoom,
                representingRoom,
                representedFrom,
                representedTo,
                representationText);
    }

    public String getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(String schoolClass) {
        this.schoolClass = schoolClass;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
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

    public String getRepresentedTeacher() {
        return representedTeacher;
    }

    public void setRepresentedTeacher(String representedTeacher) {
        this.representedTeacher = representedTeacher;
    }

    public Subject getRepresentedSubject() {
        return representedSubject;
    }

    public void setRepresentedSubject(Subject representedSubject) {
        this.representedSubject = representedSubject;
    }

    public String getRepresentingTeacher() {
        return representingTeacher;
    }

    public void setRepresentingTeacher(String representingTeacher) {
        this.representingTeacher = representingTeacher;
    }

    public String getRepresentedRoom() {
        return representedRoom;
    }

    public void setRepresentedRoom(String representedRoom) {
        this.representedRoom = representedRoom;
    }

    public String getRepresentingRoom() {
        return representingRoom;
    }

    public void setRepresentingRoom(String representingRoom) {
        this.representingRoom = representingRoom;
    }

    public String getRepresentedFrom() {
        return representedFrom;
    }

    public void setRepresentedFrom(String representedFrom) {
        this.representedFrom = representedFrom;
    }

    public String getRepresentedTo() {
        return representedTo;
    }

    public void setRepresentedTo(String representedTo) {
        this.representedTo = representedTo;
    }

    public String getRepresentationText() {
        return representationText;
    }

    public void setRepresentationText(String representationText) {
        this.representationText = representationText;
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(RepresentationsDbHelper.REPRESENTATIONS_COL_SCHOOL_CLASS, schoolClass);
        values.put(RepresentationsDbHelper.REPRESENTATIONS_COL_DATE, date.getTimeInMillis());
        values.put(RepresentationsDbHelper.REPRESENTATIONS_COL_FIRST_LESSON_NUMBER, firstLessonNumber);
        values.put(RepresentationsDbHelper.REPRESENTATIONS_COL_LAST_LESSON_NUMBER, lastLessonNumber);
        values.put(RepresentationsDbHelper.REPRESENTATIONS_COL_REPRESENTED_TEACHER, representedTeacher);
        values.put(RepresentationsDbHelper.REPRESENTATIONS_COL_REPRESENTED_SUBJECT, representedSubject.getShorthand());
        values.put(RepresentationsDbHelper.REPRESENTATIONS_COL_REPRESENTING_TEACHER, representingTeacher);
        values.put(RepresentationsDbHelper.REPRESENTATIONS_COL_REPRESENTED_ROOM, representedRoom);
        values.put(RepresentationsDbHelper.REPRESENTATIONS_COL_REPRESENTING_ROOM, representingRoom);
        values.put(RepresentationsDbHelper.REPRESENTATIONS_COL_REPRESENTED_FROM, representedFrom);
        values.put(RepresentationsDbHelper.REPRESENTATIONS_COL_REPRESENTED_TO, representedTo);
        values.put(RepresentationsDbHelper.REPRESENTATIONS_COL_REPRESENTATION_TEXT, representationText);
        return values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Representation representation = (Representation) o;

        if (!schoolClass.equals(representation.schoolClass)) return false;
        if (date != representation.date) return false;
        if (firstLessonNumber != representation.firstLessonNumber) return false;
        if (lastLessonNumber != representation.lastLessonNumber) return false;
        if (!representedTeacher.equals(representation.representedTeacher)) return false;
        if (!representedSubject.equals(representation.representedSubject)) return false;
        if (!representingTeacher.equals(representation.representingTeacher)) return false;
        if (!representedRoom.equals(representation.representedRoom)) return false;
        if (!representingRoom.equals(representation.representingRoom)) return false;
        if (!representedFrom.equals(representation.representedFrom)) return false;
        if (!representedTo.equals(representation.representedTo)) return false;
        if (!representationText.equals(representation.representationText)) return false;
        return true;
    }

    public String getRepresentedSubjectText(boolean hasSetSchoolClass) {
        String representationSubjectText = representedSubject.getFullName();

        SubjectFactory subjectFactory = new SubjectFactory();
        if (subjectFactory.isMultiTeacherSubject(representedSubject)) {
            representationSubjectText += " (" + representedTeacher;
            if (!hasSetSchoolClass) {
                representationSubjectText += ", " + schoolClass;
            }
            representationSubjectText += ")";
        } else {
            if (!hasSetSchoolClass) {
                representationSubjectText += " (" + schoolClass + ")";
            }
        }
        return representationSubjectText;
    }

    public String getRepresentedSubjectText() {
        return getRepresentedSubjectText(true);
    }

    public String getSummary() {
        String output = "";

        output += getRepresentedSubjectText();

        if (representedTo.equals("Entfall")) {
            output += " - Entfall";
        } else if (!representedFrom.equals("") && !representedFrom.equals("\u00A0")) {
            output += " - Vertretung";
            if (!representedTo.equals("") && !representedTo.equals("\u00A0")) {
                output += "/Verlegt";
            }
        } else if (!representedTo.equals("") && !representedTo.equals("\u00A0")) {
            output += " - Verlegt";
        } else {
            output += " - Vertretung";
        }

        return output;
    }

    public String getDescription(Context context) {
        String output = "";
        if (firstLessonNumber == lastLessonNumber) {
            output += firstLessonNumber + ". Stunde: ";
        } else {
            output += firstLessonNumber + ".-" + lastLessonNumber + ". Stunde: ";
        }

        Teacher teacher = TeachersContentHelper.getTeacher(context, representedTeacher);

        SubjectFactory subjectFactory = new SubjectFactory();

        if (subjectFactory.isMultiTeacherSubject(representedSubject)) {
            if (teacher != null && teacher.getLastName() != null && !teacher.getLastName().equals("")) {
                output += representedSubject.getFullName() + " (" + teacher.getLastName() + ")";
            } else {
                output += representedSubject.getFullName() + " (" + representedTeacher + ")";
            }
        } else {
            output += representedSubject.getFullName();
        }

        if (representedTo.equals("Entfall")) {
            output += " - Entfall";
        } else if (!representedFrom.equals("") && !representedFrom.equals("\u00A0")) {
            output += " - Vertretung";
            if (!representedTo.equals("") && !representedTo.equals("\u00A0")) {
                output += "/Verlegt von " + representedFrom;
            }
            output += " nach " + representedTo +
                    " in " + representingRoom;
        } else if (!representedTo.equals("") && !representedTo.equals("\u00A0")) {
            output += " - Verlegt nach " + representedTo;
        } else {
            output += " - Vertretung" +
                    " in " + representingRoom;
        }

        if (!representationText.equals("") && !representationText.equals("\u00A0")) {
            output += " (" + representationText + ")";
        }

        return output;
    }


    @Override
    public String toString() {
        return schoolClass + ": " + representedSubject + ", " + firstLessonNumber + "-" + lastLessonNumber + "(" + date.get(Calendar.DAY_OF_MONTH) + "." + date.get(Calendar.MONTH) + "." + date.get(Calendar.YEAR) + ")";
    }

}