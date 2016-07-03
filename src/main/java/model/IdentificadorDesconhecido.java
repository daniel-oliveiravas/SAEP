/*
 * Copyright (c) 2016. Fábrica de Software - Instituto de Informática (UFG)
 * Creative Commons Attribution 4.0 International License.
 */

package model;

/**
 * Indica ausência de identificador único em objeto,
 * o que impede a persistência do mesmo.
 */
public class IdentificadorDesconhecido extends RuntimeException {

    public IdentificadorDesconhecido(String mensagem) {
        super(mensagem);
    }
}
