package com.mycompany.serenity;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LandingPage {

    @FXML
    public void handleToLogin(ActionEvent event) {
        switchPage(event, "login.fxml");
    }
    @FXML
    public void handleToSignup(ActionEvent event) {
        switchPage(event,"signup.fxml");
    }

    @FXML
    public void handleClickEmergencyResources(ActionEvent event) {
        switchPage(event, "EmergencyResources.fxml");
    }

    public void switchPage(ActionEvent event, String page) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(page)));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e);
        }
    }


}
