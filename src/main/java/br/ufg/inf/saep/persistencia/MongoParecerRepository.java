package br.ufg.inf.saep.persistencia;

import br.ufg.inf.es.saep.sandbox.dominio.*;
import br.ufg.inf.saep.persistencia.custom.NotaDeserialize;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bson.Document;

public class MongoParecerRepository implements ParecerRepository {

    public static final String parecerCollection = "parecer";
    public static final String radocCollection = "radoc";
    private static final String nomeEntidadeRadoc = "Radoc";
    private static final String nomeEntidadeParecer = "Parecer";
    private DatabaseHelper dbHelper;
    private static Gson gson;

    public MongoParecerRepository() {
        this.dbHelper = DatabaseHelper.getInstancia();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Nota.class, new NotaDeserialize());
        gson = gsonBuilder.create();
    }

    @Override
    public void adicionaNota(String parecer, Nota nota) {
        //Buscando Objeto Parecer com auxilio da classe DatabaseHelper
        Document parecerDocument = dbHelper.findById("id", parecer, parecerCollection);

        if (parecerDocument != null) {
            removeNota(parecer, nota.getItemOriginal());

            String notaJson = gson.toJson(nota);
            Document notaParaAdicionar = new Document("notas", Document.parse(notaJson));
            dbHelper.updateObjectWithFilter("id", parecer, parecerCollection, new Document("$push", notaParaAdicionar));
        } else {
            throw new IdentificadorDesconhecido(mensagemEntidadeNaoEncontrada(nomeEntidadeRadoc, parecer));
        }
    }

    @Override
    public void removeNota(String id, Avaliavel original) {
        String avaliavelJSON = gson.toJson(original);
        Document documentOriginalParaRemover = new Document("notas", new Document("original", Document.parse(avaliavelJSON)));
        Document removeQuery = new Document("$pull", documentOriginalParaRemover);
        dbHelper.updateObjectWithFilter("id", id, parecerCollection, removeQuery);
    }

    @Override
    public void persisteParecer(Parecer parecer) {

        /* Verifica se já existe uma parecer com este identificador no banco de dados
        *  caso exista, lance a execeção de identificador já existente
        * */
        String idParecer = parecer.getId();
        Document document = dbHelper.findById("id", idParecer, parecerCollection);
        if (document != null) {
            throw new IdentificadorExistente(mensagemEntidadeJaExistente(nomeEntidadeRadoc, idParecer));
        }

        String parecerJson = gson.toJson(parecer);
        dbHelper.saveIntoCollection(parecerJson, parecerCollection);
    }

    @Override
    public void atualizaFundamentacao(String parecer, String fundamentacao) {

        Document parecerDocument = dbHelper.findById("id", parecer, parecerCollection);

        if (parecerDocument == null) {
            throw new IdentificadorDesconhecido(mensagemEntidadeNaoEncontrada(nomeEntidadeParecer, parecer));
        } else {
            String parecerJson = gson.toJson(parecerDocument);

            Parecer parecerEncontrado = gson.fromJson(parecerJson, Parecer.class);

            Parecer novoParecer = new Parecer(
                    parecerEncontrado.getId(),
                    parecerEncontrado.getResolucao(),
                    parecerEncontrado.getRadocs(),
                    parecerEncontrado.getPontuacoes(),
                    fundamentacao,
                    parecerEncontrado.getNotas()
            );

            String novoParecerJson = gson.toJson(novoParecer);

            dbHelper.updateCollectionObject("id", novoParecer.getId(), novoParecerJson, parecerCollection);
        }
    }


    @Override
    public Parecer byId(String id) {
        Document parecerDocument = dbHelper.findById("id", id, parecerCollection);

        if (parecerDocument != null) {
            String parecerJson = gson.toJson(parecerDocument);
            return gson.fromJson(parecerJson, Parecer.class);
        }

        return null;
    }

    @Override
    public void removeParecer(String id) {
        dbHelper.removeObjectFromCollection("id", id, parecerCollection);
    }

    @Override
    public Radoc radocById(String identificador) {

        Document radocDocument = dbHelper.findById("id", identificador, radocCollection);

        if (radocDocument != null) {
            String radocJson = gson.toJson(radocDocument);
            return gson.fromJson(radocJson, Radoc.class);
        }
        return null;
    }

    @Override
    public String persisteRadoc(Radoc radoc) {

        String idRadoc = radoc.getId();
        Document radocExistenteDocument = dbHelper.findById("id", idRadoc, radocCollection);

        if (radocExistenteDocument != null) {
            throw new IdentificadorExistente(mensagemEntidadeJaExistente("Radoc", idRadoc));
        } else {
            String radocJson = gson.toJson(radoc);
            Document radocDocument = dbHelper.saveIntoCollectionReturningDocument(radocJson, radocCollection);

            //Verifica se a operação foi completada com sucesso
            if (radocDocument != null) {
                String radocSalvo = gson.toJson(radocDocument);
                Radoc radocEncontrado = gson.fromJson(radocSalvo, Radoc.class);
                return radocEncontrado.getId();
            }
        }
        return null;
    }

    @Override
    public void removeRadoc(String identificador) {

        if (!verificaSeAlgumParecerReferenciaRadoc(identificador)) {
            dbHelper.removeObjectFromCollection("id", identificador, radocCollection);
        } else {
            throw new ExisteParecerReferenciandoRadoc("Existe um parecer referenciando o radoc como id: " + identificador);
        }
    }

    private boolean verificaSeAlgumParecerReferenciaRadoc(String identificador) {

        Document query = new Document("radocs", identificador);

        Document parecerEncontrado = dbHelper.findObjectFromCollectionWithFilter(parecerCollection, query);

        if (parecerEncontrado == null) {
            return false;
        } else {
            return true;
        }
    }

    private String mensagemEntidadeNaoEncontrada(String nomeEntidade, String idEntidade) {
        return "Identificador do " + nomeEntidade + " " + idEntidade + " não encontrado.";
    }

    private String mensagemEntidadeJaExistente(String nomeEntidade, String idEntidade) {
        return "Identificador do " + nomeEntidade + " " + idEntidade + " já existe.";
    }

}
