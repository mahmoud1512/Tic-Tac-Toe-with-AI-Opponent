package com.example.game;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class Game {
    private Player Humanplayer;
    private Player AI = new Player("AI", Color.BLACK);

    private int turn = 0;
    private String gameStatus = "Game ON";
    @FXML
    GridPane Grid;
    @FXML
    Label CurrentPlayer;

    public void setHumanplayer(Player humanplayer) {
        Humanplayer = humanplayer;
    }

    @FXML
    private char[][] GridProxy = {{'t', 't', 't'}, {'t', 't', 't'}, {'t', 't', 't'}};  // t is a way of saying we are playing nothing
    private boolean[][] visited;

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
        if (turn % 2 == 0 && GridProxy[row][col] == 't') {
            button.setText("X");
            button.setTextFill(Humanplayer.getColor());
            GridProxy[row][col] = 'X';
            turn ++;
            checkEnd('X',GridProxy.clone());
            CurrentPlayer.setText("AI turn");
            playAI();
        }
    }

    private int checkEnd(char c,char[][]grid) {
        visited = new boolean[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!visited[i][j] && grid[i][j] == c) {
                    if(CheckWinner(c, i, j, 1, "none"))
                    {
                        if(c=='X')
                            return -1;
                        else
                            return 1;
                    }
                }
            }
        }
       return 0;
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
        int bestScore=-100000000;
        int[]move=new int[2];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(GridProxy[i][j]=='t')
                {
                    GridProxy[i][j]='O';
                    int score=findMove(GridProxy.clone(),false,'O',turn);
                    if(score>bestScore)
                    {
                        bestScore=score;
                        move[0]=i;
                        move[1]=j;
                    }
                    GridProxy[i][j]='t';
                }
            }
        }
        GridProxy[move[0]][move[1]]='O';
        Button button=getButtonAt(move[0],move[1]);
        button.setText("O");
        turn++;
        CurrentPlayer.setText("Your turn");
    }

    private int findMove(char[][] clone, boolean b ,char player,int turn) {
        if(turn==9)
        {
            int p1=checkEnd('X',clone);
            int p2=checkEnd('O',clone);
            if(p1==0&&p2==0)
                return 0;
            else if (b) {
                return p2;
            }
            else
            {
                return p1;
            }
        }

        if(b)
        {
            int bestScore=-100000000;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if(clone[i][j]=='t')
                    {
                        char[][]clone2=clone.clone();
                        clone2[i][j]='O';
                        int score=findMove(clone2,false,'X',turn+1);
                        bestScore=Math.max(score,bestScore);

                    }
                }
            }
            return bestScore;

        }
        else
        {
            int bestScore=100000000;

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if(clone[i][j]=='t')
                    {
                        char[][]clone2=clone.clone();
                        clone2[i][j]='X';
                        int score=findMove(clone2,true,'O',turn+1);
                        bestScore=Math.min(score,bestScore);
                    }
                }
            }
            return bestScore;
        }
    }


}
