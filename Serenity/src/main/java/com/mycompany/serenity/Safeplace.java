package com.mycompany.serenity;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

public class Safeplace {

    @FXML
    private TextArea Q1TXTArea, Q2TXTArea, Q3TXTArea;

    /**
     * Saves safe place form as a text file to local machine
     * @param e
     */
    @FXML
    public void handleSendToFiles(ActionEvent e) {
        StringBuilder sb = new StringBuilder();
        sb.append("How are you feeling?\n" + Q1TXTArea.getText() + "\n");
        sb.append("What is on your mind?\n" + Q2TXTArea.getText() + "\n");
        sb.append("What can you do to fix your situation?\n" + Q3TXTArea.getText() + "\n");


        String userHome = System.getProperty("user.home");

        // Construct the path to the "Downloads" directory
        String downloadsPath = userHome + File.separator + "Documents";
        // Create a File object for the destination file on the desktop
        File diary = new File(downloadsPath, "my_diary.txt - " + LocalDate.now());

        //check if diary exists and write to it
        try (FileWriter writer = new FileWriter(diary)) {
            // Write the contents of the StringBuilder to the file
            writer.write(sb.toString());
            writer.close();
            System.out.println("wrote to diary");
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    /**
     * Routes user to emergency resources page
     * @param event
     */
    @FXML
    public void handleClickEmergencyResources(ActionEvent event) {
        switchPage(event, "EmergencyResources.fxml");
    }

    /**
     * Routes user to meditation page
     * @param event
     */
    @FXML
    public void handleClickMeditate(ActionEvent event) {
        switchPage(event, "Meditation.fxml");
    }

    /**
     * Routes user to daily survey page
     * @param event
     */
    @FXML
    public void handleClickDailySurvey(ActionEvent event) {
        switchPage(event, "SelfAssessment.fxml");
    }

    /**
     * Routes user back to home page
     * @param event
     */
    @FXML
    public void handleBackToHome(MouseEvent event) {
        UserSession userSession = UserSession.getInstance();
        switchToHome(userSession.getName().join(), event);
    }

    /**
     * Helper method for page routing
     * @param event
     * @param page
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
     *
     * @param userName
     * @param event
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
