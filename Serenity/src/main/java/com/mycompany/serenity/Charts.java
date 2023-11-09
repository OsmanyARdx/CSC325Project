package com.mycompany.serenity;

import com.mongodb.client.MongoCollection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ChoiceBox;
import org.bson.Document;

import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class Charts {

    @FXML
    private ChoiceBox<String> selectChart;
    @FXML
    private PieChart pieChart;
    private ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

    private MongoCollection<Document> users;
    private String userEmail;

    @FXML
    public void initialize(){
        users = UserSession.getInstance().openConn();
        userEmail = UserSession.getInstance().getEmail();
        selectChart.getItems().addAll("Mood", "Energy", "Sleep Quality", "Self Care", "Outlook Today");

        selectChart.setOnAction(event -> {
            String selectedCategory = selectChart.getValue();
            updatePieChart(selectedCategory);
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
                    //not sure where to go from here yet

                    //


                }
            }
        }
    }


}
