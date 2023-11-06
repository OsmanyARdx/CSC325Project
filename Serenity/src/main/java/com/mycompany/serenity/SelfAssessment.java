package com.mycompany.serenity;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.bson.Document;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

public class SelfAssessment {
    @FXML
    private ChoiceBox<String> mood_todayBOX, energy_levelBOX,
            sleep_qualityBOX, self_careBOX, outlookBOX;

    @FXML
    private DatePicker todaysDate;
    @FXML
    private Pane surveyPane;
    @FXML
    private Label surveyComplete;
    @FXML
    private Label errorMessage;

    /**
     * Initialize the choice box items
     * The page will only show the survey if you haven't filled it out today.
     * Otherwise, shows a thank you message.
     */
    @FXML
    public void initialize() {
        // You can initialize the ChoiceBox items here
        mood_todayBOX.getItems().addAll("Happy", "Sad", "Excited", "Angry", "Calm", "Depressed", "Anxious");
        energy_levelBOX.getItems().addAll("Energetic", "Moderately active", "Low energy", "Completely drained");
        sleep_qualityBOX.getItems().addAll("Very well", "Somewhat well", "Not very well", "Terribly");
        self_careBOX.getItems().addAll("Yes", "No", "Planning to later");
        outlookBOX.getItems().addAll("Optimistic", "Neutral", "Pessimistic");

        Platform.runLater(() -> {
            boolean hasSurvey = checkForSurvey();
            handleSurveyCheckResult(hasSurvey);
        });
    }

    /**
     * Helper method for showing the right survey screen
     * @param hasSurvey
     */
    private void handleSurveyCheckResult(boolean hasSurvey) {
        if (!hasSurvey) {
            surveyPane.setDisable(false);
            surveyComplete.setOpacity(0);
        } else {
            surveyPane.setDisable(true);
            surveyComplete.setOpacity(1);
        }
    }

    /**
     * Query DB and return true if a survey exists with today's date.
     * @return true/false
     */
    public Boolean checkForSurvey(){

        String todaysDate = LocalDate.now().toString();

        MongoCollection<Document> users = UserSession.getInstance().openConn();
        Document filter = new Document("_id", UserSession.getInstance().getEmail());

        filter.append("Daily surveys.Date", todaysDate);
        FindIterable<Document> result = users.find(filter);

        return result.iterator().hasNext();

    }

    /**
     * Send survey info to database
     */
    @FXML
    public void handleSubmitSurvey() {
        String mood = mood_todayBOX.getValue();
        String energyLevel = energy_levelBOX.getValue();
        String sleepQuality = sleep_qualityBOX.getValue();
        String selfCare = self_careBOX.getValue();
        String outlookToday = outlookBOX.getValue();
        LocalDate date = todaysDate.getValue();

        if (mood == null || energyLevel == null || sleepQuality == null || selfCare == null || outlookToday == null || date == null) {
            // At least one of the fields is empty
            errorMessage.setOpacity(1);
        } else {
            // All fields are filled in
            // Continue with the processing
            Document dailySurveyDocument = new Document();
            dailySurveyDocument.append("Mood", mood);
            dailySurveyDocument.append("Energy Level", energyLevel);
            dailySurveyDocument.append("Sleep Quality", sleepQuality);
            dailySurveyDocument.append("Self Care", selfCare);
            dailySurveyDocument.append("Outlook Today", outlookToday);
            dailySurveyDocument.append("Date", date.toString());

            MongoCollection<Document> users = UserSession.getInstance().openConn();
            Document filter = new Document("_id", UserSession.getInstance().getEmail());
            Document update = new Document("$addToSet", new Document("Daily surveys", dailySurveyDocument));

            users.updateOne(filter, update);

            surveyPane.setDisable(true);
            surveyComplete.setOpacity(1);
        }
    }

    @FXML
    public void handleClickEmergencyResources(ActionEvent event) {
        switchPage(event, "EmergencyResources.fxml");
    }

    @FXML
    public void handleClickMeditate(ActionEvent event) {
        switchPage(event, "Meditate.fxml");
    }


    @FXML
    public void handleClickSafePlace(ActionEvent event) {
        switchPage(event, "safeplace.fxml");
    }

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