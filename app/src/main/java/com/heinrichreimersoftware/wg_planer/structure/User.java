package com.heinrichreimersoftware.wg_planer.structure;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Base64;

import com.google.gson.Gson;
import com.heinrichreimersoftware.wg_planer.Constants;
import com.heinrichreimersoftware.wg_planer.data.UserDbHelper;
import com.heinrichreimersoftware.wg_planer.utils.BitmapUtils;

import java.io.UnsupportedEncodingException;

public class User {

    private String username;
    private String password;
    private String imageUrl;
    private String title;
    private String firstName;
    private String lastName;
    private String company;
    private String birthday;
    private String nickname;
    private String schoolClass;
    private String street;
    private String zipCode;
    private String city;
    private String country;
    private String phone;
    private String mobilePhone;
    private String fax;
    private String mail;
    private String homepage;
    private String icq;
    private String jabber;
    private String msn;
    private String skype;
    private String oid;
    private String authToken;
    private Bitmap bitmap;

    public User() {
    }

    public User(String username,
                String password,
                String imageUrl,
                String title,
                String firstName,
                String lastName,
                String company,
                String birthday,
                String nickname,
                String schoolClass,
                String street,
                String zipCode,
                String city,
                String country,
                String phone,
                String mobilePhone,
                String fax,
                String mail,
                String homepage,
                String icq,
                String jabber,
                String msn,
                String skype,
                String oid,
                String authToken,
                Bitmap bitmap) {
        this.username = username;
        this.password = password;
        this.imageUrl = imageUrl;
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.company = company;
        this.birthday = birthday;
        this.nickname = nickname;
        this.schoolClass = schoolClass;
        this.street = street;
        this.zipCode = zipCode;
        this.city = city;
        this.country = country;
        this.phone = phone;
        this.mobilePhone = mobilePhone;
        this.fax = fax;
        this.mail = mail;
        this.homepage = homepage;
        this.icq = icq;
        this.jabber = jabber;
        this.msn = msn;
        this.skype = skype;
        this.oid = oid;
        this.authToken = authToken;
        this.bitmap = bitmap;
    }

