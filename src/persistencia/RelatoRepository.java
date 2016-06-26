package persistencia;


import model.Relato;

import java.util.List;

/*
* Repositório responsável pela abstração da persistência
* do objeto Relatório.
* Um relatório é um conjunto de relatos, porém este serviço não trata
* da operação de edição dos relatos de um Relatório.
* */
public interface RelatoRepository {

    /*
    * Salvar um relato de um docente
    *
    * @params:
    *   Relato relato - O objeto Relato a ser persistido
    *
    * */
    void salvarRelato(Relato relato);

    /*
    * Remover um relato de um docente
    * @params:
    *   String identificador - Identificador único do relato
    *
    * */
    void removerRelato(String identificador);

    /*
    *
    * Alterar o relato de um docente.
    * É necessário que seja mantido o relato original com todas as suas referências,
    * além de que para realizar uma alteração é necessário informar uma justificativa.
    * Logo, precisamos obter o identificador do relato, o objeto com suas modificações
    * e uma justificativa.
    *
    * @params:
    *  String identificador - Identificador do relato que está sendo alterado
    *  Relato relato - O novo objeto relato com suas alterações. (É necessário que este seja
    *  uma cópia do objeto original, exceto nos valores e atributos onde ocorreram as modificações)
    *  String justificativa - Texto explicando o motivo da alteração do relato
    * @return:
    *   O objeto modificado que foi salvo no banco de dados. Caso não tenha sido possível salvar o objeto,
    *   o retorno será null.
    * */
    Relato alterarRelato(String identificador, Relato relatoModificado, String justificativaAlteracao);

    /*
    * Obter lista de relatos de um relatório
    *
    * @params:
    *   String identificadorRelatorio - É o identificador único do relatório ao qual os relatos pertencem
    * @return:
    *   List<Relatos> - Uma lista de todos os relatos de um relatório
    * */
    List<Relato> findAllBy(String identificadorRelatorio);

    /*
    * Obter relato pelo identificador
    *
    * @params:
    *   String identificador - Identificador único do relato
    *
    * */
    Relato findById(String identificador);
}
