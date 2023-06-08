package com.example.minesweeper;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private RadioGroup gridSizeRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        gridSizeRadioGroup = findViewById(R.id.grid_size_radio_group);
    }

    public void startGame(View view) {
        int rows, columns;

        int checkedRadioButtonId = gridSizeRadioGroup.getCheckedRadioButtonId();

        if (checkedRadioButtonId == R.id.radio_small) {
            rows = 10;
            columns = 5;
        } else if (checkedRadioButtonId == R.id.radio_medium) {
            rows = 15;
            columns = 10;
        } else if (checkedRadioButtonId == R.id.radio_large) {
            rows = 20;
            columns = 15;
        } else {
            rows = 10;
            columns = 10;
        }

        Intent intent = new Intent(this, MinesweeperActivity.class);
        intent.putExtra("rows", rows);
        intent.putExtra("columns", columns);
        startActivity(intent);
    }
}