import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class SaepTestSpecification {

    static MongoDatabase createDatabaseConnection() {
        MongoClient mongoClient = new MongoClient();
        return mongoClient.getDatabase("SAEP-test");
    }

    Parecer criarParecerTeste(String identificador) {
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

    Nota criaNota(Relato relatoOriginal, Relato relatoNovo, String justificativa) {
        return new Nota(relatoOriginal, relatoNovo, justificativa);
    }

    Relato criaRelato(String idTipoRelato) {

        Map<String, Valor> stringValorMap = new HashMap<>();

        stringValorMap.put("atributo1", criaValorString("Um tipo de valor"));
        stringValorMap.put("atributo2", criaValorString("Outro tipo de valor"));

        return new Relato(idTipoRelato, stringValorMap);

    }

    private Valor criaValorString(String valor) {
        return new Valor(valor);
    }
}
