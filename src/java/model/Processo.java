package model;

import java.util.Date;
import java.util.List;

public class Processo {

    String identificador;
    String tipoProcesso;
    String matriculaDocente;
    Date dataAbertura;
    Date dataFinalizacao;

    List<Relatorio> relatoriosDocente;

}