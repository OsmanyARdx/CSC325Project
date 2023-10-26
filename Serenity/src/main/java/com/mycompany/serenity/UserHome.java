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
import java.util.Objects;


public class UserHome {


    @FXML
    private Label welcomeMessage;
    public void initialize(String userName) {
        welcomeMessage.setText("Welcome, " + userName + "!");
    }

    /**
     * Route user to safe place page
     * @param event on click
     */
    @FXML
    public void handleClickSafePlace(ActionEvent event){
        switchPage(event, "safeplace.fxml");
    }

    /**
     * Route user to emergency resources page
     * @param event on click
     */
    @FXML
    public void handleClickEmergencyResources(ActionEvent event){
        switchPage(event, "EmergencyResources.fxml");
    }

    /**
     * Route user to meditate page
     * @param event on click
     */
    @FXML
    public void handleClickMeditate(ActionEvent event){
        switchPage(event, "Meditation.fxml");
    }

    /**
     * Route user to daily survey page
     * @param event on click
     */
    @FXML
    public void handleClickSurvey(ActionEvent event){
        switchPage(event, "SelfAssessment.fxml");
    }

    /**
     * Sign user out and redirect to landing page
     * @param event on click
     */
    @FXML
    public void handleSignOut(ActionEvent event){
        switchPage(event, "landingPage.fxml");
        UserSession.getInstance().setEmail(null);
    }

    /**
     * Helper method for switching between pages
     * @param event on click action
     * @param page page to route to
     */
    public void switchPage(ActionEvent event, String page){
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(page)));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e){
            System.out.println(e);
        }
    }

}

