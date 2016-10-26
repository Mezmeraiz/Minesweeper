package com.mezmeraiz.minesweeper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by pc on 26.10.2016.
 */

public class DB {

    private SQLiteDatabase mSQLiteDatabase;
    private DBHelper mDBHelper;

    public DB open(Context context){
        if (mDBHelper == null || mSQLiteDatabase == null){
            mDBHelper = new DBHelper(context);
            mSQLiteDatabase = mDBHelper.getWritableDatabase();
        }
        return this;
    }

    public Cursor getCursor(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy){
        if (mSQLiteDatabase != null){
            return mSQLiteDatabase.query(table,columns,selection,selectionArgs,groupBy,having,orderBy);
        }else{
            return null;
        }
    }

    public void checkAndUpdate(String name, int score){
        Cursor cursor = getCursor(DBHelper.SCORE_TABLE_NAME,null,null,null,null,null,DBHelper.SCORE);
        if (cursor.getCount() < 10){
            addPlayer(name, score);
        }else{
            int min = -1;
            if(cursor.moveToFirst()){
                min = cursor.getInt(cursor.getColumnIndex(DBHelper.SCORE));
                do{
                    int nextScore = cursor.getInt(cursor.getColumnIndex(DBHelper.SCORE));
                    if(nextScore < min){
                        min = nextScore;
                    }
                }while (cursor.moveToNext());
                if(score > min){
                    removePlayer(min);
                    addPlayer(name, score);
                }
            }
        }
    }

    public void addPlayer(String name, int score){
        Cursor cursor = getCursor(DBHelper.SCORE_TABLE_NAME,null,null,null,null,null,DBHelper.SCORE);
        if(cursor.moveToFirst()){
            do{
                if(name.equals(cursor.getString(cursor.getColumnIndex(DBHelper.NAME)))){
                    if(score > cursor.getInt(cursor.getColumnIndex(DBHelper.SCORE))){
                        mSQLiteDatabase.delete(DBHelper.SCORE_TABLE_NAME,"name='"+name+"'" , null);
                    }else{
                        return;
                    }
                }
            }while (cursor.moveToNext());
        }
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.NAME,name);
        cv.put(DBHelper.SCORE,score);
        mSQLiteDatabase.insert(DBHelper.SCORE_TABLE_NAME,null,cv);
    }

    public void removePlayer(int score){
        mSQLiteDatabase.delete(DBHelper.SCORE_TABLE_NAME,DBHelper.SCORE + "=" + score, null);
    }

    public void close(){
        if(mSQLiteDatabase!=null)
            mSQLiteDatabase.close();
        if(mDBHelper!=null)
            mDBHelper.close();
    }
}
