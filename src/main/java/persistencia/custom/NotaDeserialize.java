package persistencia.custom;

import com.google.gson.*;
import model.Avaliavel;
import model.Nota;
import model.Pontuacao;
import model.Relato;

import java.lang.reflect.Type;

public class NotaDeserialize implements JsonDeserializer<Nota> {


    @Override
    public Nota deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject originalJSON = (JsonObject) ((JsonObject) json).get("original");
        JsonObject novoJSON = (JsonObject) ((JsonObject) json).get("novo");
        String justificativa = ((JsonObject) json).get("justificativa").getAsString();

        Avaliavel original = criarObjetoAvaliavel(originalJSON);
        Avaliavel novo = criarObjetoAvaliavel(novoJSON);

        return new Nota(
                original,
                novo,
                justificativa
        );
    }

    private Avaliavel criarObjetoAvaliavel(JsonObject jsonObject) {
        Gson gson = new Gson();

        if (jsonObject.has("tipo")) {
            return gson.fromJson(jsonObject, Relato.class);
        } else {
            return gson.fromJson(jsonObject, Pontuacao.class);
        }

    }

}