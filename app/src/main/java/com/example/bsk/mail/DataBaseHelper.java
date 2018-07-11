package com.example.bsk.mail;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "mydatabase";
    public static final String PERSON_TABLE_NAME = "newtable";
    public static final String PERSON_COLUMN_ID = "_id";
    public static final String PERSON_COLUMN_TO = "togo";
    public static final String PERSON_COLUMN_FROM = "fromwho";
    public static final String PERSON_COLUMN_CONTENT = "content";
    public static final String PERSON_COLUMN_SUBJECT = "subject";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(" CREATE TABLE " + PERSON_TABLE_NAME + "(" +
                PERSON_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PERSON_COLUMN_TO + " TEXT NOT NULL, " +
                PERSON_COLUMN_FROM + " TEXT NOT NULL, " +
                PERSON_COLUMN_CONTENT + " TEXT NOT NULL, " +
                PERSON_COLUMN_SUBJECT + " TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PERSON_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
