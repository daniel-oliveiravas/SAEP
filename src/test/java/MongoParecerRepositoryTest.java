import br.ufg.inf.es.saep.sandbox.dominio.Nota;
import br.ufg.inf.es.saep.sandbox.dominio.Parecer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.*;
import persistencia.DatabaseHelper;
import persistencia.MongoParecerRepository;
import persistencia.custom.NotaDeserialize;

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

        Assert.assertEquals(idParecer, parecerEncontrado.getId());
        Assert.assertEquals(2, parecerEncontrado.getNotas().size());
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

        Assert.assertEquals(3, parecerAlterado.getNotas().size());

    }

    private static void createParecerCollectionsForTest() {
        mongoDB.createCollection(MongoParecerRepository.parecerCollection);
    }

    private static void destroyParecerTestCollections() {
        mongoDB.getCollection(MongoParecerRepository.parecerCollection).drop();
    }

}
