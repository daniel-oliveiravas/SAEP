package persistencia;

import model.Atributo;
import model.Relato;

/*
* Repositório responsável pela abstração da persistência
* do objeto Atributo
* */
public interface AtributoRepository {

    /*
    * Salva um atributo
    *
    * @param Atributo atrituo - O atributo a ser persistido
    * */
    void salvarAtributo(Atributo atributo);

    /*
    * Remove um atributo
    *
    * @param String identificador - O identificador do atributo a ser removido
    * */
    void removerAtributo(Atributo atributo);

    /*
    * Altera um atributo
    *
    * @param String identificador - O identificador do atributo a ser alterado
    * @param Atributo atributoModificado - O atributo com os novos dados a serem
    * @param persistidos
    * */
    void alterarAtributo(String identificador, Atributo atributoModificado);

    /*
    * Busca um atributo pelo identificador
    *
    * @param String identificador - O identificador do atributo a ser buscado
    * */
    Atributo findByIdentificador(String identificador);

    /*
    * Busca um atributo pelo nome e pelo tipo
    *
    * @param String nome - O nome do objeto Atributo a ser buscado
    * @param String tipo - O tipo do objeto Atributo a ser buscado
    * @returns
    *   Relato - O objeto relato que possui nome e tipo iguais aos passados
    *   por parêmetro
    * */
    Relato findByNomeAndTipo(String nome, String tipo);

}
