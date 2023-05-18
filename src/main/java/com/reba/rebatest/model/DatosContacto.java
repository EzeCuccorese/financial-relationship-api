package com.reba.rebatest.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class DatosContacto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String direccion;
    private String telefono;
    private String email;

    public DatosContacto(final String direccion, final String telefono, final String email) {
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
    }
}
