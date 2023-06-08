package com.example.minesweeper;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import java.util.Random;

public class MinesweeperGame {
    private final MinesweeperActivity activity;
    private final int rows;
    private final int columns;
    private final int[][] grid;
    private final Button[][] buttons;
    private boolean gameOver;
    private final int mines;

    public MinesweeperGame(MinesweeperActivity activity, int rows, int columns) {
        this.activity = activity;
        this.rows = rows;
        this.columns = columns;
        this.grid = new int[rows][columns];
        this.buttons = new Button[rows][columns];
        this.gameOver = false;
        mines = (rows * columns) / 6;
        //mines = 1;
    }

    public void createGame(GridLayout gridLayout) {
        initializeGrid();
        initializeButtons(gridLayout);
        placeMines();
        setNumberHints();
    }

    private void initializeGrid() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                grid[i][j] = 0;
            }
        }
    }

    private void initializeButtons(GridLayout gridLayout) {
        gridLayout.setColumnCount(columns);
        gridLayout.setRowCount(rows);

        int screenWidth = activity.getResources().getDisplayMetrics().widthPixels;
        int screenHeight = activity.getResources().getDisplayMetrics().heightPixels;
        int availableHeight = screenHeight - getStatusBarHeight() - getActionBarHeight();

        int buttonSize = Math.min(screenWidth / columns, availableHeight / rows);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                buttons[i][j] = new Button(activity);
                buttons[i][j].setLayoutParams(new GridLayout.LayoutParams());
                buttons[i][j].setPadding(0, 0, 0, 0);
                buttons[i][j].setTextSize(20);
                buttons[i][j].setGravity(Gravity.CENTER);
                buttons[i][j].setOnClickListener(handleButtonClick(i, j));

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = buttonSize;
                params.height = buttonSize;

                buttons[i][j].setLayoutParams(params);
                gridLayout.addView(buttons[i][j]);
            }
        }
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private int getActionBarHeight() {
        int result = 0;
        TypedValue typedValue = new TypedValue();
        if (activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true)) {
            result = TypedValue.complexToDimensionPixelSize(typedValue.data, activity.getResources().getDisplayMetrics());
        }
        return result;
    }

    private View.OnClickListener handleButtonClick(final int row, final int column) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!gameOver) {
                    if (grid[row][column] == -1) {
                        revealMines(false);
                        showMessage("Game Over!");
                        gameOver = true;
                        activity.stopTimer();
                    } else if (grid[row][column] == 0) {
                        revealEmptyCells(row, column);
                        checkWinCondition();
                    } else {
                        revealCell(row, column);
                        checkWinCondition();
                    }
                }
            }
        };
    }

    private void placeMines() {
        Random random = new Random();

        for (int i = 0; i < mines; i++) {
            int row = random.nextInt(rows);
            int column = random.nextInt(columns);

            if (grid[row][column] == -1) {
                i--;
            } else {
                grid[row][column] = -1;
            }
        }
    }

    private void setNumberHints() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (grid[i][j] != -1) {
                    int count = 0;

                    if (i > 0 && j > 0 && grid[i - 1][j - 1] == -1)
                        count++;
                    if (i > 0 && grid[i - 1][j] == -1)
                        count++;
                    if (i > 0 && j < columns - 1 && grid[i - 1][j + 1] == -1)
                        count++;
                    if (j > 0 && grid[i][j - 1] == -1)
                        count++;
                    if (j < columns - 1 && grid[i][j + 1] == -1)
                        count++;
                    if (i < rows - 1 && j > 0 && grid[i + 1][j - 1] == -1)
                        count++;
                    if (i < rows - 1 && grid[i + 1][j] == -1)
                        count++;
                    if (i < rows - 1 && j < columns - 1 && grid[i + 1][j + 1] == -1)
                        count++;

                    grid[i][j] = count;
                }
            }
        }
    }

    private void revealCell(int row, int column) {
        buttons[row][column].setText(String.valueOf(grid[row][column]));
        buttons[row][column].setBackgroundColor(Color.WHITE);
        buttons[row][column].setEnabled(false);
    }

    private void revealEmptyCells(int row, int column) {
        if (row >= 0 && row < rows && column >= 0 && column < columns && buttons[row][column].isEnabled()) {
            if (grid[row][column] == 0) {
                revealCell(row, column);

                revealEmptyCells(row - 1, column - 1);
                revealEmptyCells(row - 1, column);
                revealEmptyCells(row - 1, column + 1);
                revealEmptyCells(row, column - 1);
                revealEmptyCells(row, column + 1);
                revealEmptyCells(row + 1, column - 1);
                revealEmptyCells(row + 1, column);
                revealEmptyCells(row + 1, column + 1);
            } else if (grid[row][column] > 0) {
                revealCell(row, column);
            }
        }
    }

    private void revealMines(boolean isWin) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (grid[i][j] == -1) {
                    buttons[i][j].setText("X");
                    if (isWin) buttons[i][j].setBackgroundColor(Color.GREEN);
                    else buttons[i][j].setBackgroundColor(Color.RED);
                    buttons[i][j].setEnabled(false);
                }
            }
        }
    }

    private void checkWinCondition() {
        int revealedCells = 0;
        int totalCells = rows * columns;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (!buttons[i][j].isEnabled())
                    revealedCells++;
            }
        }

        if (revealedCells == totalCells - mines) {
            showMessage("Congratulations! You win!");
            activity.stopTimer();
            gameOver = true;
            revealMines(true);
        }
    }

    private void showMessage(String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }
}