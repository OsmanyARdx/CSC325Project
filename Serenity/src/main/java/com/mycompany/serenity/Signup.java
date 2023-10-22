package com.mycompany.serenity;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
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
import org.bson.Document;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
    public void handleFinishSignup(ActionEvent event) {
        String userName = name.getText();
        String userEmail = email.getText();
        String pass = password.getText();
        String confirm = confirmPass.getText();


        //User didn't fill out all the fields
        if(!allFieldsFull()){
            setErrorMessage("All fields are required!");
        }
        //User filled out all the fields, but the email format isn't correct ex: serenity@gmail.com
        if (!isValidEmail(userEmail) && allFieldsFull()){
            setErrorMessage("Please enter valid email format!");
        }

        //User's passwords don't match
        if (!pass.equals(confirm) && allFieldsFull()){
            setErrorMessage("Passwords must match!");
        }
        //All values pass check conditions and user's sign up info is sent to DB
        if (pass.equals(confirm) && allFieldsFull() && isValidEmail(userEmail)) {
            addUser(userName, userEmail, pass);
            handleClickToLogin(event);

        }
    }

    private void setErrorMessage(String message){
        requiredMessage.setText(message);
        requiredMessage.setOpacity(1);
    }
    public Boolean isValidEmail(String email){
        String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public Boolean allFieldsFull(){
        return !name.getText().isEmpty() && !email.getText().isEmpty() && !password.getText().isEmpty() && !confirmPass.getText().isEmpty();
    }

    public void addUser(String name, String email, String password){

        MongoCollection<Document> users = openConn();

        BCrypt.Hasher hasher = BCrypt.withDefaults();
        String hashedPassword = hasher.hashToString(12, password.toCharArray());

        Document userDoc = new Document("_id", email)
                .append("name", name)
                .append("password", hashedPassword);

        users.insertOne(userDoc);
    }
    public MongoCollection<Document> openConn() {
        String connectionString = "mongodb+srv://Serenity:Serenity123@serenity.u9qpr7n.mongodb.net/?retryWrites=true&w=majority";

        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();

        try (MongoClient mongoClient = MongoClients.create(settings)) {
            MongoDatabase serenityDB = mongoClient.getDatabase("Serenity");
            return serenityDB.getCollection("serenity-users");
        }
    }


}
