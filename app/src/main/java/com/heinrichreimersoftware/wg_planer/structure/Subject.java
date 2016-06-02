package com.heinrichreimersoftware.wg_planer.structure;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.heinrichreimer.inquiry.annotations.Column;
import com.heinrichreimer.inquiry.annotations.Table;
import com.heinrichreimersoftware.wg_planer.Constants;

@Table(Constants.DATABASE_TABLE_NAME_SUBJECTS)
public class Subject {
    @Column(Constants.DATABASE_COLUMN_NAME_SHORTHAND)
    @SerializedName(Constants.JSON_KEY_SHORTHAND)
    //TODO Change this from "subject" to "shorthand" in API
    protected String shorthand;
    @Column(Constants.DATABASE_COLUMN_NAME_FULL_NAME)
    @Expose(serialize = false, deserialize = false) //TODO Add this to API
    protected String fullName;
    @Column(Constants.DATABASE_COLUMN_NAME_COLOR)
    @Expose(serialize = false, deserialize = false) //TODO Add this to API
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