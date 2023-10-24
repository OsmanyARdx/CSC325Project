package com.mycompany.serenity;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import java.net.URL;
import java.util.ResourceBundle;

public class SelfAssessment implements Initializable {
    @FXML
    private ChoiceBox<String> mood_todayBOX, energy_levelBOX,
            sleep_qualityBOX, stressors_todayBOX, positive_eventsBOX, self_careBOX, outlookBOX;

    @FXML
    private Label mood_today,energy_level, sleep_quality, stressors_today, positive_events, self_care, outlook;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // You can initialize the ChoiceBox items here
        mood_todayBOX.getItems().addAll("Happy", "Sad", "Excited", "Angry", "Calm");
        energy_levelBOX.getItems().addAll("Energetic", "Moderately active", "Low energy", "Completely drained");
        sleep_qualityBOX.getItems().addAll("Very well", "Somewhat well", "Not very well", "Terribly");
        stressors_todayBOX.getItems().addAll("Yes", "No", "Not sure");
        positive_eventsBOX.getItems().addAll("Yes", "No", "Not sure");
        self_careBOX.getItems().addAll("Yes", "No", "Planning to later");
        outlookBOX.getItems().addAll("Optimistic", "Neutral", "Pessimistic");
    }
}
