package br.ufg.inf.saep.persistencia;

import br.ufg.inf.es.saep.sandbox.dominio.*;
import br.ufg.inf.saep.persistencia.custom.NotaDeserialize;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MongoResolucaoRepository implements ResolucaoRepository {

    public static String resolucaoCollection = "resolucao";
    public static String tipoCollection = "tipo";
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

        if (resolucaoSalvaDocument == null) {
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
        List<String> listaIdsResolucoes = new ArrayList<>();
        Iterable<Document> listaResolucoes = dbHelper.findAll(resolucaoCollection);

        String idResolucao;
        for (Document documentResolucao : listaResolucoes) {
            idResolucao = documentResolucao.getString("id");
            listaIdsResolucoes.add(idResolucao);
        }

        return listaIdsResolucoes;
    }

    @Override
    public void persisteTipo(Tipo tipo) {
        String tipoJson = gson.toJson(tipo);
        dbHelper.saveIntoCollection(tipoJson, tipoCollection);
    }

    @Override
    public void removeTipo(String codigo) {
        Document query = new Document("regras.tipoRelato", codigo);

        if (dbHelper.findObjectFromCollectionWithFilter(resolucaoCollection, query) != null) {
            throw new ResolucaoUsaTipoException(mensagemResolucaoUsaTipoException());
        } else {
            dbHelper.removeObjectFromCollection("id", codigo, tipoCollection);
        }
    }

    @Override
    public Tipo tipoPeloCodigo(String codigo) {
        Document tipoDocument = dbHelper.findById("id", codigo, tipoCollection);
        String tipoJSON = gson.toJson(tipoDocument);
        return gson.fromJson(tipoJSON, Tipo.class);
    }

    @Override
    public List<Tipo> tiposPeloNome(String nome) {
        ArrayList<Tipo> listaTipo = new ArrayList<>();

        for (Document tipoDocument : dbHelper.findAllLike("nome", nome, tipoCollection)) {
            String tipoJson = tipoDocument.toJson();
            listaTipo.add(gson.fromJson(tipoJson, Tipo.class));
        }
        return listaTipo;
    }

    private String mensagemResolucaoUsaTipoException(){
        return "Existe uma resolução referenciando esse tipo";
    }
}
