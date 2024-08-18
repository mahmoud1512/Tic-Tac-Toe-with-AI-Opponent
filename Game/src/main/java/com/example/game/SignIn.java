package com.example.game;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class SignIn {

    @FXML
    private Button goGame;
    @FXML
    private TextField name1;

    @FXML
    private ColorPicker color1;
    @FXML
    private Label Status;

    public void GoGame(ActionEvent e) {
        String Name1 = name1.getText();
        if (Name1.isEmpty()) {
            Status.setText("The name field can't be empty");
        } else {
            switchToGameScene(e, Name1, color1.getValue());
        }
    }

    private void switchToGameScene(ActionEvent event, String playerName, Color playerColor) {
        try {
            // Load the Game.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Game.fxml"));
            Parent gameRoot = loader.load();
            // Get the controller associated with Game.fxml
            Game gameController = loader.getController();
            Player player=new Player(playerName,playerColor);
            gameController.setHumanPlayer(player);
            Scene gameScene = new Scene(gameRoot);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(gameScene);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            Status.setText("Failed to load the game scene.");
        }
    }
}
