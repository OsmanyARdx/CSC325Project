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
import javafx.application.Platform;
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
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login {

    private MongoCollection<Document> users;


    @FXML
    private TextField email;
    @FXML
    private TextField password;

    @FXML
    private Label requiredMessage;

    public void initialize(){
        String connectionString = "mongodb+srv://NicholasG:Serenity123@cluster0.ddkjcfa.mongodb.net/?retryWrites=true&w=majority";

        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();

        MongoClient mongoClient = MongoClients.create(settings);
        users = mongoClient.getDatabase("Serenity").getCollection("serenity-users-db");
    }



    @FXML
    public void handleLogin(ActionEvent event) {
        String userEmail = email.getText();
        String pass = password.getText();

        if (!allFieldsFull()) {
            setErrorMessage("All fields are required!");
            return;
        }

        if (!isValidEmailFormat(userEmail)) {
            setErrorMessage("Please enter a valid email format!");
            return;
        }

        doesEmailExist(userEmail)
                .thenAcceptAsync(emailExists -> {
                    if (!emailExists) {
                        Platform.runLater(() -> setErrorMessage("Email not found! Please sign up."));
                    } else {
                        passwordMatch(userEmail, pass)
                                .thenAcceptAsync(passwordMatches -> {
                                    if (!passwordMatches) {
                                        Platform.runLater(() -> setErrorMessage("Invalid password, please try again."));
                                    } else {
                                        Platform.runLater(() -> switchToHome(getName(userEmail).join(), event));
                                    }
                                })
                                .exceptionally(e -> {
                                    System.out.println("An error occurred: " + e.getMessage());
                                    Platform.runLater(() -> setErrorMessage("An error occurred. Please try again later."));
                                    return null;
                                });
                    }
                });
    }

    private void setErrorMessage(String message) {
        requiredMessage.setText(message);
        requiredMessage.setOpacity(1);
    }

    @FXML
    public void handleClickToSignup(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Signup.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void switchToHome(String userName, ActionEvent event) {
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

    public CompletableFuture<Boolean> doesEmailExist(String email) {
        return CompletableFuture.supplyAsync(() -> {
            Bson filter = Filters.eq("_id", email);
            Document existingUser = users.find(filter).first();
            return existingUser != null;
        });
    }

    public CompletableFuture<Boolean> passwordMatch(String email, String password) {
        return CompletableFuture.supplyAsync(() -> {
            Bson filter = Filters.eq("_id", email);
            Document userDoc = users.find(filter).first();

            if (userDoc != null) {
                String hashedPassword = userDoc.getString("password");
                BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hashedPassword);
                return result.verified;
            }
            return false;
        });
    }

    public CompletableFuture<String> getName(String email) {
        return CompletableFuture.supplyAsync(() -> {
            Bson filter = Filters.eq("_id", email);
            Document userDoc = users.find(filter).first();

            return userDoc != null ? userDoc.getString("name") : null;
        });
    }

}
           
