package persistencia.interfaces;

import model.Resolucao;

/*
* Repositório responsável pela abstração da persistência
* do objeto Resolucao
* */
public interface ResolucaoRepository {

    /*
    * Salva o objeto Resolucao
    *
    * @param Resolucao resolucao - A Resolucao a ser persistida
    * */
    void salvarResolucao(Resolucao resolucao);

    /*
    * Remove uma resolucao
    *
    * @param String identificadorResolucao - O identificador da Resolucao a ser removida
    * */
    void removerResolucao(String identificadorResolucao);

    /*
    * Altera uma Resolucao
    *
    * @param String identificadorResolucao - O identificador da Resolucao a ser alterada
    * @param Atributo resolucaoModificada - A resolucao com os novos dados a serem
    * persistidos
    * */
    void alterarResolucao(String identificadorResolucao, Resolucao resolucaoModificada);

    /*
    * Busca uma Resolucao pelo identificadorResolucao
    *
    * @param String identificadorResolucao - O identificadorResolucao da Resolucao a ser buscada
    * */
    Resolucao findByIdentificador(String identificadorResolucao);
}
