package com.heinrichreimersoftware.wg_planer.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

public class TeachersContentProvider extends ContentProvider {

    public static final String PATH = "teachers";
    public static final int PATH_TOKEN = 500;
    public static final String PATH_FOR_ID = "teachers/*";
    public static final int PATH_FOR_ID_TOKEN = 600;
    public static final UriMatcher URI_MATCHER = buildUriMatcher();
    private TeachersDbHelper dbHelper;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = TeachersContract.AUTHORITY;
        matcher.addURI(authority, PATH, PATH_TOKEN);
        matcher.addURI(authority, PATH_FOR_ID, PATH_FOR_ID_TOKEN);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new TeachersDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        final int match = URI_MATCHER.match(uri);
        switch (match) {
            case PATH_TOKEN:
                return TeachersContract.CONTENT_TYPE_DIR;
            case PATH_FOR_ID_TOKEN:
                return TeachersContract.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("URI " + uri + " is not supported.");
        }
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if (!dbHelper.tableExists(db)) {
            dbHelper.onCreate(db);
        }
        int match = URI_MATCHER.match(uri);
        switch (match) {
            case PATH_TOKEN: {
                SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
                builder.setTables(TeachersDbHelper.TEACHERS_TABLE_NAME);
                return builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
            }
            case PATH_FOR_ID_TOKEN: {
                int teachersId = (int) ContentUris.parseId(uri);
                SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
                builder.setTables(TeachersDbHelper.TEACHERS_TABLE_NAME);
                builder.appendWhere(TeachersDbHelper.TEACHERS_COL_ID + "=" + teachersId);
                return builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
            }
            default:
                return null;
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (!dbHelper.tableExists(db)) {
            dbHelper.onCreate(db);
        }
        int token = URI_MATCHER.match(uri);
        switch (token) {
            case (PATH_FOR_ID_TOKEN):
                long id = db.insert(TeachersDbHelper.TEACHERS_TABLE_NAME, null, values);
                if (id != -1) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                break;
            default:
                throw new UnsupportedOperationException("URI: " + uri + " not supported.");
        }
        return uri;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (!dbHelper.tableExists(db)) {
            dbHelper.onCreate(db);
        }
        int token = URI_MATCHER.match(uri);
        int numChanges = 0;
        switch (token) {
            case (PATH_TOKEN):
                for (ContentValues value : values) {
                    long id = db.insert(TeachersDbHelper.TEACHERS_TABLE_NAME, null, value);
                    if (id != -1) {
                        getContext().getContentResolver().notifyChange(uri, null);
                    } else {
                        numChanges++;
                    }
                }
                break;
            default:
                throw new UnsupportedOperationException("URI: " + uri + " not supported.");
        }
        return numChanges;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db;
        try {
            db = dbHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            return 0;
        }
        if (!dbHelper.tableExists(db)) {
            dbHelper.onCreate(db);
        }
        int token = URI_MATCHER.match(uri);
        int rowsDeleted;
        switch (token) {
            case (PATH_TOKEN):
                rowsDeleted = db.delete(TeachersDbHelper.TEACHERS_TABLE_NAME, selection, selectionArgs);
                break;
            case (PATH_FOR_ID_TOKEN):
                String idSelection = TeachersDbHelper.TEACHERS_COL_ID + "=" + uri.getLastPathSegment();
                if (!TextUtils.isEmpty(selection))
                    idSelection += " AND " + selection;
                rowsDeleted = db.delete(TeachersDbHelper.TEACHERS_TABLE_NAME, idSelection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        if (rowsDeleted != -1)
            getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}