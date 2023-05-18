package com.reba.rebatest.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "El ID único de la persona")
    private Long id;

    @Schema(description = "El nombre de la persona")
    @NotNull
    @NotBlank
    private String nombre;

    @Schema(description = "La fecha de nacimiento de la persona")
    @NotNull
    private LocalDate fechaNacimiento;

    @Schema(description = "La nacionalidad de la persona")
    @NotNull
    @NotBlank
    private String nacionalidad;

    @Schema(description = "El número de documento de la persona")
    private String numeroDocumento;

    @Enumerated(EnumType.STRING)
    @Schema(description = "El tipo de documento de la persona")
    private TipoDocumento tipoDocumento;

    @ManyToOne
    @JoinColumn(name = "pais_id")
    @Schema(description = "El país de la persona")
    private Pais pais;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "datos_contacto_id")
    @Schema(description = "Los datos de contacto de la persona")
    private DatosContacto datosContacto;

    @OneToMany(mappedBy = "persona1")
    @Schema(description = "Las relaciones de la persona")
    private List<Relacion> relaciones;

    @ManyToOne
    @JoinColumn(name = "padre_id")
    @Schema(description = "El padre de la persona")
    private Persona padre;

    public Persona(final String nombre, final LocalDate fechaNacimiento, final String nacionalidad,
                   final String numeroDocumento, final TipoDocumento tipoDocumento, final Pais pais,
                   final DatosContacto datosContacto) {
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.nacionalidad = nacionalidad;
        this.numeroDocumento = numeroDocumento;
        this.tipoDocumento = tipoDocumento;
        this.pais = pais;
        this.datosContacto = datosContacto;
    }
}
