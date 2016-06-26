package persistencia.interfaces;

import model.TipoRelato;

/*
* Repositório responsável pela abstração da persistência
* do objeto TipoRelato
* */
public interface TipoRelatoRepository {

    /*
    * Salva um TipoRelato
    *
    * @param TipoRelato tipoRelato - O TipoRelato a ser persistido
    * */
    void salvarTipoRelato(TipoRelato tipoRelato);

    /*
    * Remove um TipoRelato
    *
    * @param String identificadorTabela - O identificadorTabela do TipoRelato a ser removido
    * */
    void removerTipoRelato(String identificadorTabela);

    /*
    * Altera um TipoRelato
    *
    * @param String identificadorTabela - O identificador do TipoRelato a ser alterado
    * @param TipoRelato tipoRelatoModificado - O TipoRelato com os novos dados a serem
    * persistidos
    * */
    void alterarTipoRelato(String identificadorTabela, TipoRelato tipoRelatoModificado);

    /*
    * Busca um atributo pelo identificador
    *
    * @param String identificadorTabela - O identificadorTabela do atributo a ser buscado
    * */
    TipoRelato findByIdentificador(String identificadorTabela);

}
