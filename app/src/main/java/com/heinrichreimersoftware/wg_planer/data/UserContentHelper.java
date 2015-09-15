package com.heinrichreimersoftware.wg_planer.data;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.heinrichreimersoftware.wg_planer.structure.User;

import java.util.ArrayList;
import java.util.List;

public class UserContentHelper {
    public static List<User> getUsers(@NonNull Context context) {
        List<User> users = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(
                UserContract.CONTENT_URI,
                null,
                null,
                null,
                null);

        if (cursor == null) return new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                User user = User.fromCursor(context, cursor);
                users.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return users;
    }

    public static User getUser(@NonNull Context context) {
        Cursor cursor = context.getContentResolver().query(
                UserContract.CONTENT_URI,
                null,
                null,
                null,
                null);

        if (cursor == null) return null;
        if (cursor.moveToFirst()) {
            do {
                User user = User.fromCursor(context, cursor);
                if (user != null) {
                    cursor.close();
                    return user;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return null;
    }

    public static int clearUsers(@NonNull Context context) {
        return context.getContentResolver().delete(UserContract.CONTENT_URI, null, null);
    }
}
