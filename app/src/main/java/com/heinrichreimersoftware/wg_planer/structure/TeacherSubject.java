package com.heinrichreimersoftware.wg_planer.structure;

public class TeacherSubject extends Subject {

    protected String teacher;
    protected String room;

    public TeacherSubject(String shorthand, String fullName, int color, String teacher, String room) {
        super(shorthand, fullName, color);
        this.shorthand = shorthand;
        this.fullName = fullName;
        this.color = color;
        this.teacher = teacher;
        this.room = room;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
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


    @Override
    public String toString() {
        return fullName + "(" + shorthand + "): " + teacher + " in " + room;
    }
}