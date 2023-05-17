package com.reba.rebatest.model;

public enum TipoDocumento {
    DNI("Documento Nacional de Identidad"),
    PASAPORTE("Pasaporte"),
    CI("Cédula de Identidad"),
    LC("Libreta Cívica"),
    LE("Libreta de Enrolamiento");

    private final String descripcion;

    TipoDocumento(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}

