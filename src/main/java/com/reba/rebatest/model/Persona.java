package com.reba.rebatest.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private LocalDate fechaNacimiento;

    private String nacionalidad;

    private String numeroDocumento;

    @Enumerated(EnumType.STRING)
    private TipoDocumento tipoDocumento;

    @ManyToOne
    @JoinColumn(name = "pais_id")
    private Pais pais;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "datos_contacto_id")
    private DatosContacto datosContacto;

    @OneToMany(mappedBy = "persona1")
    private List<Relacion> relaciones;

    @ManyToOne
    @JoinColumn(name = "padre_id")
    private Persona padre;

    public Persona(String nombre, LocalDate fechaNacimiento, String nacionalidad, String numeroDocumento,
                   TipoDocumento tipoDocumento, Pais pais, DatosContacto datosContacto) {
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.nacionalidad = nacionalidad;
        this.numeroDocumento = numeroDocumento;
        this.tipoDocumento = tipoDocumento;
        this.pais = pais;
        this.datosContacto = datosContacto;
    }
}
