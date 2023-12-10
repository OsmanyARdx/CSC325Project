package com.mycompany.serenity;

import com.mongodb.client.MongoCollection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.bson.Document;

import java.io.IOException;

public class ReviewPage {

    @FXML
    private ChoiceBox<String> reviewBOX1, reviewBOX2;

    @FXML
    private TextArea textArea1, textArea2, textArea3;

    @FXML
    private Label errorMessage;

    @FXML
    public void initialize() {
        // You can initialize the ChoiceBox items here
        reviewBOX1.getItems().addAll("Yes", "Somewhat", "No");
        reviewBOX2.getItems().addAll("Very likely", "Likely", "Neutral", "Unlikely", "Very unlikely");
    }

    @FXML
    public void handleSendReview(){
        String reviewQ1 = reviewBOX1.getValue();
        String reviewQ2 = reviewBOX2.getValue();
        String textQ1 = textArea1.getText();
        String textQ2 = textArea2.getText();
        String textQ3 = textArea3.getText();

        if (reviewQ1 == null || reviewQ2 == null || textQ1 == null || textQ2 == null || textQ3 == null) {
            // At least one of the fields is empty
            errorMessage.setOpacity(1);
        }
        else {
            // All fields are filled in
            // Continue with the processing
            Document dailySurveyDocument = new Document();
            dailySurveyDocument.append("Info useful?", reviewQ1);
            dailySurveyDocument.append("Likely to recommend?", reviewQ2);
            dailySurveyDocument.append("Learn something?", textQ1);
            dailySurveyDocument.append("Improvements suggestions?", textQ2);
            dailySurveyDocument.append("Satisfied with content?", textQ3);

            MongoCollection<Document> users = UserSession.getInstance().openConn();
            Document filter = new Document("_id", UserSession.getInstance().getEmail());
            Document update = new Document("$addToSet", new Document("Reviews", dailySurveyDocument));

            users.updateOne(filter, update);
        }
    }
    @FXML
    public void handleBackToHome(MouseEvent event) {
        UserSession userSession = UserSession.getInstance();
        switchToHome(userSession.getName().join(), event);
    }
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