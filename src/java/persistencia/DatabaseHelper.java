package persistencia;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class DatabaseHelper {

    private MongoDatabase mongoDB;

    public DatabaseHelper() {
        this.mongoDB = DatabaseConnection.getDatabaseConnection();
    }

    public void saveIntoCollection(String jsonObject, String collectionName) {

        MongoCollection<Document> collection = getCollection(collectionName);
        Document documentToSave = parseJsonToDocument(jsonObject);
        collection.insertOne(documentToSave);

    }

    private Document parseJsonToDocument(String jsonObject) {
        return Document.parse(jsonObject);
    }

    private MongoCollection<Document> getCollection(String collectionName) {
        return mongoDB.getCollection(collectionName);
    }

}
