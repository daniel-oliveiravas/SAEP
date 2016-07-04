package persistencia;

import br.ufg.inf.es.saep.sandbox.dominio.Avaliavel;
import br.ufg.inf.es.saep.sandbox.dominio.Nota;
import br.ufg.inf.es.saep.sandbox.dominio.Parecer;
import br.ufg.inf.es.saep.sandbox.dominio.Radoc;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bson.Document;
import persistencia.custom.NotaDeserialize;
import persistencia.repository.ParecerRepository;

import java.util.List;

public class MongoParecerRepository implements ParecerRepository {

    public static final String parecerCollection = "parecer";
    private DatabaseHelper dbHelper;
    private static Gson gson;

    public MongoParecerRepository(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Nota.class, new NotaDeserialize());
        gson = gsonBuilder.create();
    }

    @Override
    public void adicionaNota(String parecer, Nota nota) {
        //Buscando Objeto Parecer com auxilio da classe DatabaseHelper
        Document parecerDocument = dbHelper.findById("id", parecer, parecerCollection);

        if (parecerDocument != null) {
            String parecerJson = gson.toJson(parecerDocument);

            Parecer parecerEncontrado = gson.fromJson(parecerJson, Parecer.class);
            List<Nota> listaNotas = parecerEncontrado.getNotas();

            //adicionando nova Nota na lista de notas do parecer
            listaNotas.add(nota);

            Parecer novoParecer = new Parecer(
                    parecerEncontrado.getId(),
                    parecerEncontrado.getResolucao(),
                    parecerEncontrado.getRadocs(),
                    parecerEncontrado.getPontuacoes(),
                    parecerEncontrado.getFundamentacao(),
                    listaNotas
            );

            String novoParecerJson = gson.toJson(novoParecer);

            dbHelper.updateCollectionObject("id", parecer, novoParecerJson, parecerCollection);

        }
        // else {
        // TODO: Throw not found exception
        // }
    }

    @Override
    public void removeNota(Avaliavel original) {
        //TODO: Deveria passar o identificador do parecer
    }

    @Override
    public void persisteParecer(Parecer parecer) {

        String parecerJson = gson.toJson(parecer);
        dbHelper.saveIntoCollection(parecerJson, parecerCollection);

    }

    @Override
    public void atualizaFundamentacao(String parecer, String fundamentacao) {

    }


    @Override
    public Parecer byId(String id) {
        return null;
    }

    @Override
    public void removeParecer(String id) {

    }

    @Override
    public Radoc radocById(String identificador) {
        return null;
    }

    @Override
    public String persisteRadoc(Radoc radoc) {
        return null;
    }

    @Override
    public void removeRadoc(String identificador) {

    }

}
