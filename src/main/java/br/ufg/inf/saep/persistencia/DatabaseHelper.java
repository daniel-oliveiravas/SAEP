package br.ufg.inf.saep.persistencia;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;

public class DatabaseHelper {

    private MongoDatabase mongoDB;

    public DatabaseHelper(MongoDatabase mongoDB) {
        this.mongoDB = mongoDB;
    }

    /*
    * Salvar um objeto em uma collection.
    *
    * @param String jsonObject - O objeto serializado para JSON no formato de uma String
    * @param String collectionName - o nome da coleção em que este objeto será salvo
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
    * @param String jsonObject - O objeto serializado para JSON no formato de uma String
    * @param String collectionName - o nome da coleção em que este objeto será salvo
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
    public Document findById(String nomeIdentificador, String valorIdentificador, String collectionNome) {
        MongoCollection<Document> collection = getCollection(collectionNome);
        return collection.find(eq(nomeIdentificador, valorIdentificador)).first();
    }

    /*
    * Busca um objeto de uma coleção pelo ObjectId do MongoDB
    *
    * @param nomeIdentificador - É o nome identificador utilizado pelo objeto que
    *  foi salvo no Banco (nome do atributo identificador Ex: "CPF")
    * @param valorIdentificador - É o valor do identificador que será buscado no banco (Ex: "111.222.333-44")
    * */
    public Document findById(ObjectId objectId, String collectionNome) {
        MongoCollection<Document> collection = getCollection(collectionNome);
        return collection.find(eq("_id", objectId)).first();
    }

    /*
    * Atualiza um objeto de uma coleção pelo JSON no formato de uma String
    *
    * @param idName - Nome do identificador utilizado para diferenciar este objeto dos demais
    * @param idValue - Valor do identificador a ser procurado na coleção
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
    * */
    public void removeObjectFromCollection(String idName, String idValue, String collectionName) {

        MongoCollection<Document> collection = getCollection(collectionName);
        collection.deleteOne(eq(idName, idValue));

    }


    public Document findObjectFromCollectionWithFilter(String collectionName, Document query) {

        MongoCollection<Document> collection = getCollection(collectionName);
        return collection.find(query).first();
    }

    private Document parseJsonToDocument(String jsonObject) {
        return Document.parse(jsonObject);
    }

    private MongoCollection<Document> getCollection(String collectionName) {
        return mongoDB.getCollection(collectionName);
    }

}