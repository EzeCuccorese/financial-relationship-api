package com.reba.rebatest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Relacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idPersona1")
    private Persona persona1;

    @ManyToOne
    @JoinColumn(name = "idPersona2")
    private Persona persona2;

    @Enumerated(EnumType.STRING)
    private TipoRelacion tipoRelacion;


    public enum TipoRelacion {
        HERMANX("HERMAN@"),
        PRIMX("PRIM@"),
        TIX("TI@");

        private final String descripcion;

        TipoRelacion(final String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }

}
