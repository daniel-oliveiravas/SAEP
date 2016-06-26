package persistencia;

import model.Relatorio;

/*
* Repositório responsável pela abstração da persistência
* do objeto Relatório.
* Um relatório é um conjunto de relatos, porém este serviço não trata
* da operação de edição dos relatos de um Relatório.
* */
public interface RelatorioRepository {

    /*
    * Abstração da implementação para salvar Relatório (RADOC)
    *
    * @param Relatorio relatorio - O objeto Relatorio a ser persistido
    * */

    void salvarRelatorio(Relatorio relatorio);

    /*
    * Remoção de um relatório
    * @param String identificador - identificador do relatório
    *
    * */

    void removerRelatorio(String identificador);

    /*
    * Busca o relatório pelo identificador único
    * @param String identificador - identificador do relatório
    *
    * */
    Relatorio findById(String identificador);

    /*
    * Edita o anoBase de um Relatório
    * @param String identificador - Identificador único do relatório
    * @param int novoAnoBase - Editar o ano Base do relatório
    * */
    void editarAnoBase(String identificador, int novoAnoBase);


}
