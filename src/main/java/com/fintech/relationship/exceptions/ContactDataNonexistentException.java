package com.fintech.relationship.exceptions;

public class ContactDataNonexistentException extends RuntimeException {

    public ContactDataNonexistentException() {
        super("Debe tener al menos un dato de contacto.");
    }
}
