package com.mycompany.serenity;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Login {

    @FXML
    private Button clickToSignUp;
    @FXML
    private Button login;

    @FXML
    public void handleClickToSignup(){
        try{
            App.setRoot("signup");

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }
    @FXML
    public void handleLogin(){
        try{
            App.setRoot("landingPage");

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }
}