package com.framgia.englishforkid.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.framgia.englishforkid.data.local.EnglishForKidProvider.DATABASE_NAME;
import static com.framgia.englishforkid.data.local.EnglishForKidProvider.DATABASE_VERSION;

public class DataHelper extends SQLiteOpenHelper {
    public static final String FIELD_ID = "_id";
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_URL_PAGE_VIDEO = "url_page_video";
    public static final String FIELD_URL_IMG = "url_image";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_URL_VIDEO = "url_video";
    public static final int TYPE_SONG = 0;
    public static final int TYPE_STORY = 1;
    public static final String TABLE_NAME = "english_for_kid";
    public static final String COMMAND_CREATE_DB = " CREATE TABLE " + TABLE_NAME + " ("
        + FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
        + FIELD_TITLE + " TEXT NOT NULL, "
        + FIELD_URL_PAGE_VIDEO + " TEXT NOT NULL, "
        + FIELD_URL_VIDEO + " TEXT, "
        + FIELD_URL_IMG + " TEXT NOT NULL, "
        + FIELD_TYPE + " INTERGER NOT NULL);";

    public DataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(COMMAND_CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}

