package com.mezmeraiz.minesweeper.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.mezmeraiz.minesweeper.R;
import com.mezmeraiz.minesweeper.db.DB;
import com.mezmeraiz.minesweeper.db.DBHelper;
import com.mezmeraiz.minesweeper.loaders.ScoreCursorLoader;

/**
 * Created by pc on 26.10.2016.
 */

public class RecordActivity extends AppCompatActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>{

    public final static String UPDATE  = "UPDATE";
    public final static String NAME  = "NAME";
    public final static String SCORE  = "SCORE";
    public final static String NEW_GAME_ACTION  = "NEW_GAME_ACTION";
    public final static String EXIT_ACTION  = "EXIT_ACTION";
    public final static String REPEAT_ACTION  = "REPEAT_ACTION";
    private Button mExitButton, mNewButton, mRepeatButton;
    private TextView mTopTextView;
    private DB mDB;
    private final int SCORE_LOADER = 7;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        mExitButton = (Button) findViewById(R.id.button_exit);
        mNewButton = (Button) findViewById(R.id.button_new_game);
        mRepeatButton = (Button) findViewById(R.id.button_repeat);
        mTopTextView = (TextView)findViewById(R.id.textView_score);
        mDB = new DB().open(this);
        if(getIntent().getBooleanExtra(UPDATE, false)){
            mExitButton.setVisibility(View.VISIBLE);
            mNewButton.setVisibility(View.VISIBLE);
            mRepeatButton.setVisibility(View.VISIBLE);
            mExitButton.setOnClickListener(this);
            mNewButton.setOnClickListener(this);
            mRepeatButton.setOnClickListener(this);
        }
        getSupportLoaderManager().initLoader(SCORE_LOADER, null, this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_repeat:
                setResult(RESULT_OK, new Intent(REPEAT_ACTION));
                finish();
                break;
            case R.id.button_exit:
                setResult(RESULT_OK, new Intent(EXIT_ACTION));
                finish();
                break;
            case R.id.button_new_game:
                setResult(RESULT_OK, new Intent(NEW_GAME_ACTION));
                finish();
                break;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(getIntent().getBooleanExtra(UPDATE, false)){
            return new ScoreCursorLoader(this, mDB, getIntent().getStringExtra(NAME), getIntent().getIntExtra(SCORE, 0), true);
        }else{
            return new ScoreCursorLoader(this, mDB);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data.moveToFirst()){
            String result = "";
            do{
                result = String.format("%s - %d\n", data.getString(data.getColumnIndex(DBHelper.NAME)),
                        data.getInt(data.getColumnIndex(DBHelper.SCORE))) + result;
            }while(data.moveToNext());
            mTopTextView.setText(result);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mDB != null){
            mDB.close();
        }
    }

}
