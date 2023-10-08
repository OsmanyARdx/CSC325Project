module com.mycompany.serenity {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.serenity to javafx.fxml;
    exports com.mycompany.serenity;
}
