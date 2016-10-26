package com.mezmeraiz.minesweeper.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import com.mezmeraiz.minesweeper.db.DB;
import com.mezmeraiz.minesweeper.db.DBHelper;

/**
 * Created by pc on 26.10.2016.
 */

public class ScoreCursorLoader extends CursorLoader {

    private DB mDB;
    private String mName;
    private int mScore;
    private boolean mUpdate;


    public ScoreCursorLoader(Context context, DB db) {
        super(context);
        mDB = db;
    }

    public ScoreCursorLoader(Context context, DB db, String name, int score, boolean update){
        super(context);
        mDB = db;
        mName = name;
        mScore = score;
        mUpdate = update;
    }

    @Override
    public Cursor loadInBackground() {
        if(mUpdate){
            mDB.checkAndUpdate(mName, mScore);
        }
        return mDB.getCursor(DBHelper.SCORE_TABLE_NAME,null,null,null,null,null,DBHelper.SCORE);
    }
}
