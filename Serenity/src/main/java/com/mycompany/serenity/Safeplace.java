package com.mycompany.serenity;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;

public class Safeplace {

    @FXML
    private TextArea Q1TXTArea, Q2TXTArea, Q3TXTArea, chatHistory;

    @FXML
    private TextField userMessageInput;

    @FXML
    public void initialize(){
        chatHistory.appendText("Bot: Hello! I'm your personal mental health assistant. Use the text bar below to discuss any issues with me.\n\n");
    }

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

    @FXML
    public void handleClickEmergencyResources(ActionEvent event) {
        switchPage(event, "EmergencyResources.fxml");
    }

    //Meditation page method goes here
    @FXML
    public void handleClickMeditate(ActionEvent event) {
        switchPage(event, "Meditate.fxml");
    }

    @FXML
    public void handleClickDailySurvey(ActionEvent event) {
        switchPage(event, "SelfAssessment.fxml");
    }

    @FXML
    public void handleClickWellnessTips(ActionEvent event) {
        switchPage(event, "WellnessTips.fxml");
    }

    @FXML
    public void handleBackToHome(MouseEvent event) {
        UserSession userSession = UserSession.getInstance();
        switchToHome(userSession.getName().join(), event);
    }

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

    @FXML
    public void handleSendMessage() {
        String userMessage = userMessageInput.getText();

        if (!userMessage.isEmpty()) {
            // Create and start a new thread to handle the GPT request
            Thread gptThread = new Thread(() -> {
                String botResponse = handleGPTRequest(userMessage);

                // Update the JavaFX UI on the JavaFX Application Thread
                Platform.runLater(() -> {
                    displayMessage("You: " + userMessage);
                    displayMessage("Bot: " + botResponse);
                    userMessageInput.clear();
                });
            });

            gptThread.start();
        }
    }


    private void displayMessage(String message) {
        chatHistory.appendText(message + "\n\n");
    }

    public String handleGPTRequest(String message){
        String url = "https://api.openai.com/v1/chat/completions";
        String apiKey = System.getenv("apiKey");
        String model = "gpt-3.5-turbo";

        try{
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer " + apiKey);
            con.setRequestProperty("Content-Type", "application/json");

            String body = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + message + "\"}]}";
            con.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
            writer.write(body);
            writer.flush();
            writer.close();
            //Get the response
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }
            in.close();

            return extractContentFromJson(String.valueOf(response));

        } catch(IOException e){
            System.out.println(e);
        }
        return null;
    }

    public String extractContentFromJson(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            JSONArray choicesArray = jsonObject.getJSONArray("choices");

            if (!choicesArray.isEmpty()) {
                JSONObject firstChoice = choicesArray.getJSONObject(0);
                JSONObject messageObject = firstChoice.getJSONObject("message");

                if (messageObject.has("content")) {
                    return messageObject.getString("content");
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return null; // Return null if extraction fails
    }



}