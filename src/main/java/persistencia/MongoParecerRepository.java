package persistencia;

import com.google.gson.Gson;
import model.Avaliavel;
import model.Nota;
import model.Parecer;
import model.Radoc;
import org.bson.Document;
import persistencia.repository.ParecerRepository;

import java.util.List;

public class MongoParecerRepository implements ParecerRepository {

    private static final String parecerCollection = "parecer";
    private DatabaseHelper dbHelper;
    private Gson gson;

    public MongoParecerRepository(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
        this.gson = new Gson();
    }

    @Override
    public void adicionaNota(String parecer, Nota nota) {
        //Buscando Objeto Parecer com auxilio da classe DatabaseHelper
        Document parecerDocument = dbHelper.findById("guid", parecer, parecerCollection);

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

            dbHelper.updateCollectionObject("guid", parecer, novoParecerJson, parecerCollection);

        }
        // else {
        // TODO: Throw not found exception
        // }
    }

    @Override
    public void removeNota(Avaliavel original) {

    }

    @Override
    public void persisteParecer(Parecer parecer) {

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
