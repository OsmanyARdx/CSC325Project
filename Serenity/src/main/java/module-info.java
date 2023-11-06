module com.mycompany.serenity {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires bcrypt;
    requires org.mongodb.driver.core;
    //requires javafx.media;
    requires org.json;

    opens com.mycompany.serenity to javafx.fxml;
    exports com.mycompany.serenity;
}
