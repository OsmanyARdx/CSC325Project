package com.mycompany.serenity;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.concurrent.CompletableFuture;

public class UserSession {
    private static UserSession instance;
    private String email;

    private Boolean isSurveySubmitted;

    private UserSession(){
        //leave empty
    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public CompletableFuture<String> getName() {
        return CompletableFuture.supplyAsync(() -> {
            Bson filter = Filters.eq("_id", email);
            Document userDoc = openConn().find(filter).first();

            return userDoc != null ? userDoc.getString("name") : null;
        });
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail(){ return this.email; }
    public MongoCollection<Document> openConn(){
        String connectionString = "mongodb+srv://NicholasG:Serenity123@cluster0.ddkjcfa.mongodb.net/?retryWrites=true&w=majority";

        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();

        MongoClient mongoClient = MongoClients.create(settings);
        return mongoClient.getDatabase("Serenity").getCollection("serenity-users-db");
    }
}

