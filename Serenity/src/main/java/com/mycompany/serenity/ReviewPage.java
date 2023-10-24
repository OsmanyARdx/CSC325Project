package com.mycompany.serenity;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class ReviewPage implements Initializable {

    @FXML
    private Label reviewLabel, reviewLabel1;

    @FXML
    private ChoiceBox<String> reviewBOX, reviewBOX1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // You can initialize the ChoiceBox items here
        reviewBOX.getItems().addAll("1", "2", "3", "4", "5");
        reviewBOX1.getItems().addAll("Yes","No");
    }
}
