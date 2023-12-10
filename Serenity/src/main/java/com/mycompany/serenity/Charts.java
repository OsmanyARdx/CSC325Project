package com.mycompany.serenity;

import com.mongodb.client.MongoCollection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.bson.Document;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.mongodb.client.model.Filters.eq;

public class Charts {

    @FXML
    private ChoiceBox<String> selectChart;
    @FXML
    private PieChart pieChart;
    @FXML
    private Label chartLabel;
    private ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

    private MongoCollection<Document> users;
    private String userEmail;

    @FXML
    public void initialize(){
        users = UserSession.getInstance().openConn();
        userEmail = UserSession.getInstance().getEmail();
        selectChart.getItems().addAll("Mood", "Energy Level", "Sleep Quality", "Self Care", "Outlook Today");

        selectChart.setOnAction(event -> {
            String selectedCategory = selectChart.getValue();
            updatePieChart(selectedCategory);
            chartLabel.setText(selectedCategory);
        });

    }

    public void updatePieChart(String selectedCategory){
        pieChartData.clear();

        Document surveyData = users.find(eq("_id", userEmail)).first();

        if (surveyData != null){
            List<Document> dailySurveys = surveyData.get("Daily surveys", List.class);
            int totalSurveys = dailySurveys.size();

            if (dailySurveys != null && !dailySurveys.isEmpty()){
                for(Document survey : dailySurveys){
                    if (survey.containsKey(selectedCategory)) {
                        String categoryValue = survey.getString(selectedCategory);
                        addToPieChart(selectedCategory, categoryValue);
                    }
                }
                pieChart.setData(pieChartData);
            }
            else{
                System.out.println("No data is inserted in the database");
            }
        }
    }

    public void addToPieChart(String category, String value){
        boolean found = false;
        for (PieChart.Data data : pieChartData) {
            if (data.getName().equals(value)) {
                data.setPieValue(data.getPieValue() + 1);
                found = true;
                break;
            }
        }

        // If the category doesn't exist, add a new PieChart.Data
        if (!found) {
            pieChartData.add(new PieChart.Data(value, 1));
        }
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
     * Route user to meditation page
     * @param event on click
     */
    @FXML
    public void handleClickMeditate(ActionEvent event){
        switchPage(event, "Meditate.fxml");
    }

    /**
     * Helper method for page routing
     * @param event on click
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