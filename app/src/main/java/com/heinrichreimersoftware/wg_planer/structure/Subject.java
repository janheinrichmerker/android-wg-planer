package com.heinrichreimersoftware.wg_planer.structure;

import com.afollestad.inquiry.annotations.Column;
import com.heinrichreimersoftware.wg_planer.Constants;

public class Subject {
    @Column(name = Constants.DATABASE_COLUMN_NAME_ID, primaryKey = true, notNull = true, autoIncrement = true)
    protected long id;

    @Column(name = Constants.DATABASE_COLUMN_NAME_SHORTHAND)
    protected String shorthand;
    @Column(name = Constants.DATABASE_COLUMN_NAME_FULL_NAME)
    protected String fullName;
    @Column(name = Constants.DATABASE_COLUMN_NAME_COLOR)
    protected int color;

    public Subject() {
    }

    public Subject(Builder builder) {
        shorthand = builder.shorthand;
        fullName = builder.fullName;
        color = builder.color;
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

    public static class Builder {
        protected String shorthand;
        protected String fullName;
        protected int color;

        public String shorthand() {
            return shorthand;
        }

        public Builder shorthand(String shorthand) {
            this.shorthand = shorthand;
            return this;
        }

        public String fullName() {
            return fullName;
        }

        public Builder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public int color() {
            return color;
        }

        public Builder color(int color) {
            this.color = color;
            return this;
        }

        public Subject build() {
            return new Subject(this);
        }
    }
}