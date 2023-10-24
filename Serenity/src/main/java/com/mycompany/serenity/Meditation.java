package com.mycompany.serenity;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import java.io.IOException;
import javafx.scene.Scene;

public class Meditation extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        VBox root = FXMLLoader.load(getClass().getResource("Meditation.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Testing Hyperlink");
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }

}
