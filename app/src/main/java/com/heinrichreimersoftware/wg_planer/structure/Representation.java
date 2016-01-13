package com.heinrichreimersoftware.wg_planer.structure;

import android.content.Context;
import android.text.TextUtils;

import com.afollestad.inquiry.annotations.Column;
import com.heinrichreimersoftware.wg_planer.Constants;
import com.heinrichreimersoftware.wg_planer.R;
import com.heinrichreimersoftware.wg_planer.content.TeachersContentHelper;
import com.heinrichreimersoftware.wg_planer.content.UserContentHelper;
import com.heinrichreimersoftware.wg_planer.utils.ColorUtils;
import com.heinrichreimersoftware.wg_planer.utils.factories.LessonTimeFactory;
import com.heinrichreimersoftware.wg_planer.utils.factories.SubjectFactory;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Representation {
    @Column(name = Constants.DATABASE_COLUMN_NAME_ID, primaryKey = true, notNull = true, autoIncrement = true)
    private long id;

    @Column(name = Constants.DATABASE_COLUMN_NAME_SCHOOL_CLASS)
    private String schoolClass;
    @Reference(columnName = Constants.DATABASE_COLUMN_NAME_SUBJECT,
            tableName = Constants.DATABASE_TABLE_NAME_SUBJECTS)
    private Subject subject; //FIXME
    @Reference(columnName = Constants.DATABASE_COLUMN_NAME_FROM,
            tableName = Constants.DATABASE_TABLE_NAME_FROM_TOS)
    private FromTo from; //FIXME
    @Reference(columnName = Constants.DATABASE_COLUMN_NAME_TO,
            tableName = Constants.DATABASE_TABLE_NAME_FROM_TOS)
    private FromTo to; //FIXME
    @Column(name = Constants.DATABASE_COLUMN_NAME_DESCRIPTION)
    private String description;

    public Representation() {
    }

    public Representation(Builder builder) {
        schoolClass = builder.schoolClass;
        subject = builder.subject;
        from = builder.from;
        to = builder.to;
        description = builder.description;
    }

    public String getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(String schoolClass) {
        this.schoolClass = schoolClass;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public FromTo getFrom() {
        return from;
    }

    public void setFrom(FromTo from) {
        this.from = from;
    }

    public FromTo getTo() {
        return to;
    }

    public void setTo(FromTo to) {
        this.to = to;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isOver() {
        Calendar currentTime = new GregorianCalendar();
        Calendar endTime = (Calendar) getFrom().getDate().clone();
        Calendar time = LessonTimeFactory.fromRepresentationFromTo(getTo()).getEndTime();

        endTime.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
        endTime.set(Calendar.MINUTE, time.get(Calendar.MINUTE));
        endTime.set(Calendar.SECOND, time.get(Calendar.SECOND));
        //endTime.add(Calendar.MILLISECOND, -currentTime.getTimeZone().getOffset(currentTime.getTimeInMillis()));
        if (currentTime.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || currentTime.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            endTime.add(Calendar.WEEK_OF_YEAR, 1);
        }
        return endTime.before(currentTime);
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Representation representation = (Representation) o;

        if (!schoolClass.equals(representation.schoolClass)) return false;
        if (!subject.equals(representation.subject)) return false;
        if (!from.equals(representation.from)) return false;
        if (!to.equals(representation.to)) return false;
        if (!description.equals(representation.description)) return false;
        return true;
    }

    public static class FromTo {
        public static final FromTo ELIMINATION = new FromTo();

        @Column(name = Constants.DATABASE_COLUMN_NAME_ID, primaryKey = true, notNull = true, autoIncrement = true)
        private long id;

        @Column(name = Constants.DATABASE_COLUMN_NAME_DATE)
        private Calendar date;
        @Column(name = Constants.DATABASE_COLUMN_NAME_FIRST_LESSON_NUMBER)
        private int firstLessonNumber;
        @Column(name = Constants.DATABASE_COLUMN_NAME_LAST_LESSON_NUMBER)
        private int lastLessonNumber;
        @Column(name = Constants.DATABASE_COLUMN_NAME_ROOM)
        private String room;
        @Reference(columnName = Constants.DATABASE_COLUMN_NAME_TEACHER,
                tableName = Constants.DATABASE_TABLE_NAME_TEACHERS)
        private Teacher teacher; //FIXME

        public FromTo() {
        }

        public FromTo(Builder builder) {
            date = builder.date;
            firstLessonNumber = builder.firstLessonNumber;
            lastLessonNumber = builder.lastLessonNumber;
            room = builder.room;
            teacher = builder.teacher;
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

        public String getRoom() {
            return room;
        }

        public void setRoom(String room) {
            this.room = room;
        }

        public Teacher getTeacher() {
            return teacher;
        }

        public void setTeacher(Teacher teacher) {
            this.teacher = teacher;
        }

        @SuppressWarnings("RedundantIfStatement")
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            FromTo fromTo = (FromTo) o;

            if (!date.equals(fromTo.date)) return false;
            if (firstLessonNumber != fromTo.firstLessonNumber) return false;
            if (lastLessonNumber != fromTo.lastLessonNumber) return false;
            if (!room.equals(fromTo.room)) return false;
            if (!teacher.equals(fromTo.teacher)) return false;
            return true;
        }

        public static class Builder {
            private Calendar date;
            private int firstLessonNumber;
            private int lastLessonNumber;
            private String room;
            private Teacher teacher;

            public Calendar date() {
                return date;
            }

            public void date(Calendar date) {
                this.date = date;
            }

            public int firstLessonNumber() {
                return firstLessonNumber;
            }

            public void firstLessonNumber(int firstLessonNumber) {
                this.firstLessonNumber = firstLessonNumber;
            }

            public int lastLessonNumber() {
                return lastLessonNumber;
            }

            public void lastLessonNumber(int lastLessonNumber) {
                this.lastLessonNumber = lastLessonNumber;
            }

            public String room() {
                return room;
            }

            public void room(String room) {
                this.room = room;
            }

            public Teacher teacher() {
                return teacher;
            }

            public void teacher(Teacher teacher) {
                this.teacher = teacher;
            }

            public FromTo build() {
                return new FromTo(this);
            }
        }
    }

    public static class Builder {

        private String schoolClass;
        private Subject subject;
        private FromTo from;
        private FromTo to;
        private String description;

        public String schoolClass() {
            return schoolClass;
        }

        public Builder schoolClass(String schoolClass) {
            this.schoolClass = schoolClass;
            return this;
        }

        public Subject subject() {
            return subject;
        }

        public Builder subject(Subject subject) {
            this.subject = subject;
            return this;
        }

        public FromTo from() {
            return from;
        }

        public Builder from(FromTo from) {
            this.from = from;
            return this;
        }

        public FromTo to() {
            return to;
        }

        public Builder to(FromTo to) {
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
            return new Representation(this);
        }
    }

    public static class Formatter {

        public static String time(Context context, Representation.FromTo fromTo) {
            if (fromTo.getFirstLessonNumber() == fromTo.getLastLessonNumber()) {
                return context.getString(R.string.format_lesson_time_1, fromTo.getFirstLessonNumber(), LessonTimeFactory.fromRepresentationFromTo(fromTo));
            } else {
                return context.getString(R.string.format_lesson_time_2, fromTo.getFirstLessonNumber(), fromTo.getLastLessonNumber(), LessonTimeFactory.fromRepresentationFromTo(fromTo));
            }
        }

        public static int color(Representation representation) {
            if (representation.isOver()) {
                return ColorUtils.grey(representation.getSubject().getColor());
            } else {
                return representation.getSubject().getColor();
            }
        }

        public static String info(Context context, Representation representation) {
            if (TextUtils.isEmpty(representation.getTo().getRoom()) || TextUtils.isEmpty(representation.getTo().getTeacher())) {
                return "";
            } else {
                String info = "";
                info += "in " + representation.getTo().getRoom();

                Teacher teacher = TeachersContentHelper.getTeacher(context, representation.getTo().getTeacher());
                if (teacher != null && !teacher.getLastName().equals("")) {
                    info += " (" + teacher.getLastName() + ")";
                } else {
                    info += " (" + representation.getTo().getTeacher() + ")";
                }
                return info;
            }
        }

        public static String type(Representation representation) {
            String type;

            if (representation.getTo().equals(FromTo.ELIMINATION)) {
                type = "Entfall";
            } else {
                type = "Vertretung";
            }

            return type;
        }

        public static String subject(Context context, Representation representation) {
            boolean hasSetSchoolClass = false;

            User user = UserContentHelper.getUser(context);
            if (user != null && user.getSchoolClasses() != null && user.getSchoolClasses().length == 0)
                hasSetSchoolClass = true;

            String representationSubjectText = representation.getSubject().getFullName();

            SubjectFactory subjectFactory = new SubjectFactory();
            if (subjectFactory.isMultiTeacherSubject(representation.getSubject())) {
                representationSubjectText += " (" + representation.getFrom().getTeacher();
                if (!hasSetSchoolClass) {
                    representationSubjectText += ", " + representation.getSchoolClass();
                }
                representationSubjectText += ")";
            } else {
                if (!hasSetSchoolClass) {
                    representationSubjectText += " (" + representation.getSchoolClass() + ")";
                }
            }
            return representationSubjectText;
        }

        public static String summary(Context context, Representation representation) {
            String output = subject(context, representation);

            if (representation.getTo().equals(FromTo.ELIMINATION)) {
                output += " - Entfall";
            } else {
                output += " - Vertretung";
            }

            return output;
        }

        public static String description(Context context, Representation representation) {
            String description = "";
            FromTo fromTo;
            if (representation.getTo().getDate() != null && representation.getTo().getFirstLessonNumber() != 0 && representation.getTo().getLastLessonNumber() != 0) {
                fromTo = representation.getTo();
            } else {
                fromTo = representation.getFrom();
            }

            if (fromTo.getFirstLessonNumber() == fromTo.getLastLessonNumber()) {
                description += fromTo.getFirstLessonNumber() + ". Stunde: ";
            } else {
                description += fromTo.getFirstLessonNumber() + ".-" + fromTo.getLastLessonNumber() + ". Stunde: ";
            }

            Teacher teacher = TeachersContentHelper.getTeacher(context, fromTo.getTeacher());

            SubjectFactory subjectFactory = new SubjectFactory();

            if (subjectFactory.isMultiTeacherSubject(representation.getSubject())) {
                if (teacher != null && teacher.getLastName() != null &&
                        !teacher.getLastName().equals("")) {
                    description += representation.getSubject().getFullName() + " (" + teacher.getLastName() + ")";
                } else {
                    description += representation.getSubject().getFullName() + " (" + fromTo.getTeacher() + ")";
                }
            } else {
                description += representation.getSubject().getFullName();
            }

            if (representation.getTo().equals(FromTo.ELIMINATION)) {
                description += " - Entfall";
            } else {
                description += " - Vertretung";
            }

            if (!TextUtils.isEmpty(representation.getDescription())) {
                description += " (" + representation.getDescription() + ")";
            }

            return description;
        }
    }
}