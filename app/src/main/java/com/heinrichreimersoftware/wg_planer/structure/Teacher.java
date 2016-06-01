package com.heinrichreimersoftware.wg_planer.structure;

import com.heinrichreimer.inquiry.annotations.Column;
import com.heinrichreimer.inquiry.annotations.Table;
import com.heinrichreimersoftware.wg_planer.Constants;

@Table(Constants.DATABASE_TABLE_NAME_TEACHERS)
public class Teacher {
    @Column(Constants.DATABASE_COLUMN_NAME_TITLE)
    private String title;
    @Column(Constants.DATABASE_COLUMN_NAME_FIRST_NAME)
    private String firstName;
    @Column(Constants.DATABASE_COLUMN_NAME_LAST_NAME)
    private String lastName;
    @Column(Constants.DATABASE_COLUMN_NAME_SHORTHAND)
    private String shorthand;
    @Column(Constants.DATABASE_COLUMN_NAME_URL)
    private String url;
    @Column(Constants.DATABASE_COLUMN_NAME_IMG_URL)
    private String imgUrl;

    public Teacher() {
    }

    public Teacher(Builder builder) {
        title = builder.title;
        firstName = builder.firstName;
        lastName = builder.lastName;
        shorthand = builder.shorthand;
        url = builder.url;
        imgUrl = builder.imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
        return firstName + " " + lastName;
    }

    public String getShorthand() {
        return shorthand;
    }

    public void setShorthand(String shorthand) {
        this.shorthand = shorthand;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Teacher representation = (Teacher) o;

        if (!title.equals(representation.title)) return false;
        if (!firstName.equals(representation.firstName)) return false;
        if (!lastName.equals(representation.lastName)) return false;
        if (!shorthand.equals(representation.shorthand)) return false;
        if (!url.equals(representation.url)) return false;
        if (!imgUrl.equals(representation.imgUrl)) return false;
        return true;
    }

    public static class Builder {
        private String title;
        private String firstName;
        private String lastName;
        private String shorthand;
        private String url;
        private String imgUrl;

        public String title() {
            return title;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public String firstName() {
            return firstName;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public String lastName() {
            return lastName;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public String shorthand() {
            return shorthand;
        }

        public Builder shorthand(String shorthand) {
            this.shorthand = shorthand;
            return this;
        }

        public String url() {
            return url;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public String imgUrl() {
            return imgUrl;
        }

        public Builder imgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
            return this;
        }

        public Teacher build() {
            return new Teacher(this);
        }
    }
}