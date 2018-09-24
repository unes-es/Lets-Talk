package com.applications.coffee.letstalkrussian_free;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by essabri on 07/12/2017.
 */

class DbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "letsSpeakKoreanPhraseBook.db";

    private static final String SQL_CREATE_PHRASES =
            "CREATE TABLE " + Data.PhraseTable.TABLE_NAME + " (" +
                    /*Data.PhraseTable._ID + " INTEGER PRIMARY KEY," +*/
                    Data.PhraseTable.ID + " INTEGER," +
                    /*Data.PhraseTable.ORIGIN + " TEXT," +*/
                    Data.PhraseTable.TARGET + " TEXT," +
                    Data.PhraseTable.PRONUNCIATION + " TEXT," +
                    Data.PhraseTable.CATEGORY + " INTEGER," +
                    Data.PhraseTable.IS_FAVORITE + " INTEGER)";

    private static final String SQL_DELETE_PHRASES = "DROP TABLE IF EXISTS " + Data.PhraseTable.TABLE_NAME;


    private static final String SQL_CREATE_CATEGORIES =
            "CREATE TABLE " + Data.CategoryTable.TABLE_NAME + " (" +
                    Data.CategoryTable._ID + " INTEGER PRIMARY KEY," +
                    Data.CategoryTable.ID + " INTEGER," +
                    //Data.CategoryTable.NAME + " TEXT,"+
                    Data.CategoryTable.TRANSLATEDNAME + " TEXT)";

    private static final String SQL_DELETE_CATEGORIES = "DROP TABLE IF EXISTS " + Data.CategoryTable.TABLE_NAME;

    DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PHRASES);
        db.execSQL(SQL_CREATE_CATEGORIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_PHRASES);
        db.execSQL(SQL_DELETE_CATEGORIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}