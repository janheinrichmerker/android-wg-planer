package com.heinrichreimersoftware.wg_planer.structure;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.heinrichreimersoftware.wg_planer.R;
import com.heinrichreimersoftware.wg_planer.data.RepresentationsDbHelper;
import com.heinrichreimersoftware.wg_planer.data.TeachersContentHelper;
import com.heinrichreimersoftware.wg_planer.data.UserContentHelper;
import com.heinrichreimersoftware.wg_planer.utils.ColorUtils;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Representation {

    private String schoolClass;
    private Calendar date;
    private int firstLessonNumber;
    private int lastLessonNumber;
    private Subject subject;
    private String fromTeacher;
    private String fromRoom;
    private String from;
    private String toTeacher;
    private String toRoom;
    private String to;
    private String description;

    public static Representation fromCursor(Cursor cursor) {
        Calendar date = new GregorianCalendar();
        date.setTimeInMillis(cursor.getLong(cursor.getColumnIndex(
                RepresentationsDbHelper.REPRESENTATIONS_COL_DATE)));

        return new Builder()
                .schoolClass(cursor.getString(cursor.getColumnIndex(
                        RepresentationsDbHelper.REPRESENTATIONS_COL_SCHOOL_CLASS)))
                .date(date)
                .firstLessonNumber(cursor.getInt(cursor.getColumnIndex(
                        RepresentationsDbHelper.REPRESENTATIONS_COL_FIRST_LESSON_NUMBER)))
                .lastLessonNumber(cursor.getInt(cursor.getColumnIndex(
                        RepresentationsDbHelper.REPRESENTATIONS_COL_LAST_LESSON_NUMBER)))
                .subject(new SubjectFactory().fromShorthand(cursor.getString(cursor.getColumnIndex(
                        RepresentationsDbHelper.REPRESENTATIONS_COL_SUBJECT))))
                .fromTeacher(cursor.getString(cursor.getColumnIndex(
                        RepresentationsDbHelper.REPRESENTATIONS_COL_FROM_TEACHER)))
                .fromRoom(cursor.getString(cursor.getColumnIndex(
                        RepresentationsDbHelper.REPRESENTATIONS_COL_FROM_ROOM)))
                .from(cursor.getString(cursor.getColumnIndex(
                        RepresentationsDbHelper.REPRESENTATIONS_COL_FROM)))
                .toTeacher(cursor.getString(cursor.getColumnIndex(
                        RepresentationsDbHelper.REPRESENTATIONS_COL_TO_TEACHER)))
                .toRoom(cursor.getString(cursor.getColumnIndex(
                        RepresentationsDbHelper.REPRESENTATIONS_COL_TO_ROOM)))
                .to(cursor.getString(cursor.getColumnIndex(
                        RepresentationsDbHelper.REPRESENTATIONS_COL_TO)))
                .description(cursor.getString(cursor.getColumnIndex(
                        RepresentationsDbHelper.REPRESENTATIONS_COL_DESCRIPTION)))
                .build();
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

    public String getFromTeacher() {
        return fromTeacher;
    }

    public void setFromTeacher(String fromTeacher) {
        this.fromTeacher = fromTeacher;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getToTeacher() {
        return toTeacher;
    }

    public void setToTeacher(String toTeacher) {
        this.toTeacher = toTeacher;
    }

    public String getFromRoom() {
        return fromRoom;
    }

    public void setFromRoom(String fromRoom) {
        this.fromRoom = fromRoom;
    }

    public String getToRoom() {
        return toRoom;
    }

    public void setToRoom(String toRoom) {
        this.toRoom = toRoom;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDescription() {
        if (description.equals("\u00A0")) return null;
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(RepresentationsDbHelper.REPRESENTATIONS_COL_SCHOOL_CLASS, schoolClass);
        values.put(RepresentationsDbHelper.REPRESENTATIONS_COL_DATE, date.getTimeInMillis());
        values.put(RepresentationsDbHelper.REPRESENTATIONS_COL_FIRST_LESSON_NUMBER, firstLessonNumber);
        values.put(RepresentationsDbHelper.REPRESENTATIONS_COL_LAST_LESSON_NUMBER, lastLessonNumber);
        values.put(RepresentationsDbHelper.REPRESENTATIONS_COL_FROM_TEACHER, fromTeacher);
        values.put(RepresentationsDbHelper.REPRESENTATIONS_COL_SUBJECT, subject.getShorthand());
        values.put(RepresentationsDbHelper.REPRESENTATIONS_COL_TO_TEACHER, toTeacher);
        values.put(RepresentationsDbHelper.REPRESENTATIONS_COL_FROM_ROOM, fromRoom);
        values.put(RepresentationsDbHelper.REPRESENTATIONS_COL_TO_ROOM, toRoom);
        values.put(RepresentationsDbHelper.REPRESENTATIONS_COL_FROM, from);
        values.put(RepresentationsDbHelper.REPRESENTATIONS_COL_TO, to);
        values.put(RepresentationsDbHelper.REPRESENTATIONS_COL_DESCRIPTION, description);
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
        if (!subject.equals(representation.subject)) return false;
        if (!fromTeacher.equals(representation.fromTeacher)) return false;
        if (!fromRoom.equals(representation.fromRoom)) return false;
        if (!from.equals(representation.from)) return false;
        if (!toTeacher.equals(representation.toTeacher)) return false;
        if (!toRoom.equals(representation.toRoom)) return false;
        if (!to.equals(representation.to)) return false;
        if (!description.equals(representation.description)) return false;
        return true;
    }

    public Formatter getFormatter(Context context) {
        return new Formatter(context, this);
    }

    public static class Builder {

        private String schoolClass;
        private Calendar date;
        private int firstLessonNumber;
        private int lastLessonNumber;
        private Subject subject;
        private String fromTeacher;
        private String fromRoom;
        private String from;
        private String toTeacher;
        private String toRoom;
        private String to;
        private String description;

        public String schoolClass() {
            return schoolClass;
        }

        public Builder schoolClass(String schoolClass) {
            this.schoolClass = schoolClass;
            return this;
        }

        public Calendar date() {
            return date;
        }

        public Builder date(Calendar date) {
            this.date = date;
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

        public Subject subject() {
            return subject;
        }

        public Builder subject(Subject subject) {
            this.subject = subject;
            return this;
        }

        public String fromTeacher() {
            return fromTeacher;
        }

        public Builder fromTeacher(String fromTeacher) {
            this.fromTeacher = fromTeacher;
            return this;
        }

        public String fromRoom() {
            return fromRoom;
        }

        public Builder fromRoom(String fromRoom) {
            this.fromRoom = fromRoom;
            return this;
        }

        public String from() {
            return from;
        }

        public Builder from(String from) {
            this.from = from;
            return this;
        }

        public String toTeacher() {
            return toTeacher;
        }

        public Builder toTeacher(String toTeacher) {
            this.toTeacher = toTeacher;
            return this;
        }

        public String toRoom() {
            return toRoom;
        }

        public Builder toRoom(String toRoom) {
            this.toRoom = toRoom;
            return this;
        }

        public String to() {
            return to;
        }

        public Builder to(String to) {
            this.to = to;
            return this;
        }

        public String description() {
            return description;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Representation build() {
            Representation representation = new Representation();

            representation.setSchoolClass(schoolClass);
            representation.setDate(date);
            representation.setFirstLessonNumber(firstLessonNumber);
            representation.setLastLessonNumber(lastLessonNumber);
            representation.setSubject(subject);
            representation.setFromTeacher(fromTeacher);
            representation.setFromRoom(fromRoom);
            representation.setFrom(from);
            representation.setToTeacher(toTeacher);
            representation.setToRoom(toRoom);
            representation.setTo(to);
            representation.setDescription(description);

            return representation;
        }
    }

    public class Formatter {

        private Context context;
        private Representation representation;

        private Formatter(Context context, Representation representation) {
            this.context = context;
            this.representation = representation;
        }

        public String time() {
            if (representation.getFirstLessonNumber() == representation.getLastLessonNumber()) {
                return context.getString(R.string.format_lesson_1, representation.getFirstLessonNumber(), LessonTimeFactory.fromRepresentation(representation));
            } else {
                return context.getString(R.string.format_lesson_2, representation.getFirstLessonNumber(), representation.getLastLessonNumber(), LessonTimeFactory.fromRepresentation(representation));
            }
        }

        public int color() {
            Calendar currentTime = new GregorianCalendar();
            Calendar representationEndTime = representation.getDate();
            Calendar time = LessonTimeFactory.fromRepresentation(representation).getEndTime();

            representationEndTime.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
            representationEndTime.set(Calendar.MINUTE, time.get(Calendar.MINUTE));
            representationEndTime.set(Calendar.SECOND, time.get(Calendar.SECOND));
            representationEndTime.add(Calendar.HOUR, -1);
            if (currentTime.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || currentTime.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                representationEndTime.add(Calendar.WEEK_OF_YEAR, 1);
            }

            if (representationEndTime.before(currentTime)) {
                return ColorUtils.grey(representation.getSubject().getColor());
            } else {
                return representation.getSubject().getColor();
            }
        }

        public String info() {
            if (!representation.getTo().equals("") && !representation.getTo().equals("\u00A0")) {
                return "";
            } else {
                String info = "";
                info += "in " + representation.getToRoom();

                Teacher teacher = TeachersContentHelper.getTeacher(context, representation.getToTeacher());
                if (teacher != null && !teacher.getLastName().equals("")) {
                    info += " (" + teacher.getLastName() + ")";
                } else {
                    info += " (" + representation.getToTeacher() + ")";
                }
                return info;
            }
        }

        public String type() {
            String type = "";

            if (representation.getTo().equals("Entfall")) {
                type += representation.getTo();
            } else if (!representation.getFrom().equals("") && !representation.getFrom().equals("\u00A0")) {
                type += "Vertretung von " + representation.getFrom();
                if (!representation.getTo().equals("") && !representation.getTo().equals("\u00A0") && !representation.getFrom().equals("") && !representation.getFrom().equals("\u00A0")) {
                    type += "\nVerlegt nach " + representation.getTo();
                }
            } else if (!representation.getTo().equals("") && !representation.getTo().equals("\u00A0")) {
                type += "Verlegt nach " + representation.getTo();
            } else {
                type += "Vertretung";
            }

            return type;
        }

        public String subject() {
            boolean hasSetSchoolClass = false;

            User user = UserContentHelper.getUser(context);
            if (user != null && user.getSchoolClass() != null && !user.getSchoolClass().equals(""))
                hasSetSchoolClass = true;

            String representationSubjectText = representation.subject.getFullName();

            SubjectFactory subjectFactory = new SubjectFactory();
            if (subjectFactory.isMultiTeacherSubject(representation.subject)) {
                representationSubjectText += " (" + representation.fromTeacher;
                if (!hasSetSchoolClass) {
                    representationSubjectText += ", " + representation.schoolClass;
                }
                representationSubjectText += ")";
            } else {
                if (!hasSetSchoolClass) {
                    representationSubjectText += " (" + representation.schoolClass + ")";
                }
            }
            return representationSubjectText;
        }

        public String summary() {
            String output = subject();

            if (representation.to.equals("Entfall")) {
                output += " - Entfall";
            } else if (!representation.from.equals("") && !representation.from.equals("\u00A0")) {
                output += " - Vertretung";
                if (!representation.to.equals("") && !representation.to.equals("\u00A0")) {
                    output += "/Verlegt";
                }
            } else if (!representation.to.equals("") && !representation.to.equals("\u00A0")) {
                output += " - Verlegt";
            } else {
                output += " - Vertretung";
            }

            return output;
        }

        public String description() {
            String description = "";
            if (representation.firstLessonNumber == representation.lastLessonNumber) {
                description += representation.firstLessonNumber + ". Stunde: ";
            } else {
                description += representation.firstLessonNumber + ".-" + representation.lastLessonNumber + ". Stunde: ";
            }

            Teacher teacher = TeachersContentHelper.getTeacher(context, representation.fromTeacher);

            SubjectFactory subjectFactory = new SubjectFactory();

            if (subjectFactory.isMultiTeacherSubject(representation.subject)) {
                if (teacher != null && teacher.getLastName() != null &&
                        !teacher.getLastName().equals("")) {
                    description += representation.subject.getFullName() + " (" + teacher.getLastName() + ")";
                } else {
                    description += representation.subject.getFullName() + " (" + representation.fromTeacher + ")";
                }
            } else {
                description += representation.subject.getFullName();
            }

            if (representation.to.equals("Entfall")) {
                description += " - Entfall";
            } else if (!representation.from.equals("") && !representation.from.equals("\u00A0")) {
                description += " - Vertretung";
                if (!representation.to.equals("") && !representation.to.equals("\u00A0")) {
                    description += "/Verlegt von " + representation.from;
                }
                description += " nach " + representation.to +
                        " in " + representation.toRoom;
            } else if (!representation.to.equals("") && !representation.to.equals("\u00A0")) {
                description += " - Verlegt nach " + representation.to;
            } else {
                description += " - Vertretung" +
                        " in " + representation.toRoom;
            }

            if (!representation.description.equals("") && !representation.description.equals("\u00A0")) {
                description += " (" + representation.description + ")";
            }

            return description;
        }
    }
}