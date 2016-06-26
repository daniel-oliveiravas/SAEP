import com.mongodb.client.MongoDatabase;
import persistencia.DatabaseConnection;

public class Main {

    public static void main(String[] args) {
        MongoDatabase db = DatabaseConnection.getDatabaseConnection();
        System.out.println(db);
    }
}
