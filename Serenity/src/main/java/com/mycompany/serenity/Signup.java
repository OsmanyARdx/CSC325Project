package com.mycompany.serenity;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class Signup {


    @FXML
    private Button clickToLogin;
    @FXML
    private Button finishSignup;
    @FXML
    private TextField name;
    @FXML
    private TextField email;
    @FXML
    private TextField password;
    @FXML
    private TextField confirmPass;
    @FXML
    private Label requiredMessage;

    @FXML
    public void handleClickToLogin(ActionEvent event){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch(IOException e){
            System.out.println(e);
        }
    }
    @FXML
    public void handleFinishSignup() {
        String userName = name.getText();
        String userEmail = email.getText();
        String pass = password.getText();
        String confirm = confirmPass.getText();
        if(!allFieldsFull()){
            requiredMessage.setOpacity(1);
        }
        if (!isValidEmail(userEmail) && allFieldsFull()){
            requiredMessage.setText("Please enter valid email format!");
            requiredMessage.setOpacity(1);
        }
        if (!pass.equals(confirm) && allFieldsFull()){
            requiredMessage.setText("Passwords must match!");
            requiredMessage.setOpacity(1);
        }
        if (pass.equals(confirm) && allFieldsFull() && isValidEmail(userEmail)) {
            try {
                openConn();
                registerUser(userName, userEmail, pass);
                App.setRoot("login");

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    public Boolean isValidEmail(String email){
        String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public Boolean allFieldsFull(){
        if(name.getText().isEmpty() || email.getText().isEmpty() || password.getText().isEmpty() || confirmPass.getText().isEmpty()){
            return false;
        } else{
            return true;
        }
    }

    public void openConn(){
        try {

            FirebaseOptions options = FirebaseOptions.builder()
                    .setDatabaseUrl("https://serenity-971f6-default-rtdb.firebaseio.com")
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (IllegalStateException e){
            System.out.println("Firebase initialization failed: " + e.getMessage());
        }
    }
    public void registerUser(String name, String email, String password){
        try{
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(email)
                    .setPassword(password)
                    .setDisplayName(name);

            UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
            System.out.println("Successfully created new user: " + userRecord.getUid());
        } catch (FirebaseAuthException e){
            System.out.println("Error creating new user: " + e.getMessage());
        }
    }

}
