import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.*;
import org.bson.Document;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import persistencia.DatabaseHelper;
import persistencia.MongoParecerRepository;
import persistencia.custom.NotaDeserialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MongoParecerRepositoryTest extends SaepTestSpecification {

    private static MongoParecerRepository parecerRepository;
    private static DatabaseHelper dbHelper;
    private static Gson gson;

    @BeforeClass
    public static void setup() {
        dbHelper = new DatabaseHelper(createDatabaseConnection());
        parecerRepository = new MongoParecerRepository(dbHelper);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Nota.class, new NotaDeserialize());
        gson = gsonBuilder.create();
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

    private Parecer criarParecerTeste(String identificador) {
        return new Parecer(
                identificador,
                "12345",
                criarListaString(3),
                criarListaPontuacao(),
                "Este professor possui nota suficiente.",
                criarListaNota()
        );
    }

    private List<String> criarListaString(int listSize) {
        List<String> radocsIds = new ArrayList<>();

        for (int i = 0; i < listSize; i++) {
            radocsIds.add(String.valueOf(i));
        }

        return radocsIds;
    }

    private List<Pontuacao> criarListaPontuacao() {

        Valor valor = criaValorString("Livro - Ensinando MongoDB");
        Valor outroValor = criaValorString("Artigo - Aplicações reais do MongoDB");

        Pontuacao pontuacao = new Pontuacao("Pontuacao do Livro", valor);
        Pontuacao outraPontuacao = new Pontuacao("Pontuacao do Artigo", outroValor);

        List<Pontuacao> listaPontuacoes = new ArrayList<>();
        listaPontuacoes.add(pontuacao);
        listaPontuacoes.add(outraPontuacao);

        return listaPontuacoes;
    }

    private List<Nota> criarListaNota() {

        Relato relatoOriginal = criaRelato("II - 3.1");
        Relato relatoNovo = criaRelato("IV - 8");

        Relato outroRelatoOriginal = criaRelato("III - 1.1");
        Relato outroRelatoNovo = criaRelato("I - 9");

        Nota umaNota = criaNota(relatoOriginal, relatoNovo, "Justificativa para alteração");
        Nota outraNota = criaNota(outroRelatoOriginal, outroRelatoNovo, "Outro justificativa para alteração");

        List<Nota> listaNotas = new ArrayList<>();
        listaNotas.add(umaNota);
        listaNotas.add(outraNota);

        return listaNotas;
    }

    private Nota criaNota(Relato relatoOriginal, Relato relatoNovo, String justificativa) {
        return new Nota(relatoOriginal, relatoNovo, justificativa);
    }

    private Relato criaRelato(String idTipoRelato) {

        Map<String, Valor> stringValorMap = new HashMap<>();

        stringValorMap.put("atributo1", criaValorString("Um tipo de valor"));
        stringValorMap.put("atributo2", criaValorString("Outro tipo de valor"));

        return new Relato(idTipoRelato, stringValorMap);

    }

    private Valor criaValorString(String valor) {
        return new Valor(valor);
    }

}
