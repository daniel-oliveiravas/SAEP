package br.ufg.inf.saep.persistencia;

import br.ufg.inf.es.saep.sandbox.dominio.Nota;
import br.ufg.inf.es.saep.sandbox.dominio.Resolucao;
import br.ufg.inf.es.saep.sandbox.dominio.ResolucaoRepository;
import br.ufg.inf.es.saep.sandbox.dominio.Tipo;
import br.ufg.inf.saep.persistencia.custom.NotaDeserialize;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bson.Document;

import java.util.List;

public class MongoResolucaoRepository implements ResolucaoRepository {

    public static String resolucaoCollection = "resolucao";
    private DatabaseHelper dbHelper;
    private Gson gson;

    public MongoResolucaoRepository(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Nota.class, new NotaDeserialize());
        gson = gsonBuilder.create();
    }

    @Override
    public Resolucao byId(String id) {

        Document resolucaoEncontrada = dbHelper.findById("id", id, resolucaoCollection);
        if (resolucaoEncontrada == null) {
            return null;
        }

        String resolucaoJson = gson.toJson(resolucaoEncontrada);
        return gson.fromJson(resolucaoJson, Resolucao.class);
    }

    @Override
    public String persiste(Resolucao resolucao) {

        String resolucaoJson = gson.toJson(resolucao);

        Document resolucaoSalvaDocument = dbHelper.saveIntoCollectionReturningDocument(resolucaoJson, resolucaoCollection);

        if(resolucaoSalvaDocument == null){
            return null;
        }

        String resolucaoSalvaJson = gson.toJson(resolucaoSalvaDocument);
        Resolucao resolucaoSalva = gson.fromJson(resolucaoSalvaJson, Resolucao.class);
        return resolucaoSalva.getId();
    }

    @Override
    public boolean remove(String identificador) {
        return dbHelper.removeObjectFromCollection("id", identificador, resolucaoCollection);
    }

    @Override
    public List<String> resolucoes() {
        return null;
    }

    @Override
    public void persisteTipo(Tipo tipo) {

    }

    @Override
    public void removeTipo(String codigo) {

    }

    @Override
    public Tipo tipoPeloCodigo(String codigo) {
        return null;
    }

    @Override
    public List<Tipo> tiposPeloNome(String nome) {
        return null;
    }
}
