package com.mycompany.serenity;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

public class ReviewPage {

    @FXML
    private Label reviewLabel, reviewLabel1;

    @FXML
    private ChoiceBox<String> reviewBOX, reviewBOX1;

    @FXML
    public void initialize() {
        // You can initialize the ChoiceBox items here
        reviewBOX.getItems().addAll("1", "2", "3", "4", "5");
        reviewBOX1.getItems().addAll("Yes","No");
    }
}