    public static User fromCursor(Context context, Cursor curRepresentations) {
        String username = curRepresentations.getString(curRepresentations.getColumnIndex(UserDbHelper.USER_COL_USERNAME));
        String password = curRepresentations.getString(curRepresentations.getColumnIndex(UserDbHelper.USER_COL_PASSWORD));
        String imageUrl = curRepresentations.getString(curRepresentations.getColumnIndex(UserDbHelper.USER_COL_IMAGE_URL));
        String title = curRepresentations.getString(curRepresentations.getColumnIndex(UserDbHelper.USER_COL_TITLE));
        String firstName = curRepresentations.getString(curRepresentations.getColumnIndex(UserDbHelper.USER_COL_FIRST_NAME));
        String lastName = curRepresentations.getString(curRepresentations.getColumnIndex(UserDbHelper.USER_COL_LAST_NAME));
        String company = curRepresentations.getString(curRepresentations.getColumnIndex(UserDbHelper.USER_COL_COMPANY));
        String birthday = curRepresentations.getString(curRepresentations.getColumnIndex(UserDbHelper.USER_COL_BIRTHDAY));
        String nickname = curRepresentations.getString(curRepresentations.getColumnIndex(UserDbHelper.USER_COL_NICKNAME));
        String schoolClass = curRepresentations.getString(curRepresentations.getColumnIndex(UserDbHelper.USER_COL_SCHOOL_CLASS));
        String street = curRepresentations.getString(curRepresentations.getColumnIndex(UserDbHelper.USER_COL_STREET));
        String zipCode = curRepresentations.getString(curRepresentations.getColumnIndex(UserDbHelper.USER_COL_ZIP_CODE));
        String city = curRepresentations.getString(curRepresentations.getColumnIndex(UserDbHelper.USER_COL_CITY));
        String country = curRepresentations.getString(curRepresentations.getColumnIndex(UserDbHelper.USER_COL_COUNTRY));
        String phone = curRepresentations.getString(curRepresentations.getColumnIndex(UserDbHelper.USER_COL_PHONE));
        String mobilePhone = curRepresentations.getString(curRepresentations.getColumnIndex(UserDbHelper.USER_COL_MOBILE_PHONE));
        String fax = curRepresentations.getString(curRepresentations.getColumnIndex(UserDbHelper.USER_COL_FAX));
        String mail = curRepresentations.getString(curRepresentations.getColumnIndex(UserDbHelper.USER_COL_MAIL));
        String homepage = curRepresentations.getString(curRepresentations.getColumnIndex(UserDbHelper.USER_COL_HOMEPAGE));
        String icq = curRepresentations.getString(curRepresentations.getColumnIndex(UserDbHelper.USER_COL_ICQ));
        String jabber = curRepresentations.getString(curRepresentations.getColumnIndex(UserDbHelper.USER_COL_JABBER));
        String msn = curRepresentations.getString(curRepresentations.getColumnIndex(UserDbHelper.USER_COL_MSN));
        String skype = curRepresentations.getString(curRepresentations.getColumnIndex(UserDbHelper.USER_COL_SKYPE));
        String oid = curRepresentations.getString(curRepresentations.getColumnIndex(UserDbHelper.USER_COL_OID));
        String authToken = curRepresentations.getString(curRepresentations.getColumnIndex(UserDbHelper.USER_COL_AUTH_TOKEN));
        String bitmapFileName = curRepresentations.getString(curRepresentations.getColumnIndex(UserDbHelper.USER_COL_LOCAL_IMAGE_FILE_NAME));

        Bitmap bitmap = BitmapUtils.loadBitmapFromSd(context, bitmapFileName);

        return new User(username,
                password,
                imageUrl,
                title,
                firstName,
                lastName,
                company,
                birthday,
                nickname,
                schoolClass,
                street,
                zipCode,
                city,
                country,
                phone,
                mobilePhone,
                fax,
                mail,
                homepage,
                icq,
                jabber,
                msn,
                skype,
                oid,
                authToken,
                bitmap);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public String getName() {
        if (getNickname() != null && !getNickname().equals("")) {
            return getNickname();
        }
        if (getFirstName() != null && !getFirstName().equals("") && getLastName() != null && !getLastName().equals("")) {
            return getFirstName() + " " + getLastName();
        }
        return getUsername();
    }

    public String getDescription() {
        if (getNickname() != null && !getNickname().equals("")) {
            if (getFirstName() != null && !getFirstName().equals("") && getLastName() != null && !getLastName().equals("")) {
                return getFirstName() + " " + getLastName();
            } else {
                return getUsername();
            }
        }
        if (getFirstName() != null && !getFirstName().equals("") && getLastName() != null && !getLastName().equals("")) {
            return getUsername();
        }
        return null;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(String schoolClass) {
        this.schoolClass = schoolClass;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getIcq() {
        return icq;
    }

    public void setIcq(String icq) {
        this.icq = icq;
    }

    public String getJabber() {
        return jabber;
    }

    public void setJabber(String jabber) {
        this.jabber = jabber;
    }

    public String getMsn() {
        return msn;
    }

    public void setMsn(String msn) {
        this.msn = msn;
    }

    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String toJson() {
        return new Gson().toJson(new AuthToken(this.username, this.password), AuthToken.class);
    }

    public void fromJson(String json) {
        Gson gson = new Gson();
        AuthToken obj = gson.fromJson(json, AuthToken.class);
        this.username = obj.getUsername();
        this.password = obj.getPassword();
    }

    public String toString() {
        return "User: '" + username + "'";
    }

    public String toBase64() {
        return Base64.encodeToString(this.toJson().getBytes(), Base64.DEFAULT);
    }

    public void fromBase64(String base64) {
        byte[] data = Base64.decode(base64, Base64.DEFAULT);
        String json = "";
        try {
            json = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (!json.equals("")) {
            this.fromJson(json);
        }
    }

    public ContentValues getContentValues(Context context) {
        ContentValues values = new ContentValues();
        values.put(UserDbHelper.USER_COL_USERNAME, username);
        values.put(UserDbHelper.USER_COL_PASSWORD, password);
        values.put(UserDbHelper.USER_COL_IMAGE_URL, imageUrl);
        values.put(UserDbHelper.USER_COL_TITLE, title);
        values.put(UserDbHelper.USER_COL_FIRST_NAME, firstName);
        values.put(UserDbHelper.USER_COL_LAST_NAME, lastName);
        values.put(UserDbHelper.USER_COL_COMPANY, company);
        values.put(UserDbHelper.USER_COL_BIRTHDAY, birthday);
        values.put(UserDbHelper.USER_COL_NICKNAME, nickname);
        values.put(UserDbHelper.USER_COL_SCHOOL_CLASS, schoolClass);
        values.put(UserDbHelper.USER_COL_STREET, street);
        values.put(UserDbHelper.USER_COL_ZIP_CODE, zipCode);
        values.put(UserDbHelper.USER_COL_CITY, city);
        values.put(UserDbHelper.USER_COL_COUNTRY, country);
        values.put(UserDbHelper.USER_COL_PHONE, phone);
        values.put(UserDbHelper.USER_COL_MOBILE_PHONE, mobilePhone);
        values.put(UserDbHelper.USER_COL_FAX, fax);
        values.put(UserDbHelper.USER_COL_MAIL, mail);
        values.put(UserDbHelper.USER_COL_HOMEPAGE, homepage);
        values.put(UserDbHelper.USER_COL_ICQ, icq);
        values.put(UserDbHelper.USER_COL_JABBER, jabber);
        values.put(UserDbHelper.USER_COL_MSN, msn);
        values.put(UserDbHelper.USER_COL_SKYPE, skype);
        values.put(UserDbHelper.USER_COL_OID, oid);
        values.put(UserDbHelper.USER_COL_AUTH_TOKEN, authToken);
        values.put(UserDbHelper.USER_COL_LOCAL_IMAGE_FILE_NAME, Constants.PROFILE_IMAGE);

        BitmapUtils.saveBitmapToSd(context, this.bitmap, Constants.PROFILE_IMAGE);

        return values;
    }
}