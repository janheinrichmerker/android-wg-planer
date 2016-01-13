package com.heinrichreimersoftware.wg_planer.structure;

import com.afollestad.inquiry.annotations.Column;
import com.heinrichreimersoftware.wg_planer.Constants;

public class TeacherSubject extends Subject {
    @Reference(columnName = Constants.DATABASE_COLUMN_NAME_TEACHER,
            tableName = Constants.DATABASE_TABLE_NAME_TEACHERS)
    protected Teacher teacher; //FIXME
    @Column(name = "room")
    protected String room;

    public TeacherSubject() {
    }

    public TeacherSubject(Builder builder) {
        super(builder);
        teacher = builder.teacher;
        room = builder.room;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!super.equals(o)) return false;

        TeacherSubject subject = (TeacherSubject) o;

        if (!teacher.equals(subject.teacher)) return false;
        if (!room.equals(subject.room)) return false;
        return true;
    }

    public static class Builder extends Subject.Builder {
        protected Teacher teacher;
        protected String room;

        public Teacher teacher() {
            return teacher;
        }

        public Builder teacher(Teacher teacher) {
            this.teacher = teacher;
            return this;
        }

        public String room() {
            return room;
        }

        public Builder room(String room) {
            this.room = room;
            return this;
        }

        public TeacherSubject build() {
            return new TeacherSubject(this);
        }
    }
}