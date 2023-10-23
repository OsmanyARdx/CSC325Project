package com.mycompany.serenity;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;


public class UserHome {

    @FXML
    private Label welcomeMessage;
    public void initialize(String userName) {
        welcomeMessage.setText("Welcome, " + userName + "!");
    }

    @FXML
    public void handleClickSafePlace(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("SafePlace.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}

