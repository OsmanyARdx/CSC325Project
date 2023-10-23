package com.mycompany.serenity;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

//Vanilla swirl
public class Safeplace implements Initializable {

    @FXML
    private ChoiceBox<String> moodChoiceBox;

    @FXML
    private TextArea Q2TXTArea, Q3TXTArea;

    @FXML
    private Label Q1, Q2, Q3;

    @FXML
    private Button send_to_diary;

    @FXML
    public void onClick_bn_diary(ActionEvent e) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(moodChoiceBox.getValue() + "\n");
        sb.append(Q2TXTArea.getText() + "\n");
        sb.append(Q3TXTArea.getText() + "\n");


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
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // You can initialize the ChoiceBox items here
        moodChoiceBox.getItems().addAll("Happy", "Sad", "Excited", "Angry", "Calm");
    }
}
