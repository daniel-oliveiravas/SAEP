package persistencia;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

public class DatabaseHelper {

    private MongoDatabase mongoDB;

    public DatabaseHelper(MongoDatabase mongoDB) {
        this.mongoDB = mongoDB;
    }

    /*
    * Facilitador para salvar um objeto no mongo.
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
    * Busca um objeto de uma coleção pelo identificador
    *
    * @param nomeIdentificador - É o nome identificador utilizado pelo objeto que
    *  foi salvo no Banco (nome do atributo identificador Ex: "CPF")
    * @param valorIdentificador - É o valor do identificador que será buscado no banco (Ex: "111.222.333-44")
    * */
    public Document findById(String nomeIdentificador, String valorIdentificador, String collectionNome) {
        MongoCollection<Document> collection = getCollection(collectionNome);
        return collection.find(eq(nomeIdentificador, valorIdentificador)).first();
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


    private Document parseJsonToDocument(String jsonObject) {
        return Document.parse(jsonObject);
    }

    private MongoCollection<Document> getCollection(String collectionName) {
        return mongoDB.getCollection(collectionName);
    }

}
