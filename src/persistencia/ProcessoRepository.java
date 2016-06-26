package persistencia;

import model.Processo;

public interface ProcessoRepository {

    /*
    * Busca o objeto Processo pela matrícula do docente vinculada
    *
    * @params
    *   String matriculaDocente
    * */
    Processo findByMatriculaDocente(String matriculaDocente);

    /*
    * Busca o objeto Processo pelo identificador
    *
    * @params
    *   String identificador
    * */
    Processo findById(String identificador);

    /*
    * Remove o objeto Processo pelo identificador
    *
    * @params
    *   String identificador
    * */
    Processo removerProcesso(String identificador);

    /*
    * Alterar Processo passando o identificador
    * e o objeto com os dados alterados
    * @params
    *   String identificador
    *   Processo processo
    * */
    Processo removerProcesso(String identificador, Processo processo);

    /*
    * Salva o objeto Processo
    *
    * @params
    *   Processo processo
    * */
    Processo salvarProcesso(Processo processo);

}
