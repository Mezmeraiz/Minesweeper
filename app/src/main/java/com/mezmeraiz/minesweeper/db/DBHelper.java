package com.mezmeraiz.minesweeper.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by pc on 26.10.2016.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Minesweeper_Database";
    public static final String SCORE_TABLE_NAME = "ScoreTable";
    public static final String NAME = "name";
    public static final String SCORE = "score";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_SCORE_TABLE = "CREATE TABLE " + SCORE_TABLE_NAME + "(" +
            NAME + " text," +
            SCORE + " integer);";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SCORE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
