package com.heinrichreimersoftware.wg_planer.content;

import android.content.Context;
import android.support.annotation.NonNull;

import com.afollestad.inquiry.Inquiry;
import com.heinrichreimersoftware.wg_planer.Constants;
import com.heinrichreimersoftware.wg_planer.structure.User;

public class UserContentHelper {

    public static User[] getUsers(@NonNull Context context) {
        Inquiry.init(context, Constants.DATABASE_NAME, Constants.DATABASE_VERSION);

        User[] users = Inquiry.get()
                .selectFrom(Constants.DATABASE_TABLE_NAME_USERS, User.class)
                .all();

        Inquiry.deinit();

        return users;
    }

    public static User getUser(@NonNull Context context) {
        Inquiry.init(context, Constants.DATABASE_NAME, Constants.DATABASE_VERSION);

        User user = Inquiry.get()
                .selectFrom(Constants.DATABASE_TABLE_NAME_USERS, User.class)
                .one();

        Inquiry.deinit();

        return user;
    }

    public static void addUser(@NonNull Context context, User user) {
        Inquiry.init(context, Constants.DATABASE_NAME, Constants.DATABASE_VERSION);

        Inquiry.get()
                .insertInto(Constants.DATABASE_TABLE_NAME_USERS, User.class)
                .values(user)
                .run();

        Inquiry.deinit();
    }

    public static void clearUsers(@NonNull Context context) {
        Inquiry.init(context, Constants.DATABASE_NAME, Constants.DATABASE_VERSION);

        Inquiry.get()
                .deleteFrom(Constants.DATABASE_TABLE_NAME_USERS, User.class)
                .run();

        Inquiry.deinit();
    }
}
