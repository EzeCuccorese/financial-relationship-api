package com.fintech.relationship.model;

public enum DocumentType {
    DNI("Documento Nacional de Identidad"),
    PASSPORT("Pasaporte"),
    CI("Cédula de Identidad"),
    LC("Libreta Cívica"),
    LE("Libreta de Enrolamiento");

    private final String description;

    DocumentType(final String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

