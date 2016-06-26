package persistencia;

import model.Parecer;

/*
* Repositório responsável pela abstração da persistência
* do objeto Parecer
* */
public interface ParecerRepository {

    /*
    * Implementação da busca do Parecer pelo identificador
    *
    * @params:
    *   String identificador do Parecer
    * */
    Parecer findById(String identificador);


    /*
    * Implementação do método responsável por persistir
    * o objeto Parecer
    *
    * @params:
    *   Parecer parecer a ser persistido
    * */

    void salvarParecer(Parecer parecer);

    /*
    * Implementação da remoção do Parecer
    *
    * @params:
    *   String identificador do Parecer a ser removido
    * */

    void removeParecer(String identificador);

    /*
    * Implementação da edição da descrição de um Parecer
    *
    * @params:
    *   String identificador do Radoc a ser editado
    *   String novaDescricao que será editada no Parecer
    *
    * */

    void editarParecer(String identificador, String novaDescricao);

}
