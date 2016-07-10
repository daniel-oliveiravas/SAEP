import br.ufg.inf.es.saep.sandbox.dominio.Resolucao;
import br.ufg.inf.es.saep.sandbox.dominio.Tipo;
import br.ufg.inf.saep.persistencia.DatabaseHelper;
import br.ufg.inf.saep.persistencia.MongoResolucaoRepository;
import com.mongodb.client.MongoDatabase;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class MongoResolucaoRepositoryTest extends SaepTestSpecification {

    private static MongoResolucaoRepository resolucaoRepository;
    private static DatabaseHelper dbHelper;
    private static MongoDatabase mongoDB;

    @BeforeClass
    public static void setup() {
        mongoDB = createDatabaseConnection();
        dbHelper = new DatabaseHelper(mongoDB);
        resolucaoRepository = new MongoResolucaoRepository(dbHelper);
    }

    @Before
    public void createResolucaoCollection() {
        createResolucaoRepositoryCollectionsForTest();
    }

    @After
    public void destroyResolucaoCollection() {
        destroyResolucaoRepositoryTestCollections();
    }

    @Test
    public void persisteResolucaoTest() {
        String idResolucao = "idResolucao";
        String idSalvo = resolucaoRepository.persiste(criaResolucao(idResolucao));

        assertEquals(idSalvo, idResolucao);
    }

    @Test
    public void findByTest() {
        String idResolucao = "idResolucao";
        resolucaoRepository.persiste(criaResolucao(idResolucao));

        String idEncontrado = resolucaoRepository.byId(idResolucao).getId();

        assertEquals(idEncontrado, idResolucao);
    }

    @Test
    public void findByComIdNaoExistenteRestornaNullTest() {
        String idResolucao = "idResolucao";
        resolucaoRepository.persiste(criaResolucao(idResolucao));

        String idNaoExistente = "naoExistente";
        Resolucao resolucao = resolucaoRepository.byId(idNaoExistente);

        assertNull(resolucao);
    }

    @Test
    public void removeResolucaoComSucessoTest() {
        String idResolucao = "idResolucao";
        resolucaoRepository.persiste(criaResolucao(idResolucao));

        boolean resultado = resolucaoRepository.remove(idResolucao);

        assertTrue(resultado);
    }

    @Test
    public void falhaAoRemoverResolucaoTest() {
        String idResolucao = "idResolucao";
        resolucaoRepository.persiste(criaResolucao(idResolucao));

        String idNaoExistente = "naoExistente";
        boolean resultado = resolucaoRepository.remove(idNaoExistente);

        assertFalse(resultado);
    }

    @Test
    public void obtemListaVaziaIdsResolucoesTest() {

        List<String> idsResolucoes = resolucaoRepository.resolucoes();

        assertEquals(0, idsResolucoes.size());
    }

    @Test
    public void obtemListaDeIdsResolucoesComSucessoTest() {
        String idResolucao = "idResolucao";
        String outraResolucao = "outraResolucao";
        resolucaoRepository.persiste(criaResolucao(idResolucao));
        resolucaoRepository.persiste(criaResolucao(outraResolucao));

        List<String> idsResolucoes = resolucaoRepository.resolucoes();

        Collections.sort(idsResolucoes);
        assertEquals(2, idsResolucoes.size());
        assertEquals(idResolucao, idsResolucoes.get(0));
        assertEquals(outraResolucao, idsResolucoes.get(1));
    }

    @Test
    public void persisteTipoTest() {
        String idTipo = "idTipo";
        Tipo tipo = criaTipo(idTipo);

        resolucaoRepository.persisteTipo(tipo);
        Tipo tipoEncontrado = resolucaoRepository.tipoPeloCodigo(idTipo);

        assertEquals(tipoEncontrado.getId(), idTipo);
    }

    @Test
    public void tipoPeloCodigoTest() {
        String idTipo = "idTipo";
        Tipo tipo = criaTipo(idTipo);

        resolucaoRepository.persisteTipo(tipo);
        Tipo tipoEncontrado = resolucaoRepository.tipoPeloCodigo(idTipo);

        assertNotNull(tipoEncontrado);
        assertEquals(tipo.getNome(),tipoEncontrado.getNome());
    }

    @Test
    public void tipoPeloCodigoInexistenteRetornaNullTest() {
        String idTipo = "idTipoInexistente";
        Tipo tipoEncontrado = resolucaoRepository.tipoPeloCodigo(idTipo);

        assertNull(tipoEncontrado);
    }

    private static void createResolucaoRepositoryCollectionsForTest() {
        mongoDB.createCollection(MongoResolucaoRepository.resolucaoCollection);
        mongoDB.createCollection(MongoResolucaoRepository.tipoCollection);
    }

    private static void destroyResolucaoRepositoryTestCollections() {
        mongoDB.getCollection(MongoResolucaoRepository.resolucaoCollection).drop();
        mongoDB.getCollection(MongoResolucaoRepository.tipoCollection).drop();
    }

}