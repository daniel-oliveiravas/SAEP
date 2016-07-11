import br.ufg.inf.es.saep.sandbox.dominio.IdentificadorExistente;
import br.ufg.inf.es.saep.sandbox.dominio.Resolucao;
import br.ufg.inf.es.saep.sandbox.dominio.ResolucaoUsaTipoException;
import br.ufg.inf.es.saep.sandbox.dominio.Tipo;
import br.ufg.inf.saep.persistencia.DatabaseHelper;
import br.ufg.inf.saep.persistencia.MongoResolucaoRepository;
import com.mongodb.client.MongoDatabase;
import org.junit.*;

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
        destroyResolucaoRepositoryTestCollections();
        dbHelper = new DatabaseHelper(mongoDB);
        resolucaoRepository = new MongoResolucaoRepository(dbHelper);
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

    @Test(expected = IdentificadorExistente.class)
    public void persisteResolucaoComIdExistenteLancaExcecaoTest() {
        String idResolucao = "idResolucao";
        resolucaoRepository.persiste(criaResolucao(idResolucao));
        resolucaoRepository.persiste(criaResolucao(idResolucao));
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

    @Test(expected = IdentificadorExistente.class)
    public void persisteTipoComIdExistenteLancaExcecaoTest() {
        String idTipo = "idTipo";
        Tipo tipo = criaTipo(idTipo);

        resolucaoRepository.persisteTipo(tipo);
        resolucaoRepository.persisteTipo(tipo);
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
        assertEquals(tipo.getNome(), tipoEncontrado.getNome());
    }

    @Test
    public void tipoPeloCodigoInexistenteRetornaNullTest() {
        String idTipo = "idTipoInexistente";
        Tipo tipoEncontrado = resolucaoRepository.tipoPeloCodigo(idTipo);

        assertNull(tipoEncontrado);
    }

    @Test
    public void tiposPeloNomeInexistenteRetornaListaVaziaTest() {
        String idTipo = "nomeNaoExistente";
        List<Tipo> tipoEncontrado = resolucaoRepository.tiposPeloNome(idTipo);

        assertEquals(0, tipoEncontrado.size());
    }

    @Test
    public void tiposPeloNomeRetornaListaComSucessoTest() {
        String idTipo = "idTipo";
        String outroIdTipo = "outroIdTipo";
        Tipo tipo = criaTipo(idTipo);
        Tipo outroTipo = criaTipo(outroIdTipo);
        String nomeTipo = "do";
        resolucaoRepository.persisteTipo(tipo);
        resolucaoRepository.persisteTipo(outroTipo);

        List<Tipo> tipoEncontradoLista = resolucaoRepository.tiposPeloNome(nomeTipo);

        assertEquals(2, tipoEncontradoLista.size());
    }

    @Test(expected = ResolucaoUsaTipoException.class)
    public void removeTipoReferenciadoPorResolucaoLancaExcecaoTest() {
        String idResolucao = "idResolucao";
        resolucaoRepository.persiste(criaResolucao(idResolucao));

        String idTipo = "idTipoRelato";
        Tipo tipo = criaTipo(idTipo);

        resolucaoRepository.persisteTipo(tipo);
        resolucaoRepository.removeTipo(idTipo);
    }

    @Test
    public void removeTipoSemReferenciaTest() {

        String idTipo = "idTipoRelato";
        Tipo tipo = criaTipo(idTipo);

        resolucaoRepository.persisteTipo(tipo);
        resolucaoRepository.removeTipo(idTipo);

        Tipo tipoEncontrado = resolucaoRepository.tipoPeloCodigo(idTipo);
        assertNull(tipoEncontrado);
    }

    private static void destroyResolucaoRepositoryTestCollections() {
        mongoDB.getCollection(MongoResolucaoRepository.resolucaoCollection).drop();
        mongoDB.getCollection(MongoResolucaoRepository.tipoCollection).drop();
    }

}
