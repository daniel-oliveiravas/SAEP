package persistencia.interfaces;

import model.Processo;

public interface ProcessoRepository {

    /*
    * Busca o objeto Processo pela matrícula do docente vinculada
    *
    * @param String matriculaDocente
    * */
    Processo findByMatriculaDocente(String matriculaDocente);

    /*
    * Busca o objeto Processo pelo identificador
    *
    * @param String identificador
    * */
    Processo findById(String identificador);

    /*
    * Remove o objeto Processo pelo identificador
    *
    * @param String identificador
    * */
    void removerProcesso(String identificador);

    /*
    * Alterar Processo passando o identificador
    * e o objeto com os dados alterados
    *
    * @param String identificador
    * @param Processo processo
    * */
    void alterarProcesso(String identificador, Processo processo);

    /*
    * Salva o objeto Processo
    *
    * @param Processo processo
    * */
    void salvarProcesso(Processo processo);

}
