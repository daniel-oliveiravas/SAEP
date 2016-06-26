package persistencia;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class DatabaseConnection {

    private static MongoClient mongoClient = new MongoClient();
    ;
    private static MongoDatabase mongoDB = mongoClient.getDatabase("SAEP");
    ;

    public static MongoDatabase getDatabaseConnection() {
        mongoDB.getCollection("teste");
        return mongoDB;
    }
}
