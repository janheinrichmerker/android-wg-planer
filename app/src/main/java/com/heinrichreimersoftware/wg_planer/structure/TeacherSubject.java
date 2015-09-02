package com.heinrichreimersoftware.wg_planer.structure;

import org.json.JSONException;
import org.json.JSONObject;

public class TeacherSubject extends Subject {

    public static final String KEY_SUBJECT = "subject";
    public static final String KEY_TEACHER = "teacher";
    public static final String KEY_ROOM = "room";

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

    public JSONObject toJsonObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put(KEY_SUBJECT, shorthand);
            obj.put(KEY_TEACHER, teacher);
            obj.put(KEY_ROOM, room);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public void fromJsonObject(JSONObject obj) {
        try {
            SubjectFactory subjectFactory = new SubjectFactory();
            Subject rawSubject = subjectFactory.fromShorthand(obj.getString(KEY_SUBJECT));

            this.setShorthand(rawSubject.getShorthand());
            this.setFullName(rawSubject.getFullName());
            this.setColor(rawSubject.getColor());
            this.setTeacher(obj.getString(KEY_TEACHER));
            this.setRoom(obj.getString(KEY_ROOM));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

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