
import br.ufg.inf.es.saep.sandbox.dominio.Regra;
import br.ufg.inf.es.saep.sandbox.dominio.Resolucao;
import br.ufg.inf.saep.persistencia.DatabaseHelper;
import br.ufg.inf.saep.persistencia.MongoResolucaoRepository;
import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DatabaseHelperTest extends SaepTestSpecification {

    private static MongoDatabase mongoDB;
    private static DatabaseHelper dbHelper;

    private static String resolucaoCollectionNameForTest = "resolucaoTest";
    private static String tipoCollectionNameForTest = "tipoTest";

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

        assertEquals(resolucaoEncontrada.getId(), identificadorResolucao);
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

        assertEquals(2, resolucaoAtualizada.getRegras().size());
    }

    @Test
    public void testeRemoveResolucao() {

        String idResolucao = "123";
        Resolucao resolucao = criaObjetoResolucao(idResolucao, criaListaDeRegras());
        MongoCollection<Document> resolucaoCollection = mongoDB.getCollection("resolucao");

        dbHelper.saveIntoCollection(gson.toJson(resolucao), "resolucao");

        boolean resultado = dbHelper.removeObjectFromCollection("id", idResolucao, MongoResolucaoRepository.resolucaoCollection);

        assertEquals(true, resultado);
    }

    @Test
    public void saveIntoCollectionReturningDocumentTest() {
        String identificadorResolucao = "123";
        Resolucao resolucao = criaObjetoResolucao(identificadorResolucao, criaListaDeRegras());
        String resolucaoJson = gson.toJson(resolucao);
        Document savedDocument = dbHelper.saveIntoCollectionReturningDocument(resolucaoJson, resolucaoCollectionNameForTest);

        assertEquals(savedDocument.getString("id"), identificadorResolucao);
        assertNotNull(savedDocument);
    }

    @Test
    public void findObjectFromCollectionWithFilterTest() {
        String idResolucao = "123filter";
        Resolucao resolucao = persisteResolucaoParaTeste(idResolucao);

        Document query = new Document("nome", resolucao.getNome());
        Document document = dbHelper.findObjectFromCollectionWithFilter(resolucaoCollectionNameForTest, query);

        assertNotNull(document);
    }

    @Test
    public void findAllTest() {
        String idResolucao = "idResolucao";
        String outroIdResolucao = "outroIdResolucao";
        ArrayList<String> ids = new ArrayList<>();
        ids.add(idResolucao);
        ids.add(outroIdResolucao);

        Resolucao resolucao = persisteResolucaoParaTeste(idResolucao);
        Resolucao outraResolucao = persisteResolucaoParaTeste(outroIdResolucao);
        Iterable<Document> listaResolucoes = dbHelper.findAll(resolucaoCollectionNameForTest);

        int count = 0;
        ArrayList<String> idsBuscados = new ArrayList<>();
        for (Document resolucaoDocument : listaResolucoes) {
            idsBuscados.add(resolucaoDocument.getString("id"));
            count++;
        }
        Collections.sort(ids);
        Collections.sort(idsBuscados);
        assertEquals(ids, idsBuscados);
        assertEquals(2, count);
    }

    @Test
    public void findAllLikeTest() {
        String idResolucao = "idFindAllLike";
        Resolucao resolucao = persisteResolucaoParaTeste(idResolucao);
        String nomeResolucao = resolucao.getNome();

        Iterable<Document> resolucaoDocuments = dbHelper.findAllLike("nome", nomeResolucao, resolucaoCollectionNameForTest);

        List<Document> documents = new ArrayList<>();

        String idResolucaoEncontrada = "";
        for (Document document : resolucaoDocuments) {
            idResolucaoEncontrada = document.getString("id");
        }
        assertEquals(idResolucao, idResolucaoEncontrada);
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
        mongoDB.createCollection(resolucaoCollectionNameForTest);
        mongoDB.createCollection(tipoCollectionNameForTest);
    }

    private static void destroyTestCollections() {
        mongoDB.getCollection(resolucaoCollectionNameForTest).drop();
        mongoDB.getCollection(tipoCollectionNameForTest).drop();
    }

    private Resolucao persisteResolucaoParaTeste(String idResolucao) {
        Resolucao resolucao = criaObjetoResolucao(idResolucao, criaListaDeRegras());
        String resolucaoJson = gson.toJson(resolucao);
        dbHelper.saveIntoCollection(resolucaoJson, resolucaoCollectionNameForTest);
        return resolucao;
    }
}
