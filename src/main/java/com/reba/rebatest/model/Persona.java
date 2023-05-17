package com.reba.rebatest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Persona {

    @EmbeddedId
    private PersonaId id;

    private String nombre;

    private LocalDate fechaNacimiento;

    private String nacionalidad;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "datos_contacto_id")
    private DatosContacto datosContacto;

}
