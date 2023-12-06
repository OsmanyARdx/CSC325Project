package com.mycompany.serenity;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class Meditate {

    @FXML
    public void handleClickEmergencyResources(ActionEvent event) {
        switchPage(event, "EmergencyResources.fxml");
    }

    @FXML
    public void handleClickSafePlace(ActionEvent event) {
        switchPage(event, "safeplace.fxml");
    }

    @FXML
    public void handleClickMeditate(ActionEvent event) {switchPage(event, "Meditate.fxml");}

    @FXML
    public void handleClickWellnessTips(ActionEvent event) {switchPage(event, "WellnessTips.fxml");}

    @FXML
    public void handleClickDailySurvey(ActionEvent event) {switchPage(event, "SelfAssessment.fxml");}

    /**
     * Brings user back to userHome
     * @param event mouse click
     */
    @FXML
    public void handleBackToHome(MouseEvent event) {
        UserSession userSession = UserSession.getInstance();
        switchToHome(userSession.getName().join(), event);
    }

    /**
     * Helper function for routing between any page that isn't userHome
     * @param event on click
     * @param page page to route to
     */
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

    /**
     * Helper function for returning to userHome page
     * @param userName current user's name
     * @param event mouse click
     */
    public void switchToHome(String userName, MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userHome.fxml"));
            Parent root = loader.load();
            UserHome userHome = loader.getController();
            userHome.initialize(userName);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e);
        }
    }


}