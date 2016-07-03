/*
 * Copyright (c) 2016. Fábrica de Software - Instituto de Informática (UFG)
 * Creative Commons Attribution 4.0 International License.
 */

package model;

/**
 * Indica situação excepcional ao avaliar uma
 * regra.
 */
public class TipoInvalido extends RuntimeException {

    public TipoInvalido(String mensagem) {
        super(mensagem);
    }
}
