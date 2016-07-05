import br.ufg.inf.es.saep.sandbox.dominio.Nota;
import br.ufg.inf.es.saep.sandbox.dominio.Parecer;
import br.ufg.inf.es.saep.sandbox.dominio.Radoc;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.*;
import br.ufg.inf.saep.persistencia.DatabaseHelper;
import br.ufg.inf.saep.persistencia.MongoParecerRepository;
import br.ufg.inf.saep.persistencia.custom.NotaDeserialize;

import static org.junit.Assert.*;

public class MongoParecerRepositoryTest extends SaepTestSpecification {

    private static MongoParecerRepository parecerRepository;
    private static DatabaseHelper dbHelper;
    private static MongoDatabase mongoDB;
    private static Gson gson;

    @BeforeClass
    public static void setup() {
        mongoDB = createDatabaseConnection();
        dbHelper = new DatabaseHelper(mongoDB);
        parecerRepository = new MongoParecerRepository(dbHelper);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Nota.class, new NotaDeserialize());
        gson = gsonBuilder.create();
    }

    @Before
    public void createParecerCollection() {
        createParecerCollectionsForTest();
    }

    @After
    public void destroyParecerCollection() {
        destroyParecerTestCollections();
    }

    @Test
    public void persisteParecerTest() {

        String idParecer = "identificadorParecer";
        Parecer parecer = criarParecerTeste(idParecer);

        parecerRepository.persisteParecer(parecer);
        Document parecerDocument = dbHelper.findById("id", idParecer, MongoParecerRepository.parecerCollection);

        Parecer parecerEncontrado = gson.fromJson(gson.toJson(parecerDocument), Parecer.class);

        assertEquals(idParecer, parecerEncontrado.getId());
        assertEquals(2, parecerEncontrado.getNotas().size());
    }

    @Test
    public void adicionaNotaTest() {

        String idParecer = "identificador de Parecer";
        Parecer parecer = criarParecerTeste(idParecer);

        parecerRepository.persisteParecer(parecer);
        Nota novaParaAdicionar = criaNota(criaRelato("V - 5.5"), criaRelato("V - 3.0"), "Categoria da tabela está incorreta.");

        parecerRepository.adicionaNota(idParecer, novaParaAdicionar);

        Document parecerDocument = dbHelper.findById("id", idParecer, MongoParecerRepository.parecerCollection);
        Parecer parecerAlterado = gson.fromJson(gson.toJson(parecerDocument), Parecer.class);

        assertEquals(3, parecerAlterado.getNotas().size());

    }

    @Test
    public void atualizaFundamentacaoTest() {

        String idParecer = "novo parecer";
        Parecer parecer = criarParecerTeste(idParecer);

        parecerRepository.persisteParecer(parecer);

        String fundamentacaoParaAlterar = "Fundamentação para alterar";
        parecerRepository.atualizaFundamentacao(idParecer, fundamentacaoParaAlterar);

        Parecer parecerAlterado = parecerRepository.byId(idParecer);

        assertEquals(fundamentacaoParaAlterar, parecerAlterado.getFundamentacao());

    }

    @Test
    public void byIdTest() {

        String idParecer = "idParecerParaBuscar";
        persisteParecerParaTeste(idParecer);

        Parecer parecerEncontrado = parecerRepository.byId(idParecer);

        assertEquals(idParecer, parecerEncontrado.getId());

    }

    @Test
    public void removeParecer() {

        String idParecer = "idParecerParaRemover";
        persisteParecerParaTeste(idParecer);

        Parecer parecerPersistido = parecerRepository.byId(idParecer);
        parecerRepository.removeParecer(idParecer);

        Parecer parecerRemovido = parecerRepository.byId(idParecer);

        assertNotNull(parecerPersistido);
        assertNull(parecerRemovido);
    }

    @Test
    public void persisteRadoc() {

        String idRadoc = "idRadoc";
        Radoc radoc = criarRadoc(idRadoc, 1995);

        parecerRepository.persisteRadoc(radoc);

        Document radocDocument = dbHelper.findById("id", idRadoc, MongoParecerRepository.radocCollection);

        assertNotNull(radocDocument);

    }

    @Test
    public void radocById() {

        String idRadoc = "idRadocParaBuscar";
        persisteRadocParaTeste(idRadoc, 1900);

        Radoc radocEncontrado = parecerRepository.radocById(idRadoc);

        assertEquals(idRadoc, radocEncontrado.getId());
        assertEquals(3, radocEncontrado.getRelatos().size());
    }

    @Test
    public void removeRadocSemReferencia() {

        persisteParecerParaTeste("idParecer");

        String idRadoc = "radocParaRemover";
        persisteRadocParaTeste(idRadoc, 1800);

        parecerRepository.removeRadoc(idRadoc);

        Radoc radocEncontrado = parecerRepository.radocById(idRadoc);

        assertNull(radocEncontrado);

    }

    @Test
    public void removeRadocDeParecerReferenciado() {

        String idParecer = "parecerComReferenciaParaRadoc";
        Parecer parecer = criarParecerTeste(idParecer);
        parecerRepository.persisteParecer(parecer);

        /* Persistindo um radoc com o identificador que é referenciado por um parecer */
        String idRadoc = parecer.getRadocs().get(0);
        persisteRadocParaTeste(idRadoc, 1800);

        parecerRepository.removeRadoc(idRadoc);

        Radoc radocEncontrado = parecerRepository.radocById(idRadoc);

        assertNotNull(radocEncontrado);

    }

    private static void createParecerCollectionsForTest() {
        mongoDB.createCollection(MongoParecerRepository.parecerCollection);
        mongoDB.createCollection(MongoParecerRepository.radocCollection);
    }

    private static void destroyParecerTestCollections() {
        mongoDB.getCollection(MongoParecerRepository.parecerCollection).drop();
        mongoDB.getCollection(MongoParecerRepository.radocCollection).drop();
    }

    private void persisteParecerParaTeste(String identificador) {
        parecerRepository.persisteParecer(criarParecerTeste(identificador));
    }

    private void persisteRadocParaTeste(String identificador, int anoBase) {
        parecerRepository.persisteRadoc(criarRadoc(identificador, anoBase));
    }

}
