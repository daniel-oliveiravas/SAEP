
import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import model.Regra;
import model.Resolucao;
import org.bson.Document;
import org.junit.*;
import persistencia.DatabaseHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelperTest {

    private static MongoClient mongoClient;
    private static MongoDatabase mongoDB;
    private static DatabaseHelper dbHelper;

    private Gson gson = new Gson();

    @BeforeClass
    public static void setup() {
        createDatabaseConnection();
        createCollectionsForTest();
        dbHelper = new DatabaseHelper(mongoDB);
    }

    @AfterClass
    public static void clearTestDatabase() {
        destroyTestCollections();
    }

    @Test
    public void testeFindResolucaoById() {

        String identificadorResolucao = "12345";
        Resolucao resolucao = criaObjetoResolucao(identificadorResolucao, criaListaDeRegras());

        //Converte a resolução para JSON e salva no banco de Dados
        String resolucaoJSON = gson.toJson(resolucao);
        dbHelper.saveIntoCollection(resolucaoJSON, "resolucao");

        Document objectFound = dbHelper.findById("nome", identificadorResolucao, "resolucao");
        Resolucao resolucaoEncontrada = gson.fromJson(gson.toJson(objectFound), Resolucao.class);

        Assert.assertEquals(resolucaoEncontrada.getNome(), identificadorResolucao);

    }

    @Test
    public void testeUpdateResolucao() {

        String identificador = "123";
        Resolucao resolucao = criaObjetoResolucao(identificador, criaListaDeRegras());

        String resolucaoJson = gson.toJson(resolucao);
        dbHelper.saveIntoCollection(resolucaoJson, "resolucao");

        List<Regra> regras = resolucao.getRegras();
        List<String> dependencia = new ArrayList<>();
        dependencia.add("dependenciaTeste");
        regras.add(new Regra("expressaoTeste", 99, 99, dependencia, "Regra adicionada por update", "atualizada"));

        Resolucao resolucaoAlterada = criaObjetoResolucao(identificador, regras);
        String resolucaoAlteradaJSON = gson.toJson(resolucaoAlterada);
        dbHelper.updateCollectionObject("nome", resolucao.getNome(), resolucaoAlteradaJSON, "resolucao");

        Document resolucaoDocument = dbHelper.findById("nome", resolucaoAlterada.getNome(), "resolucao");
        Resolucao resolucaoAtualizada = gson.fromJson(gson.toJson(resolucaoDocument), Resolucao.class);

        Assert.assertEquals(2, resolucaoAtualizada.getRegras().size());

    }

    private Resolucao criaObjetoResolucao(String identificadorResolucao, List<Regra> listaRegras) {

        return new Resolucao(
                identificadorResolucao,
                "Descrição da resolução",
                new Date(),
                listaRegras
        );
    }

    private List<Regra> criaListaDeRegras(){
        List<String> dependencias = new ArrayList<>();
        dependencias.add("a");
        dependencias.add("b");

        Regra regraTeste = new Regra("a = b + c", 10, 5, dependencias, "Descrição da Regra", "Variável");
        List<Regra> listaRegras = new ArrayList<>();
        listaRegras.add(regraTeste);
        return listaRegras;
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
