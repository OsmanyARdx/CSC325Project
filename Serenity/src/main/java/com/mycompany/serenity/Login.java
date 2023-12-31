package com.mycompany.serenity;

import at.favre.lib.crypto.bcrypt.BCrypt;
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
        users = UserSession.getInstance().openConn();
    }


    /**
     * Checks to make sure the login is valid, then routes user to home page
     * @param event
     */
    @FXML
    public void handleLogin(ActionEvent event) {
        UserSession userSession = UserSession.getInstance();
        String userEmail = email.getText();
        userSession.setEmail(userEmail);
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
                                        Platform.runLater(() -> switchToHome(userSession.getName().join(), event));
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

    /**
     * Error message displaying to user which fields are incorrect
     * @param message
     */
    private void setErrorMessage(String message) {
        requiredMessage.setText(message);
        requiredMessage.setOpacity(1);
    }

    /**
     * Routes user to signup page
     * @param event
     */
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

    /**
     * Helper method for routing user to home page
     * Creates a singleton instance using UserSession class
     * @param userName
     * @param event
     */
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

    /**
     * Helper method makes sure user filled out all fields
     * @return
     */
    public Boolean allFieldsFull() {
        return !email.getText().isEmpty() && !password.getText().isEmpty();
    }

    /**
     * Regex expression to make sure the user is following correct email format
     * "example@example.com"
     * @param email
     * @return
     */
    public Boolean isValidEmailFormat(String email) {
        String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Helper method makes sure entered email exists in DB
     * @param email
     * @return
     */
    public CompletableFuture<Boolean> doesEmailExist(String email) {
        return CompletableFuture.supplyAsync(() -> {
            Bson filter = Filters.eq("_id", email);
            Document existingUser = users.find(filter).first();
            return existingUser != null;
        });
    }

    /**
     * Helper method for making sure user password is correct
     * @param email
     * @param password
     * @return
     */
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


}
           
