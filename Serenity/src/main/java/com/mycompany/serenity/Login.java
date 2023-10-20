package com.mycompany.serenity;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.IOException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login {

    @FXML
    private TextField email;
    @FXML
    private TextField password;

    @FXML
    private Label requiredMessage;

    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(
            2, 2, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

    @FXML
    public void handleLogin(ActionEvent event) {

        String userEmail = email.getText();
        String pass = password.getText();

        if (!allFieldsFull()) {
            requiredMessage.setText("All fields are required!");
            requiredMessage.setOpacity(1);
            return;
        }

        if (!isValidEmailFormat(userEmail)) {
            requiredMessage.setText("Please enter a valid email format!");
            requiredMessage.setOpacity(1);
            return;
        }

        // Asynchronously check if the email exists
        Future<Boolean> emailExistsFuture = executor.submit(() -> doesEmailExist(userEmail));

        try {
            if (!emailExistsFuture.get()) {
                requiredMessage.setText("Email not found! Please sign up.");
                requiredMessage.setOpacity(1);
            } else {
                // Asynchronously check if the password matches
                Future<Boolean> passwordMatchFuture = executor.submit(() -> passwordMatch(userEmail, pass));

                if (!passwordMatchFuture.get()) {
                    requiredMessage.setText("Invalid password, please try again.");
                    requiredMessage.setOpacity(1);
                } else {
                    switchToLandingPage(event);
                    shutdownExecutor();
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    public void handleClickToSignup(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("signup.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void switchToLandingPage(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("landingPage.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public Boolean allFieldsFull() {
        return !email.getText().isEmpty() && !password.getText().isEmpty();
    }

    public Boolean isValidEmailFormat(String email) {
        String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean doesEmailExist(String email) {
        try (MongoClient mongoClient = openConn()) {
            MongoCollection<Document> users = mongoClient.getDatabase("Serenity").getCollection("serenity-users");
            Bson filter = Filters.eq("_id", email);
            Document existingUser = users.find(filter).first();
            return existingUser != null;
        }
    }

    public boolean passwordMatch(String email, String password) {
        try (MongoClient mongoClient = openConn()) {
            MongoCollection<Document> users = mongoClient.getDatabase("Serenity").getCollection("serenity-users");
            Bson filter = Filters.eq("_id", email);
            Document userDoc = users.find(filter).first();

            if (userDoc != null) {
                String hashedPassword = userDoc.getString("password");
                BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hashedPassword);
                return result.verified;
            }
            return false;
        }
    }

    public MongoClient openConn() {
        String connectionString = "mongodb+srv://Serenity:Serenity123@serenity.u9qpr7n.mongodb.net/?retryWrites=true&w=majority";

        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();

        return MongoClients.create(settings);
    }

    public void shutdownExecutor(){
        executor.shutdown();
    }
}
           
