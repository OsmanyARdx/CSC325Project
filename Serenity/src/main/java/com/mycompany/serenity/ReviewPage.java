package com.mycompany.serenity;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class ReviewPage {

    @FXML
    private ChoiceBox<String> reviewBOX1, reviewBOX2;

    @FXML
    private TextArea textArea1, textArea2, textArea3;

    @FXML
    public void initialize() {
        // You can initialize the ChoiceBox items here
        reviewBOX1.getItems().addAll("Yes", "Somewhat", "No");
        reviewBOX2.getItems().addAll("Very likely", "Likely", "Neutral", "Unlikely", "Very unlikely");
    }

    @FXML
    public void handleSendReview(){

    }


}
