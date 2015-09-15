package com.heinrichreimersoftware.wg_planer.structure;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.heinrichreimersoftware.wg_planer.data.TeachersDbHelper;

public class Teacher {
    public String shorthand = "";
    public String firstName = "";
    public String lastName = "";
    public String webLink = "";

    public Teacher(String shorthand, String firstName, String lastName, String webLink) {
        this.shorthand = shorthand;
        this.firstName = firstName;
        this.lastName = lastName;
        this.webLink = webLink;
    }

    public Teacher() {
    }

    public static Teacher fromCursor(Cursor curRepresentations) {
        String shorthand = curRepresentations.getString(curRepresentations.getColumnIndex(TeachersDbHelper.TEACHERS_COL_SHORTHAND));
        String firstName = curRepresentations.getString(curRepresentations.getColumnIndex(TeachersDbHelper.TEACHERS_COL_FIRST_NAME));
        String lastName = curRepresentations.getString(curRepresentations.getColumnIndex(TeachersDbHelper.TEACHERS_COL_LAST_NAME));
        String webLink = curRepresentations.getString(curRepresentations.getColumnIndex(TeachersDbHelper.TEACHERS_COL_WEB_LINK));

        return new Teacher(shorthand,
                firstName,
                lastName,
                webLink);
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Teacher representation = (Teacher) o;

        if (!shorthand.equals(representation.shorthand)) return false;
        if (!firstName.equals(representation.firstName)) return false;
        if (!lastName.equals(representation.lastName)) return false;
        if (!webLink.equals(representation.webLink)) return false;
        return true;
    }

    public String getShorthand() {
        return shorthand;
    }

    public void setShorthand(String shorthand) {
        this.shorthand = shorthand;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        String fullName = "";
        if (!TextUtils.isEmpty(firstName)) {
            fullName += firstName;
            if (!TextUtils.isEmpty(lastName)) {
                fullName += " ";
            }
        }
        if (!TextUtils.isEmpty(lastName)) {
            fullName += lastName;
        }
        return fullName;
    }

    public String getWebLink() {
        return webLink;
    }

    public void setWebLink(String webLink) {
        this.webLink = webLink;
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(TeachersDbHelper.TEACHERS_COL_SHORTHAND, shorthand);
        values.put(TeachersDbHelper.TEACHERS_COL_FIRST_NAME, firstName);
        values.put(TeachersDbHelper.TEACHERS_COL_LAST_NAME, lastName);
        values.put(TeachersDbHelper.TEACHERS_COL_WEB_LINK, webLink);
        return values;
    }

    public String toString() {
        return this.getShorthand() + ": " + this.getFirstName() + " " + this.getLastName() + ": " + this.getWebLink();
    }
}