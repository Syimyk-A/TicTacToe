package com.example.tictactoe;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button[][] buttons = new Button[3][3];
    private boolean playerXTurn = true; // true если ходит X, false если O
    private int scoreX = 0; // Счет для игрока X
    private int scoreY = 0; // Счет для игрока Y
    private boolean isBotMode = false; // Режим игры с ботом

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация кнопок
        buttons[0][0] = findViewById(R.id.button1);
        buttons[0][1] = findViewById(R.id.button2);
        buttons[0][2] = findViewById(R.id.button3);
        buttons[1][0] = findViewById(R.id.button4);
        buttons[1][1] = findViewById(R.id.button5);
        buttons[1][2] = findViewById(R.id.button6);
        buttons[2][0] = findViewById(R.id.button7);
        buttons[2][1] = findViewById(R.id.button8);
        buttons[2][2] = findViewById(R.id.button9);

        // Назначение обработчиков нажатий
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setOnClickListener(new ButtonClickListener(i, j));
            }
        }

        // Обработчик для кнопки сброса
        findViewById(R.id.buttonDel).setOnClickListener(v -> resetScores());

        // Обработчик для кнопки "Bot"
        findViewById(R.id.buttonBot).setOnClickListener(v -> toggleBotMode());

        // Обработчик для кнопки "Обратно"
        findViewById(R.id.buttonRe).setOnClickListener(v -> resetGame());
    }

    private class ButtonClickListener implements View.OnClickListener {
        private int row, col;

        public ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void onClick(View v) {
            if (!buttons[row][col].getText().toString().equals("")) {
                return; // Если ячейка занята, ничего не делать
            }

            buttons[row][col].setText(playerXTurn ? "X" : "O"); // Установка текста
            if (checkForWin()) {
                Toast.makeText(MainActivity.this, "Игрок " + (playerXTurn ? "X" : "O") + " выиграл!", Toast.LENGTH_SHORT).show();
                highlightWinningCells(); // Метод для окрашивания выигрышных ячеек
                if (playerXTurn) {
                    scoreX++;
                } else {
                    scoreY++;
                }
                updateScores();
                return;
            }
            playerXTurn = !playerXTurn; // Переключение игрока

            if (isBotMode && !playerXTurn) {
                botMove(); // Ход бота
            }
        }
    }

    private boolean checkForWin() {
        // Проверка горизонталей и вертикалей
        for (int i = 0; i < 3; i++) {
            if (buttons[i][0].getText().toString().equals(buttons[i][1].getText().toString()) &&
                    buttons[i][1].getText().toString().equals(buttons[i][2].getText().toString()) &&
                    !buttons[i][0].getText().toString().equals("")) {
                return true;
            }
            if (buttons[0][i].getText().toString().equals(buttons[1][i].getText().toString()) &&
                    buttons[1][i].getText().toString().equals(buttons[2][i].getText().toString()) &&
                    !buttons[0][i].getText().toString().equals("")) {
                return true;
            }
        }

        // Проверка диагоналей
        if (buttons[0][0].getText().toString().equals(buttons[1][1].getText().toString()) &&
                buttons[1][1].getText().toString().equals(buttons[2][2].getText().toString()) &&
                !buttons[0][0].getText().toString().equals("")) {
            return true;
        }

        if (buttons[0][2].getText().toString().equals(buttons[1][1].getText().toString()) &&
                buttons[1][1].getText().toString().equals(buttons[2][0].getText().toString()) &&
                !buttons[0][2].getText().toString().equals("")) {
            return true;
        }

        return false; // Победителя нет
    }

    private void highlightWinningCells() {
        for (int i = 0; i < 3; i++) {
            if (buttons[i][0].getText().toString().equals(buttons[i][1].getText().toString()) &&
                    buttons[i][1].getText().toString().equals(buttons[i][2].getText().toString()) &&
                    !buttons[i][0].getText().toString().equals("")) {
                buttons[i][0].setTextColor(Color.RED);
                buttons[i][1].setTextColor(Color.RED);
                buttons[i][2].setTextColor(Color.RED);
                return;
            }

            if (buttons[0][i].getText().toString().equals(buttons[1][i].getText().toString()) &&
                    buttons[1][i].getText().toString().equals(buttons[2][i].getText().toString()) &&
                    !buttons[0][i].getText().toString().equals("")) {
                buttons[0][i].setTextColor(Color.RED);
                buttons[1][i].setTextColor(Color.RED);
                buttons[2][i].setTextColor(Color.RED);
            }
        }

        // Проверка диагоналей для окрашивания
        if (buttons[0][0].getText().toString().equals(buttons[1][1].getText().toString()) &&
                buttons[1][1].getText().toString().equals(buttons[2][2].getText().toString()) &&
                !buttons[0][0].getText().toString().equals("")) {
            buttons[0][0].setTextColor(Color.RED);
            buttons[1][1].setTextColor(Color.RED);
            buttons[2][2].setTextColor(Color.RED);
            return;
        }

        if (buttons[0][2].getText().toString().equals(buttons[1][1].getText().toString()) &&
                buttons[1][1].getText().toString().equals(buttons[2][0].getText().toString()) &&
                !buttons[0][2].getText().toString().equals("")) {
            buttons[0][2].setTextColor(Color.RED);
            buttons[1][1].setTextColor(Color.RED);
            buttons[2][0].setTextColor(Color.RED);
            return;
        }
    }


    private void resetGame() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setTextColor(Color.LTGRAY); // Вернуть цвет кнопок к стандартному
            }
        }
        playerXTurn = true; // Вернуть к первому игроку
    }

    private void resetScores() {
        scoreX = 0;
        scoreY = 0;
        updateScores(); // Обновить отображение счета
    }

    private void updateScores() {
        ((TextView) findViewById(R.id.textViewX)).setText("X: " + scoreX);
        ((TextView) findViewById(R.id.textViewY)).setText("O: " + scoreY);
    }

    private void toggleBotMode() {
        isBotMode = !isBotMode;
        Toast.makeText(this, isBotMode ? "Режим игры с ботом включен" : "Режим игры с ботом отключен", Toast.LENGTH_SHORT).show();
        resetGame(); // Перезапуск игры при смене режима
    }

    private void botMove() {
        Random rand = new Random();
        int row, col;

        do {
            row = rand.nextInt(3);
            col = rand.nextInt(3);
        } while (!buttons[row][col].getText().toString().equals("")); // Найти пустую ячейку

        buttons[row][col].setText("O"); // Ход бота
        if (checkForWin()) {
            Toast.makeText(this, "Бот выиграл!", Toast.LENGTH_SHORT).show();
            highlightWinningCells();
            scoreY++;
            updateScores();
        } else {
            playerXTurn = true; // Вернуть ход игроку X
        }
    }
}
