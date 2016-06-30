package persistencia;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import javax.print.Doc;

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
    * @return Object identificador do objeto no banco de dados
    * */
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


    private Document parseJsonToDocument(String jsonObject) {
        return Document.parse(jsonObject);
    }

    private MongoCollection<Document> getCollection(String collectionName) {
        return mongoDB.getCollection(collectionName);
    }

}
