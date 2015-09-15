package com.heinrichreimersoftware.wg_planer.structure;

public class Subject {
    protected String shorthand;
    protected String fullName;
    protected int color;

    public Subject(String shorthand, String fullName, int color) {
        this.shorthand = shorthand;
        this.fullName = fullName;
        this.color = color;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getShorthand() {
        return shorthand;
    }

    public void setShorthand(String shorthand) {
        this.shorthand = shorthand;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Subject subject = (Subject) o;

        if (!shorthand.equals(subject.shorthand)) return false;
        if (!fullName.equals(subject.fullName)) return false;
        if (color != subject.color) return false;
        return true;
    }


    @Override
    public String toString() {
        return fullName + "(" + shorthand + "): " + color;
    }
}