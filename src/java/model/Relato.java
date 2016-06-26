package model;

import java.util.Date;
import java.util.List;

public class Relato {

    /* Identificador único do relato */
    String identificador;
    /* Identificador do relatório ao qual este relato pertence */
    String identificadorRelatorio;
    RelatoAlterado relatoAlterado;
    Date dataInicio;
    Date dataFim;

    Double pontuacaoObtida;
    List<Valor> valores;
    TipoRelato tipoItem;

}
