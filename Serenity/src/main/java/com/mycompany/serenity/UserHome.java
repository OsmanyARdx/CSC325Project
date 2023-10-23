package com.mycompany.serenity;


import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class UserHome {

    @FXML
    private Label welcomeMessage;
    public void initialize(String userName) {
        welcomeMessage.setText("Welcome, " + userName + "!");
    }

}

