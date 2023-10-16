package com.mycompany.serenity;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class Safeplace implements Initializable {

    @FXML
    private ChoiceBox<String> moodChoiceBox;

    @FXML
    private TextArea Q2TXTArea;

    @FXML
    private TextArea Q3TXTArea;

    @FXML
    private Label Q1;

    @FXML
    private Label Q2;

    @FXML
    private Label Q3;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // You can initialize the ChoiceBox items here
        moodChoiceBox.getItems().addAll("Happy", "Sad", "Excited", "Angry", "Calm");
    }

}
