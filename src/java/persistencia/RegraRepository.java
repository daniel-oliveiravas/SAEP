package persistencia;

import model.Regra;

/*
* Repositório responsável pela abstração da persistência
* do objeto Regra
* */
public interface RegraRepository {

    /*
    * Salva uma Regra
    *
    * @param Regra regra - A regra a ser persistido
    * */
    void salvarRegra(Regra regra);

    /*
    * Remove uma Regra
    *
    * @param String identificador - O identificador da regra a ser removida
    * */
    void removerRegra(String identificador);

    /*
    * Altera uma Regra
    *
    * @param String identificador - O identificador da Regra a ser alterada
    * @param Regra regraModificada - A regra com os novos dados a serem
    * persistidos
    * */
    void alterarRegra(String identificador, Regra regraModificada);

    /*
    * Busca uma regra pelo identificador
    *
    * @param String identificador - O identificador da Regra a ser buscada
    * */
    Regra findByIdentificador(String identificador);

}
