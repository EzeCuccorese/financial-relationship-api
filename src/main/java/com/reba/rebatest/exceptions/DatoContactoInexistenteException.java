package com.reba.rebatest.exceptions;

public class DatoContactoInexistenteException extends RuntimeException {

    public DatoContactoInexistenteException() {
        super("Debe tener al menos un dato de contacto.");
    }
}
