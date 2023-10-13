module com.mycompany.serenity {
    requires javafx.controls;
    requires javafx.fxml;
    requires firebase.admin;
    requires com.google.auth.oauth2;

    opens com.mycompany.serenity to javafx.fxml;
    exports com.mycompany.serenity;
}
