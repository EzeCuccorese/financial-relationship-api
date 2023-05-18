package com.reba.rebatest.exceptions;

public class EdadIncorectaException extends RuntimeException {

    public EdadIncorectaException() {
        super("La edad de la persona no puede ser menor a 18 años.");
    }
}


