package com.example.minesweeper;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import java.util.Random;

public class MinesweeperGame {
    private final Context context;
    private final int rows;
    private final int columns;
    private final int[][] grid;
    private final Button[][] buttons;
    private boolean gameOver;

    public MinesweeperGame(Context context, int rows, int columns) {
        this.context = context;
        this.rows = rows;
        this.columns = columns;
        this.grid = new int[rows][columns];
        this.buttons = new Button[rows][columns];
        this.gameOver = false;
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

        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        int screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        int buttonSize = Math.min(screenWidth / columns, screenHeight / rows);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                buttons[i][j] = new Button(context);
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

    private View.OnClickListener handleButtonClick(final int row, final int column) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!gameOver) {
                    if (grid[row][column] == -1) {
                        revealMines();
                        showMessage("Game Over!");
                        gameOver = true;
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

        int mines = (rows * columns) / 6; // Adjust the mine density as needed

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

    private void revealMines() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (grid[i][j] == -1) {
                    buttons[i][j].setText("X");
                    buttons[i][j].setBackgroundColor(Color.RED);
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

        if (revealedCells == totalCells) {
            showMessage("Congratulations! You win!");
            gameOver = true;
        }
    }

    private void showMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}