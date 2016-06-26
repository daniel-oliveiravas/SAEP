package persistencia.interfaces;

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
    void removerAtributo(String identificador);

    /*
    * Altera um atributo
    *
    * @param String identificador - O identificador do atributo a ser alterado
    * @param Atributo atributoModificado - O atributo com os novos dados a serem
    * persistidos
    * */
    void alterarAtributo(String identificador, Atributo atributoModificado);

    /*
    * Busca um atributo pelo identificador
    *
    * @param String identificador - O identificador do atributo a ser buscado
    * */
    Atributo findByIdentificador(String identificador);

}
