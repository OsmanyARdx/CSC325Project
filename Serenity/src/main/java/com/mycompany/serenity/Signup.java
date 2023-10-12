package com.mycompany.serenity;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
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
    public void handleClickToLogin(){
        try{
            App.setRoot("login");

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }
    @FXML
    public void handleFinishSignup() {
        if(!allFieldsFull()){
            requiredMessage.setOpacity(1);
        }
        if (!isValidEmail(email.getText()) && allFieldsFull()){
            requiredMessage.setText("Please enter valid email format!");
            requiredMessage.setOpacity(1);
        }
        if (!password.getText().equals(confirmPass.getText()) && allFieldsFull()){
            requiredMessage.setText("Passwords must match!");
            requiredMessage.setOpacity(1);
        }
        if (password.getText().equals(confirmPass.getText()) && allFieldsFull() && isValidEmail(email.getText())) {
            try {
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

}
