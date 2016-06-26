package persistencia;

import model.Relato;
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
    * @params
    *   Relatorio relatorio - O objeto Relatorio a ser persistido
    * */

    void salvarRelatorio(Relatorio relatorio);

    /*
    * Remoção de um relatório
    * @params
    *   String identificador - identificador do relatório
    *
    * */

    void removerRelatorio(String identificador);

    /*
    * Busca o relatório pelo identificador único
    * @params:
    *   String identificador - identificador do relatório
    *
    * */
    Relatorio findById(String identificador);

    /*
    * Edita o anoBase de um Relatório
    * @params
    *   String identificador - Identificador único do relatório
    *   int novoAnoBase - Editar o ano Base do relatório
    * */
    void editarAnoBase(String identificador, int novoAnoBase);


}
