package com.reba.rebatest.exceptions;

public class IncorrectAgeException extends RuntimeException {

    public IncorrectAgeException() {
        super("La edad de la person no puede ser menor a 18 años.");
    }
}


