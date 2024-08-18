package com.example.game;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public class Game {
    private Player Humanplayer;
    private int turn = 0;
    @FXML
    GridPane Grid;
    @FXML
    Label CurrentPlayer;

    private char[][] GridProxy = {{'t', 't', 't'}, {'t', 't', 't'}, {'t', 't', 't'}};  // t is a way of saying we are playing nothing
    private boolean[][] visited;
    private String gameState="Game on";
    public void setHumanPlayer(Player humanplayer) {
        Humanplayer = humanplayer;
        CurrentPlayer.setText(Humanplayer.getName()+"'S turn");
    }
    @FXML
    public void initialize() {
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
            int rowI, colI;
            if (GridPane.getRowIndex(node) == null)
                rowI = 0;
            else
                rowI = GridPane.getRowIndex(node);
            if (GridPane.getColumnIndex(node) == null)
                colI = 0;
            else
                colI = GridPane.getColumnIndex(node);

            if (rowI == row && colI == col) {
                return (Button) node;
            }
        }
        return null;
    }

    private void handleButtonClick(Button button, int row, int col) {
        if (turn % 2 == 0 && GridProxy[row][col] == 't'&&gameState.equals("Game on")) {
            button.setText("X");
            button.setTextFill(Humanplayer.getColor());
            GridProxy[row][col] = 'X';
            turn++;
            if (checkEnd(GridProxy)==-1) {
                CurrentPlayer.setText(Humanplayer.getName() + " wins");
                gameState = "Game end";
            }
            else if (turn==9) {
                CurrentPlayer.setText("Tie Game");
                gameState = "Game end";
            }
            else {
                CurrentPlayer.setText("AI turn");
                PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
                pause.setOnFinished(event -> {
                    if (turn % 2 == 1 && gameState.equals("Game on")) {
                        playAI();
                        if (checkEnd(GridProxy) == 1) {
                            CurrentPlayer.setText("AI wins");
                            gameState = "Game end";
                        } else if (turn != 9) {
                            CurrentPlayer.setText(Humanplayer.getName()+"'S turn");
                        } else {
                            CurrentPlayer.setText("Tie Game");
                            gameState = "Game end";
                        }
                    }
                });
                pause.play();
            }
        }
    }

    private int checkEnd(char[][]grid) {
        visited = new boolean[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!visited[i][j]) {
                    if(grid[i][j]=='X'&&CheckWinner('X',i,j,1,"none"))
                        return -1;
                    else if(grid[i][j]=='O'&&CheckWinner('O',i,j,1,"none"))
                       return 1;
                }
            }
        }
        boolean x=true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(grid[i][j]=='t')
                    x=false;
            }
        }
       if(x)
           return 0;
       else
           return 2;
    }

    private boolean CheckWinner(char c, int i, int j, int count, String dir) {
        if (i < 0 || i == 3 || j < 0 || j == 3)
            return false;
        if (GridProxy[i][j] != c)
            return false;
        if (visited[i][j])
            return false;
        if (count == 3)
            return true;
        visited[i][j] = true;
        boolean p1 = false, p2 = false, p3 = false, p4 = false, p5 = false, p6 = false, p7 = false, p8 = false;
        if (dir.equals("none") || dir.equals("down"))
            p1 = CheckWinner(c, i + 1, j, count + 1, "down");
        if (dir.equals("none") || dir.equals("up"))
            p2 = CheckWinner(c, i - 1, j, count + 1, "up");
        if (dir.equals("none") || dir.equals("right"))
            p3 = CheckWinner(c, i, j + 1, count + 1, "right");
        if (dir.equals("none") || dir.equals("left"))
            p4 = CheckWinner(c, i, j - 1, count + 1, "left");
        if (dir.equals("none") || dir.equals("down-right"))
            p5 = CheckWinner(c, i + 1, j + 1, count + 1, "down-right");
        if (dir.equals("none") || dir.equals("up-left"))
            p6 = CheckWinner(c, i - 1, j - 1, count + 1, "up-left");
        if (dir.equals("none") || dir.equals("up-right"))
            p7 = CheckWinner(c, i - 1, j + 1, count + 1, "up-right");
        if (dir.equals("none") || dir.equals("down-left"))
            p8 = CheckWinner(c, i + 1, j - 1, count + 1, "down-left");
        visited[i][j] = false;
        return p1 || p2 || p3 || p4 || p5 || p6 || p7 || p8;
    }

    private void playAI() {
        int bestScore = Integer.MIN_VALUE;
        int[] move = new int[2];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (GridProxy[i][j] == 't') {
                    GridProxy[i][j] = 'O';
                    int score = findMove(GridProxy, false);
                    GridProxy[i][j] = 't';
                    if (score > bestScore) {
                        bestScore = score;
                        move[0] = i;
                        move[1] = j;
                    }
                }
            }
        }
        GridProxy[move[0]][move[1]] = 'O'; // Make the best move
        Button button = getButtonAt(move[0], move[1]);
        button.setText("O");
        turn++;
    }

    private int findMove(char[][] grid, boolean isMaximizing) {
        int result = checkEnd(grid);
        if (result != 2) { // Game is over or tied
            return result;
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (grid[i][j] == 't') {
                        grid[i][j] = 'O';
                        int score = findMove(grid, false);
                        grid[i][j] = 't';
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (grid[i][j] == 't') {
                        grid[i][j] = 'X';
                        int score = findMove(grid, true);
                        grid[i][j] = 't';
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
            return bestScore;
        }
    }



}
