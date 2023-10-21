package com.mycompany.serenity;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {


        //creating the starting page
        scene = new Scene(loadFXML("LandingPage"), 1080, 720);
        stage.setScene(scene);
        stage.setTitle("Serenity");
        String css = getClass().getResource("styles.css").toExternalForm();
        scene.getStylesheets().add(css);
        scene.getStylesheets().add(css);
        stage.show();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}