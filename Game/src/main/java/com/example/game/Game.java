package com.example.game;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class Game {
    private Player Humanplayer;
    private Player AI=new Player("AI", Color.BLACK);

    private int turn=0;
    private String gameStatus="Game ON";
    @FXML
    GridPane Grid;
    public void setHumanplayer(Player humanplayer) {
        Humanplayer = humanplayer;
    }
    @FXML
    private char[][]GridProxy={{'t','t','t'},{'t','t','t'},{'t','t','t'}};  // t is a way of saying we are playing nothing

    @FXML
    public void initialize() {
        // Add event handlers to each button in the grid
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Button button = getButtonAt(row, col);
                if (button != null) {
                    final int r = row;
                    final int c = col;

                    button.setOnAction(event -> handleButtonClick(button, r, c));
                }
            }
        }
    }
    private Button getButtonAt(int row, int col) {
        for (var node : Grid.getChildren()) {
            int rowI,colI;
            if(GridPane.getRowIndex(node) == null)
                rowI=0;
            else
                rowI=GridPane.getRowIndex(node);
            if (GridPane.getColumnIndex(node) == null)
                colI=0;
            else
                colI=GridPane.getColumnIndex(node);

            if (rowI == row && colI == col) {
                return (Button) node;
            }
        }
        return null;
    }
    private void handleButtonClick(Button button, int row, int col) {
        if (turn % 2 == 0 && GridProxy[row][col] == 't') {
            button.setText("X");
            button.setTextFill(Humanplayer.getColor());
            GridProxy[row][col] = 'X';
            turn++;
            //checkGameStatus();
            if (gameStatus.equals("Game ON")) {
                playAI();
            }
        }
    }

    private void playAI() {
    }






}
