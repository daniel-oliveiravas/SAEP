package persistencia;

import model.Atributo;
import model.Parecer;
import model.Relato;

/*
* Repositório responsável pela abstração da persistência
* do objeto Atributo
* */
public interface AtributoRepository {

    /*
    * Salva um atributo
    *
    * @params
    *   Atributo atrituo - O atributo a ser persistido
    * */
    void salvarAtributo(Atributo atributo);

    /*
    * Remove um atributo
    *
    * @params
    *   String identificador - O identificador do atributo a ser removido
    * */
    void removerAtributo(Atributo atributo);

    /*
    * Altera um atributo
    *
    * @params
    *   String identificador - O identificador do atributo a ser alterado
    *   Atributo atributoModificado - O atributo com os novos dados a serem
    *   persistidos
    * */
    void alterarAtributo(String identificador, Atributo atributoModificado);

    /*
    * Busca um atributo pelo identificador
    *
    * @params
    *   String identificador - O identificador do atributo a ser buscado
    * */
    Relato findByIdentificador(String identificador);

    /*
    * Busca um atributo pelo nome e pelo tipo
    *
    * @params
    *   String nome - O atributo nome do objeto Relato a ser buscado
    *   String tipo - O atributo tipo do objeto Relato a ser buscado
    * @returns
    *   Relato - O objeto relato que possui nome e tipo iguais aos passados
    *   por parêmetro
    * */
    Relato findByNomeAndTipo(String nome, String tipo);

}
