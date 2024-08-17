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
        if (turn % 2 == 0 && GridProxy[row][col] == 't' && gameStatus.equals("Game ON")) {
            button.setText("X");
            button.setTextFill(Humanplayer.getColor());
            GridProxy[row][col] = 'X';
            turn+=2;
            checkEnd('X');
//            CurrentPlayer.setText("AI turn");
//            if (gameStatus.equals("Game ON")) {
//                playAI();
//            }
        }
    }

    private void checkEnd(char c) {
        visited=new boolean[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!visited[i][j]&&GridProxy[i][j]==c) {
                    if(checkwinner(c, i, j ,1,"none"))
                    {
                        CurrentPlayer.setText("You are winner");
                    }
                }
            }
        }
    }

    private boolean checkwinner(char c,int i,int j, int count,String dir) {
        if (i < 0 || i == 3 || j < 0 || j == 3)
            return false;
        if (GridProxy[i][j] != c)
            return false;
        if (visited[i][j])
            return false;
        if(count==3)
            return true;

        visited[i][j]=true;
        boolean p1=false,p2=false,p3=false,p4=false,p5=false,p6=false,p7=false,p8=false;
        if(dir.equals("none")||dir.equals("down"))
         p1=checkwinner(c,i+1,j,count+1,"down");
        if (dir.equals("none")||dir.equals("up"))
         p2=checkwinner(c,i-1,j,count+1,"up");
        if (dir.equals("none")||dir.equals("right"))
         p3=checkwinner(c,i,j+1,count+1,"right");
        if(dir.equals("none")||dir.equals("left"))
         p4=checkwinner(c,i,j-1,count+1,"left");
        if (dir.equals("none")||dir.equals("down-right"))
         p5=checkwinner(c,i+1,j+1,count+1,"down-right");
        if (dir.equals("none")||dir.equals("up-left"))
         p6=checkwinner(c,i-1,j-1,count+1,"up-left");
        if (dir.equals("none")||dir.equals("up-right"))
            p7=checkwinner(c,i-1,j+1,count+1,"up-right");
        if (dir.equals("none")||dir.equals("down-left"))
            p8=checkwinner(c,i+1,j-1,count+1,"down-left");
        visited[i][j]=false;
        return p1||p2||p3||p4||p5||p6||p7||p8;

    }

    private void playAI() {
    }


}
