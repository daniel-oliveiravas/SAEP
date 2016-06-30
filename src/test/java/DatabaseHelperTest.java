
import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import model.Regra;
import model.Resolucao;
import org.bson.Document;
import org.junit.*;
import persistencia.DatabaseHelper;

import java.util.ArrayList;
import java.util.Date;

import static com.mongodb.client.model.Filters.eq;

public class DatabaseHelperTest {

    private static MongoClient mongoClient;
    private static MongoDatabase mongoDB;

    private Gson gson = new Gson();

    @BeforeClass
    public static void setup() {
        createDatabaseConnection();
    }

    @AfterClass
    public void destroyCollections() {
        mongoDB.getCollection("resolucao").drop();
    }

    @Test
    public void testeFindResolucaoById() {
        DatabaseHelper dbHelper = new DatabaseHelper(mongoDB);

        mongoDB.createCollection("resolucao");

        String identificadorResolucao = "12345";
        Resolucao resolucao = criaObjetoResolucao(identificadorResolucao);

        //Converte a resolução para JSON e salva no banco de Dados
        String resolucaoJSON = gson.toJson(resolucao);
        dbHelper.saveIntoCollection(resolucaoJSON, "resolucao");

        Document objectFound = dbHelper.findById("identificador", identificadorResolucao, "resolucao");
        Resolucao resolucaoEncontrada = gson.fromJson(gson.toJson(objectFound), Resolucao.class);

        Assert.assertEquals(resolucaoEncontrada.getIdentificador(), identificadorResolucao);

    }

    private Resolucao criaObjetoResolucao(String identificadorResolucao) {
        return new Resolucao(
                identificadorResolucao,
                "Descrição da resolução",
                new Date(),
                new ArrayList<Regra>()
        );
    }

    private static void createDatabaseConnection() {
        mongoClient = new MongoClient();
        mongoDB = mongoClient.getDatabase("SAEP-test");
    }
}
