import br.ufg.inf.es.saep.sandbox.dominio.*;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import java.util.*;

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

    Radoc criarRadoc(String idRadoc, int anoBase) {

        return new Radoc(
                idRadoc,
                anoBase,
                criaListaRelatos()
        );
    }

    List<Relato> criaListaRelatos() {

        List<Relato> listaRelatos = new ArrayList<>();
        listaRelatos.add(criaRelato("idRelato1"));
        listaRelatos.add(criaRelato("idRelato2"));
        listaRelatos.add(criaRelato("idRelato3"));

        return listaRelatos;
    }

    Resolucao criaResolucao(String identificadorResolucao) {

        return new Resolucao(
                identificadorResolucao,
                "Nome da resolução",
                "Descrição da resolução",
                new Date(),
                criaListaDeRegras()
        );
    }

    Resolucao criaResolucao(String identificadorResolucao, List<Regra> listaRegras) {

        return new Resolucao(
                identificadorResolucao,
                "Nome da resolução",
                "Descrição da resolução",
                new Date(),
                listaRegras
        );
    }

    List<Regra> criaListaDeRegras() {
        List<String> dependencias = new ArrayList<>();
        dependencias.add("a");
        dependencias.add("b");

        Regra regraTeste = new Regra(4, "Descrição da Regra", 10, 5, "a", "a = b + c", "", "", "idTipoRelato", 1, dependencias);
        List<Regra> listaRegras = new ArrayList<>();
        listaRegras.add(regraTeste);
        return listaRegras;
    }

    Tipo criaTipo(String idTipo) {
        return new Tipo(
                idTipo,
                "Nome do Tipo",
                "Descrição do tipo",
                criaAtributos()
        );
    }

    Set<Atributo> criaAtributos() {
        Set<Atributo> atributos = new LinkedHashSet<>();

        Atributo atributo = new Atributo(
                "Atrbiuto 1",
                "Descrição do primeiro atributo",
                2
        );

        Atributo outroAtributo = new Atributo(
                "Atrbiuto 2",
                "Descrição do segundo atributo",
                1
        );

        atributos.add(atributo);
        atributos.add(outroAtributo);
        return atributos;
    }
}
