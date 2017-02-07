package com.framgia.englishforkid.data.local;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import static com.framgia.englishforkid.data.local.DataHelper.FIELD_ID;
import static com.framgia.englishforkid.data.local.DataHelper.TABLE_NAME;

public class EnglishForKidProvider extends ContentProvider {
    public static final String PROVIDER_NAME = "com.framgia.EnglishForKidProvider";
    public static final String URL_DATA_BASE = "content://" + PROVIDER_NAME + "/datas";
    public static final Uri CONTENT_URI = Uri.parse(URL_DATA_BASE);
    public static final int URI_CODE = 1;
    public static final int URI_CODE_ID = 2;
    public static final UriMatcher sUriMatcher;
    static final String DATABASE_NAME = "English_For_Kid";
    static final int DATABASE_VERSION = 1;
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(PROVIDER_NAME, "datas", URI_CODE);
        sUriMatcher.addURI(PROVIDER_NAME, "datas/#", URI_CODE_ID);
    }
    private SQLiteDatabase mDatabase;

    public EnglishForKidProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (sUriMatcher.match(uri)) {
            case URI_CODE:
                count = mDatabase.delete(TABLE_NAME, selection, selectionArgs);
                break;
            case URI_CODE_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    count = mDatabase.delete(
                        TABLE_NAME,
                        FIELD_ID + "=" + id,
                        null);
                } else {
                    count = mDatabase.delete(
                        TABLE_NAME,
                        FIELD_ID + "=" + id
                            + " and " + selection,
                        selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = 0;
        switch (sUriMatcher.match(uri)) {
            case URI_CODE:
                rowID = mDatabase.insert(TABLE_NAME, null, values);
                if (rowID <= 0) return null;
                Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
                getContext().getContentResolver().notifyChange(_uri, null);
                return _uri;
            default:
                throw new SQLException("Failed to add a record into " + uri);
        }
    }

    @Override
    public boolean onCreate() {
        DataHelper dbHelper = new DataHelper(getContext());
        mDatabase = dbHelper.getWritableDatabase();
        if (mDatabase == null) return false;
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);
        switch (sUriMatcher.match(uri)) {
            case URI_CODE:
                break;
            case URI_CODE_ID:
                qb.appendWhere(FIELD_ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        Cursor cursor = qb.query(mDatabase, projection, selection, selectionArgs, null,
            null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count = 0;
        switch (sUriMatcher.match(uri)) {
            case URI_CODE:
                count = mDatabase.update(TABLE_NAME, values, selection, selectionArgs);
                break;
            case URI_CODE_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    count = mDatabase.update(TABLE_NAME,
                        values, FIELD_ID + "=" + id, null);
                } else {
                    count = mDatabase.update(TABLE_NAME,
                        values, FIELD_ID + "=" + id
                            + " and "
                            + selection,
                        selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
