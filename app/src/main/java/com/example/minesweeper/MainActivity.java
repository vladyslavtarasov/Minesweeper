package com.example.minesweeper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private MinesweeperGame minesweeperGame;
    private LinearLayout mainMenuLayout;
    private GridLayout gridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainMenuLayout = findViewById(R.id.mainMenuLayout);
        gridLayout = findViewById(R.id.gridLayout);

        int rows = 8;
        int columns = 10;

        minesweeperGame = new MinesweeperGame(this, rows, columns);
    }

    public void startGame(View view) {
        mainMenuLayout.setVisibility(View.GONE);
        gridLayout.setVisibility(View.VISIBLE);

        minesweeperGame.createGame(gridLayout);
    }
}