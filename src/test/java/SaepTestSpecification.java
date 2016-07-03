import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

class SaepTestSpecification {

    static MongoDatabase createDatabaseConnection() {
        MongoClient mongoClient = new MongoClient();
        return mongoClient.getDatabase("SAEP-test");
    }
}
