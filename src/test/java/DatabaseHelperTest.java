
import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import model.Regra;
import model.Resolucao;
import org.bson.Document;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import persistencia.DatabaseHelper;

import java.util.ArrayList;
import java.util.Date;

public class DatabaseHelperTest {

    private static MongoClient mongoClient;
    private static MongoDatabase mongoDB;

    private Gson gson = new Gson();

    @BeforeClass
    public static void setup() {
        createDatabaseConnection();
        createCollectionsForTest();
    }

    @AfterClass
    public static void clearTestDatabase() {
        destroyTestCollections();
    }

    @Test
    public void testeFindResolucaoById() {
        DatabaseHelper dbHelper = new DatabaseHelper(mongoDB);


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

    private static void createCollectionsForTest() {
        mongoDB.createCollection("resolucao");
    }

    private static void destroyTestCollections() {
        mongoDB.getCollection("resolucao").drop();

    }
}
