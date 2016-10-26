package com.mezmeraiz.minesweeper.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.mezmeraiz.minesweeper.R;

/**
 * Created by pc on 26.10.2016.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.button_login).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, PlayActivity.class);
        intent.putExtra(PlayActivity.NAME_KEY, ((EditText)findViewById(R.id.editText_login)).getText().toString());
        startActivity(intent);
        finish();
    }
}
