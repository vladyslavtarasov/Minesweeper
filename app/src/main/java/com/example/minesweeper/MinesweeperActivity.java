package com.example.minesweeper;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class MinesweeperActivity extends AppCompatActivity {

    private MinesweeperGame minesweeperGame;
    private GridLayout gridLayout;
    private TextView timerText;
    private Timer gameTimer;
    private long startTime;
    private int rows, columns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minesweeper);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        gridLayout = findViewById(R.id.grid_layout);
        timerText = findViewById(R.id.timer_text);

        Intent intent = getIntent();
        rows = intent.getIntExtra("rows", 10);
        columns = intent.getIntExtra("columns", 10);

        minesweeperGame = new MinesweeperGame(this, rows, columns);
        minesweeperGame.createGame(gridLayout);

        startTimer();
    }

    public void back(View view) {
        stopTimer();
        finish();
    }

    public void restart(View view) {
        stopTimer();
        recreate();
    }

    private void startTimer() {
        startTime = System.currentTimeMillis();

        gameTimer = new Timer();
        gameTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        long currentTime = System.currentTimeMillis();
                        long elapsedTime = currentTime - startTime;

                        int seconds = (int) (elapsedTime / 1000) % 60;
                        int minutes = (int) ((elapsedTime / (1000 * 60)) % 60);
                        int hours = (int) ((elapsedTime / (1000 * 60 * 60)) % 24);

                        String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                        timerText.setText(timeString);
                    }
                });
            }
        }, 0, 1000);
    }

    public void stopTimer() {
        if (gameTimer != null) {
            gameTimer.cancel();
            gameTimer.purge();
        }
    }
}