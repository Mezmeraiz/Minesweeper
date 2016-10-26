package com.mezmeraiz.minesweeper.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.mezmeraiz.minesweeper.R;
import java.util.Random;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnClickListener{

    public final static String NAME_KEY  = "NAME_KEY";
    public final static int REQUEST_CODE = 11;
    private GridLayout mGridLayout;
    private int mFieldSize;
    private int[][] mMineField;
    private int mScore;
    private String mName;
    private TextView mScoreTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mName = getIntent().getStringExtra(NAME_KEY);
        mFieldSize = new Random().nextInt(11) + 10;
        mMineField = new int[mFieldSize][mFieldSize];
        findViewById(R.id.imageView_score).setOnClickListener(this);
        mScoreTextView = (TextView)findViewById(R.id.textView_score);
        mGridLayout = (GridLayout) findViewById(R.id.gridLayout);
        initGridLayout();
        addMines();
        addNumbers();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            if(data.getAction().equals(RecordActivity.EXIT_ACTION)){
                finish();
            }else if(data.getAction().equals(RecordActivity.NEW_GAME_ACTION)){
                Intent intent = new Intent(this, this.getClass());
                intent.putExtra(NAME_KEY, mName);
                finish();
                startActivity(intent);
            }else if(data.getAction().equals(RecordActivity.REPEAT_ACTION)){
                mScoreTextView.setText(String.valueOf(mScore = 0));
                initGridLayout();
                for(int row = 0; row < mFieldSize; row++){
                    for(int column = 0; column < mFieldSize; column++) {
                        if(mMineField[row][column] == 100500){
                            mMineField[row][column] = 0;
                        }
                    }
                }
            }
        }
    }

    private void addMines(){
        Random random = new Random();
        for(int i = 0; i < mFieldSize;){
            int column = random.nextInt(mFieldSize);
            int row = random.nextInt(mFieldSize);
            if(mMineField[row][column] != -1){
                mMineField[row][column] = -1;
                i++;
            }
        }
    }

    private void addNumbers(){
        for(int row = 0; row < mFieldSize; row++){
            for(int column = 0; column < mFieldSize; column++){
                int mineCount = 0;
                if(column < mFieldSize - 1 && mMineField[row][column + 1] < 0){
                    mineCount++;
                }
                if(row < mFieldSize - 1 && column < mFieldSize - 1 && mMineField[row + 1][column + 1] < 0){
                    mineCount++;
                }
                if(row > 0 && column < mFieldSize - 1 && mMineField[row - 1][column + 1] < 0){
                    mineCount++;
                }
                if(column > 0 && mMineField[row][column - 1] < 0){
                    mineCount++;
                }
                if(row < mFieldSize - 1 && column > 0 && mMineField[row + 1][column - 1] < 0){
                    mineCount++;
                }
                if(row > 0 && column > 0 && mMineField[row - 1][column - 1] < 0){
                    mineCount++;
                }
                if(row < mFieldSize - 1 && mMineField[row + 1][column] < 0){
                    mineCount++;
                }
                if(row > 0 && mMineField[row - 1][column] < 0){
                    mineCount++;
                }
                if(mMineField[row][column] != - 1){
                    mMineField[row][column] = mineCount;
                }
            }
        }
    }

    private void initGridLayout(){
        mGridLayout.removeAllViews();
        mGridLayout.setColumnCount(mFieldSize);
        mGridLayout.setRowCount(mFieldSize);
        mGridLayout.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.LayoutParams layoutParams = mGridLayout.getLayoutParams();
                int size = Math.min(mGridLayout.getWidth(), mGridLayout.getHeight());
                layoutParams.width = size;
                layoutParams.height = size;
                mGridLayout.setLayoutParams(layoutParams);
                for(int row = 0; row < mFieldSize; row++){
                    for(int column = 0; column < mFieldSize; column++){
                        GridLayout.LayoutParams gridLayoutParams = new GridLayout.LayoutParams();
                        TextView textView = new TextView(PlayActivity.this);
                        textView.setTag(R.integer.column, column);
                        textView.setTag(R.integer.row, row);
                        textView.setTextSize(10);
                        textView.setGravity(Gravity.CENTER);
                        textView.setText("");
                        textView.setBackgroundColor(Color.parseColor("#ffffff"));
                        textView.setOnClickListener(PlayActivity.this);
                        gridLayoutParams.width = 0;
                        gridLayoutParams.height = 0;
                        gridLayoutParams.setMargins(1,1,1,1);
                        gridLayoutParams.columnSpec = GridLayout.spec(column, 1f);
                        gridLayoutParams.rowSpec = GridLayout.spec(row, 1f);
                        mGridLayout.addView(textView, gridLayoutParams);
                    }
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageView_score:
                Intent intent = new Intent(this, RecordActivity.class);
                intent.putExtra(RecordActivity.UPDATE, false);
                startActivity(intent);
                break;
            default:
                int row = (int) v.getTag(R.integer.row);
                int column = (int) v.getTag(R.integer.column);
                int value = mMineField[row][column];
                if(value == 100500){
                    return;
                }else if(value > 0){
                    ((TextView)v).setText(String.valueOf(value));
                    mMineField[row][column] = 100500;
                    mScoreTextView.setText(String.valueOf(++mScore));
                    if(mScore >= mFieldSize * mFieldSize - mFieldSize){
                        win();
                    }
                }else if(value == 0){
                    openEmptyCell(row, column);
                    if(mScore >= mFieldSize * mFieldSize - mFieldSize){
                        win();
                    }
                }else if(value < 0){
                    boom();
                }
                break;
        }
    }

    private void openEmptyCell(int row, int column){
        ((TextView) mGridLayout.getChildAt(getViewIndex(row,column))).setText("0");
        mMineField[row][column] = 100500;
        mScoreTextView.setText(String.valueOf(++mScore));;
        if(column < mFieldSize - 1 && mMineField[row][column + 1] == 0){
            openEmptyCell(row, column + 1);
        }
        if(column > 0 && mMineField[row][column - 1] == 0){
            openEmptyCell(row, column - 1);
        }
        if(row < mFieldSize - 1 && mMineField[row + 1][column] == 0){
            openEmptyCell(row + 1, column);
        }
        if(row > 0 && mMineField[row - 1][column] == 0){
            openEmptyCell(row - 1, column);
        }
    }

    private void boom(){
        showDialog("Проиграл!");
    }

    private void win(){
        showDialog("Выиграл!");
    }

    private int getViewIndex(int row, int coulumn){
        return (row * mFieldSize + coulumn);
    }

    private void showDialog(String text){
        new AlertDialog.Builder(this)
                .setMessage(text)
                .setCancelable(false)
                .setNegativeButton("OK", this)
                .create()
                .show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        Intent intent = new Intent(this, RecordActivity.class);
        intent.putExtra(RecordActivity.UPDATE, true);
        intent.putExtra(RecordActivity.NAME, mName);
        intent.putExtra(RecordActivity.SCORE, mScore);
        startActivityForResult(intent, REQUEST_CODE);
    }
}
