
import br.ufg.inf.es.saep.sandbox.dominio.Regra;
import br.ufg.inf.es.saep.sandbox.dominio.Resolucao;
import br.ufg.inf.saep.persistencia.MongoResolucaoRepository;
import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.*;
import br.ufg.inf.saep.persistencia.DatabaseHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelperTest extends SaepTestSpecification {

    private static MongoDatabase mongoDB;
    private static DatabaseHelper dbHelper;

    private Gson gson = new Gson();

    @BeforeClass
    public static void testClassConfig() {
        mongoDB = createDatabaseConnection();
        dbHelper = new DatabaseHelper(mongoDB);
    }

    @Before
    public void setup() {
        createCollectionsForTest();
    }

    @After
    public void clearTestDatabase() {
        destroyTestCollections();
    }

    @Test
    public void testeFindResolucaoById() {

        String identificadorResolucao = "12345";
        Resolucao resolucao = criaObjetoResolucao(identificadorResolucao, criaListaDeRegras());

        //Converte a resolução para JSON e salva no banco de Dados
        String resolucaoJSON = gson.toJson(resolucao);
        dbHelper.saveIntoCollection(resolucaoJSON, "resolucao");

        Document objectFound = dbHelper.findById("id", identificadorResolucao, "resolucao");
        Resolucao resolucaoEncontrada = gson.fromJson(gson.toJson(objectFound), Resolucao.class);

        Assert.assertEquals(resolucaoEncontrada.getId(), identificadorResolucao);
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
        regras.add(new Regra(1, "Regra adicionada por update", 99, 99, "variavel", "dependenciaTeste = 0", "", "", "tipo1", 10, dependencia));

        Resolucao resolucaoAlterada = criaObjetoResolucao(identificador, regras);
        String resolucaoAlteradaJSON = gson.toJson(resolucaoAlterada);
        dbHelper.updateCollectionObject("id", resolucao.getId(), resolucaoAlteradaJSON, "resolucao");

        Document resolucaoDocument = dbHelper.findById("id", resolucaoAlterada.getId(), "resolucao");
        Resolucao resolucaoAtualizada = gson.fromJson(gson.toJson(resolucaoDocument), Resolucao.class);

        Assert.assertEquals(2, resolucaoAtualizada.getRegras().size());

    }

    @Test
    public void testeRemoveResolucao() {

        String idResolucao = "123";
        Resolucao resolucao = criaObjetoResolucao(idResolucao, criaListaDeRegras());
        MongoCollection<Document> resolucaoCollection = mongoDB.getCollection("resolucao");

        dbHelper.saveIntoCollection(gson.toJson(resolucao), "resolucao");

        boolean resultado = dbHelper.removeObjectFromCollection("id", idResolucao, MongoResolucaoRepository.resolucaoCollection);

        Assert.assertEquals(true, resultado);
    }

    private Resolucao criaObjetoResolucao(String identificadorResolucao, List<Regra> listaRegras) {

        return new Resolucao(
                identificadorResolucao,
                "Nome da resolução",
                "Descrição da resolução",
                new Date(),
                listaRegras
        );
    }

    private List<Regra> criaListaDeRegras() {
        List<String> dependencias = new ArrayList<>();
        dependencias.add("a");
        dependencias.add("b");

        Regra regraTeste = new Regra(4, "Descrição da Regra", 10, 5, "a", "a = b + c", "", "", "idTipoRelato", 1, dependencias);
        List<Regra> listaRegras = new ArrayList<>();
        listaRegras.add(regraTeste);
        return listaRegras;
    }


    private static void createCollectionsForTest() {
        mongoDB.createCollection("resolucao");
    }

    private static void destroyTestCollections() {
        mongoDB.getCollection("resolucao").drop();
    }
}
