package br.ufg.inf.saep.persistencia;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.eq;

public class DatabaseHelper {

    private MongoDatabase mongoDB;

    public DatabaseHelper(MongoDatabase mongoDB) {
        this.mongoDB = mongoDB;
    }

    /*
    * Salvar um objeto em uma collection.
    *
    * @param String jsonObject - O objeto serializado para JSON no
    * formato de uma String
    * @param String collectionName - o nome da coleção em que
    * este objeto será salvo
    *
    */
    public void saveIntoCollection(String jsonObject, String collectionName) {
        MongoCollection<Document> collection = getCollection(collectionName);
        Document documentToSave = parseJsonToDocument(jsonObject);
        collection.insertOne(documentToSave);
    }

    /*
    * Salva um objeto em uma collection, retornando o objeto salvo
    *
    * @param String jsonObject - O objeto serializado para JSON no
    * formato de uma String
    * @param String collectionName - o nome da coleção em que este
    * objeto será salvo
    * */
    public Document saveIntoCollectionReturningDocument(String jsonObject, String collectionName) {
        MongoCollection<Document> collection = getCollection(collectionName);
        Document documentToSave = parseJsonToDocument(jsonObject);
        collection.insertOne(documentToSave);

        return this.findById((ObjectId) documentToSave.get("_id"), collectionName);
    }


    /*
    * Busca um objeto de uma coleção pelo identificador
    *
    * @param nomeIdentificador - É o nome identificador utilizado pelo objeto que
    *  foi salvo no Banco (nome do atributo identificador Ex: "CPF")
    * @param valorIdentificador - É o valor do identificador que será buscado no banco (Ex: "111.222.333-44")
    * @param collectionName - É o nome da coleção onde o objeto será buscado
    * */
    public Document findById(String idName, String idValue, String collectionNome) {
        MongoCollection<Document> collection = getCollection(collectionNome);
        return collection.find(eq(idName, idValue)).first();
    }

    /*
    * Busca um objeto de uma coleção pelo ObjectId do MongoDB.
    * Este método é usado somente internamente, de forma a abstrair os elementos específicos
    * do MongoDB.
    * @param ObjectId objectId - É o object ID usado como identificador no mongo
    * @param collectionNome - Nome da coleção onde o objeto será buscado
    * */
    private Document findById(ObjectId objectId, String collectionNome) {
        MongoCollection<Document> collection = getCollection(collectionNome);
        return collection.find(eq("_id", objectId)).first();
    }

    /*
    * Busca um objeto de uma coleção por um document passado como query
    *
    * @param String collectionName - É o nome identificador utilizado pelo objeto que
    *  foi salvo no Banco (nome do atributo identificador Ex: "CPF")
    * @param Document query - É o documento que será usando como comparador para buscar o objeto.
    * */
    public Document findObjectFromCollectionWithFilter(String collectionName, Document query) {
        MongoCollection<Document> collection = getCollection(collectionName);
        return collection.find(query).first();
    }

    /*
    * Busca todos os documentos de uma coleção.
    *
    * @param String collectionName - Nome da coleção onde os documentos serão buscados
    * */

    public Iterable<Document> findAll(String collectionName) {
        MongoCollection<Document> collection = getCollection(collectionName);
        return collection.find();
    }

    /*
    * Busca elemento que possui valor similar ao passado como parâmetro
    * (Entenda por similar um texto que contém o outro por exemplo)
    * */
    public Iterable<Document> findAllLike(String idName, String idValue, String collectionName) {
        MongoCollection<Document> collection = getCollection(collectionName);
        Document query = new Document(idName, Pattern.compile(idValue));
        return collection.find(query);
    }


    /*
    * Atualiza um objeto de uma coleção pelo JSON no formato de uma String
    *
    * @param String idName - Nome do identificador utilizado para diferenciar este objeto dos demais
    * @param String idValue - Valor do identificador a ser procurado na coleção
    * @param String jsonObject - Objeto a ser atualizado no formato de uma String
    * @param String collectionName - O nome da coleção em que o objeto será atualizado
    *
    * */
    public void updateCollectionObject(String idName, String idValue, String jsonObject, String collectionName) {
        MongoCollection<Document> collection = getCollection(collectionName);
        Document documentToUpdate = parseJsonToDocument(jsonObject);
        collection.replaceOne(eq(idName, idValue), documentToUpdate);
    }

    /*
    * Remove um objecto de uma coleção pelo identificador do objeto
    * retornando o resultado da remoção (true se realizado com sucesso,
    * false caso contrário)
    *
    * @param String idName - Nome do atributo que será buscado na coleção
    * @param String idValue - Valor do atributo que será buscado na coleção
    * @param String collectionName - Nome da coleção onde o atributo será buscado
    *
    * */
    public boolean removeObjectFromCollection(String idName, String idValue, String collectionName) {
        MongoCollection<Document> collection = getCollection(collectionName);
        DeleteResult result = collection.deleteOne(eq(idName, idValue));
        return result.getDeletedCount() > 0;
    }

    private Document parseJsonToDocument(String jsonObject) {
        return Document.parse(jsonObject);
    }

    private MongoCollection<Document> getCollection(String collectionName) {
        return mongoDB.getCollection(collectionName);
    }

}
