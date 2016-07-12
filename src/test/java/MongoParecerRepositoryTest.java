import br.ufg.inf.es.saep.sandbox.dominio.*;
import br.ufg.inf.saep.persistencia.DatabaseHelper;
import br.ufg.inf.saep.persistencia.MongoParecerRepository;
import br.ufg.inf.saep.persistencia.custom.NotaDeserialize;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

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

    @Test(expected = IdentificadorExistente.class)
    public void persisteParecerComIdentificadorJaExistenteTest() {

        String idParecer = "identificadorParecer";
        Parecer parecer = criarParecerTeste(idParecer);
        Parecer outroParecer = criarParecerTeste(idParecer);

        parecerRepository.persisteParecer(parecer);
        parecerRepository.persisteParecer(outroParecer);
    }

    @Test
    public void adicionaNotaNaoExistenteComSucessoTest() {

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
    public void adicionaNotaJaExistenteComSucessoTest() {

        String idParecer = "identificador de Parecer";
        Parecer parecer = criarParecerTeste(idParecer);

        parecerRepository.persisteParecer(parecer);

        Nota notaDoParecer = parecer.getNotas().get(0);
        Nota notaComMesmoOriginal = new Nota(notaDoParecer.getItemOriginal(), criaRelato("Teste de novo Relato"), "Um nova justificativa");

        parecerRepository.adicionaNota(idParecer, notaComMesmoOriginal);

        Document parecerDocument = dbHelper.findById("id", idParecer, MongoParecerRepository.parecerCollection);
        Parecer parecerAlterado = gson.fromJson(gson.toJson(parecerDocument), Parecer.class);

        assertEquals(2, parecerAlterado.getNotas().size());
        assertEquals("Um nova justificativa", parecerAlterado.getNotas().get(1).getJustificativa());
    }

    @Test(expected = IdentificadorDesconhecido.class)
    public void adicionaNotaDeUmParecerNaoExistenteLancaExcecaoTest() {

        String idParecer = "identificador de Parecer";

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

    @Test(expected = IdentificadorDesconhecido.class)
    public void atualizaFundamentacaoDeParecerNaoExistenteLancaExcecaoTest() {

        String idParecer = "idParecerNaoExistente";

        String fundamentacaoParaAlterar = "Fundamentação para alterar";
        parecerRepository.atualizaFundamentacao(idParecer, fundamentacaoParaAlterar);
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

    @Test(expected = IdentificadorExistente.class)
    public void persisteRadocComIdJaExistenteLancaExcecao() {

        String idRadoc = "idRadoc";
        Radoc radoc = criarRadoc(idRadoc, 1995);
        Radoc segundoRadoc = criarRadoc(idRadoc, 2015);

        parecerRepository.persisteRadoc(radoc);
        parecerRepository.persisteRadoc(segundoRadoc);
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

    @Test(expected = ExisteParecerReferenciandoRadoc.class)
    public void removeRadocDeParecerReferenciado() {

        String idParecer = "parecerComReferenciaParaRadoc";
        Parecer parecer = criarParecerTeste(idParecer);
        parecerRepository.persisteParecer(parecer);

        /* Persistindo um radoc com o identificador que é referenciado por um parecer */
        String idRadoc = parecer.getRadocs().get(0);
        persisteRadocParaTeste(idRadoc, 1800);

        parecerRepository.removeRadoc(idRadoc);
    }

    @Test
    public void removeNotaComSucessoTest() {
        String idParecer = "idParecer";
        persisteParecerParaTeste(idParecer);

        Relato relatoOriginalPadrao = criaRelato("II - 3.1");

        parecerRepository.removeNota(idParecer, relatoOriginalPadrao);
        Parecer parecerEncontrado = parecerRepository.byId(idParecer);

        assertEquals(1, parecerEncontrado.getNotas().size());
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
