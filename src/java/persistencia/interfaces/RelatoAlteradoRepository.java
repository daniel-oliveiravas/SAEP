package persistencia.interfaces;

import model.RelatoAlterado;

/*
* Repositório responsável pela abstração da persistência
* do objeto Atributo
* */
public interface RelatoAlteradoRepository {

    /*
    * Salva um RelatoAlterado
    *
    * @param RelatoAlterado relatoAlterado - O relatoAlterado a ser persistido
    * */
    void salvarRelatoAlterado(RelatoAlterado relatoAlterado);

    /*
    * Remove um RelatoAlterado
    *
    * @param String identificador - O identificador do relatoAlterado a ser removido
    * */
    void removerRelatoAlterado(String identificador);

    /*
    * Busca um relatoAlterado pelo identificador
    *
    * @param String identificador - O identificador do relatoAlterado a ser buscado
    * */
    RelatoAlterado findByIdentificador(String identificador);

}
