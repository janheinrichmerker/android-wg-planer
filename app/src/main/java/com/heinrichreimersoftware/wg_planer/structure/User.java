package com.heinrichreimersoftware.wg_planer.structure;

import com.afollestad.inquiry.annotations.Column;
import com.heinrichreimersoftware.wg_planer.Constants;

public class User {
    @Column(name = Constants.DATABASE_COLUMN_NAME_ID, primaryKey = true, notNull = true, autoIncrement = true)
    private long id;

    @Column(name = Constants.DATABASE_COLUMN_NAME_USERNAME)
    private String username;
    @Column(name = Constants.DATABASE_COLUMN_NAME_IMG_URL)
    private String imgUrl;
    @Column(name = Constants.DATABASE_COLUMN_NAME_FULL_NAME)
    private String fullName;
    @Column(name = Constants.DATABASE_COLUMN_NAME_NICKNAME)
    private String nickname;
    @Column(name = Constants.DATABASE_COLUMN_NAME_SCHOOL_CLASSES)
    private String[] schoolClasses; //FIXME
    @Column(name = Constants.DATABASE_COLUMN_NAME_EMAIL)
    private String email;

    private User() {
    }

    private User(Builder builder) {
        username = builder.username;
        imgUrl = builder.imgUrl;
        fullName = builder.fullName;
        nickname = builder.nickname;
        schoolClasses = builder.schoolClasses;
        email = builder.email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imageUrl) {
        this.imgUrl = imageUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String[] getSchoolClasses() {
        return schoolClasses;
    }

    public void setSchoolClasses(String[] schoolClasses) {
        this.schoolClasses = schoolClasses;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static class Builder {
        private String username;
        private String imgUrl;
        private String fullName;
        private String nickname;
        private String[] schoolClasses;
        private String email;

        public String username() {
            return username;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public String imgUrl() {
            return imgUrl;
        }

        public Builder imgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
            return this;
        }

        public String fullName() {
            return fullName;
        }

        public Builder fullName(String displayName) {
            this.fullName = displayName;
            return this;
        }

        public String nickname() {
            return nickname;
        }

        public Builder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public String[] schoolClasses() {
            return schoolClasses;
        }

        public Builder schoolClasses(String[] schoolClasses) {
            this.schoolClasses = schoolClasses;
            return this;
        }

        public String email() {
            return email;
        }

        public Builder email(String mail) {
            this.email = mail;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}