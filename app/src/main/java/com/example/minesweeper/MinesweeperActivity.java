package com.example.minesweeper;

import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;

import androidx.appcompat.app.AppCompatActivity;

public class MinesweeperActivity extends AppCompatActivity {

    private MinesweeperGame minesweeperGame;
    private GridLayout gridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minesweeper);

        gridLayout = findViewById(R.id.grid_layout);

        int rows = 8;
        int columns = 10;

        minesweeperGame = new MinesweeperGame(this, rows, columns);
        minesweeperGame.createGame(gridLayout);
    }

    public void back(View view) {
        finish();
    }

    public void restart(View view) {
        recreate();
    }
}