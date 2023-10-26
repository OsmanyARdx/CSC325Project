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

public class EmergencyResources {
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

    @FXML
    public void handleClickSafePlace(ActionEvent event){
        switchPage(event, "safeplace.fxml");
    }
    @FXML
    public void handleClickMeditate(ActionEvent event){
        switchPage(event, "Meditation.fxml");
    }
    @FXML
    public void handleClickSurvey(ActionEvent event){
        switchPage(event, "SelfAssessment.fxml");
    }
}
