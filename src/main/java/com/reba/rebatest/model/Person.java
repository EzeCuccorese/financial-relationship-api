package com.reba.rebatest.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"documentNumber", "documentType", "country_id"}))
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The unique ID of the person")
    private Long id;

    @Schema(description = "The name of the person")
    @NotNull
    @NotBlank
    private String name;

    @Schema(description = "The birth date of the person")
    @NotNull
    private LocalDate birthDate;

    @Schema(description = "The nationality of the person")
    @NotNull
    @NotBlank
    private String nationality;

    @Schema(description = "The document number of the person")
    private String documentNumber;

    @Enumerated(EnumType.STRING)
    @Schema(description = "The type of document of the person")
    private DocumentType documentType;

    @ManyToOne
    @JoinColumn(name = "country_id")
    @Schema(description = "The country of the person")
    private Country country;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contact_details_id")
    @Schema(description = "The contact details of the person")
    private ContactDetails contactDetails;

    @ManyToOne
    @JoinColumn(name = "pather_id")
    @Schema(description = "The mother/father of the person")
    private Person pather;

    public Person(final String name, final LocalDate birthDate, final String nationality,
                  final String documentNumber, final DocumentType documentType, final Country country,
                  final ContactDetails contactDetails) {
        this.name = name;
        this.birthDate = birthDate;
        this.nationality = nationality;
        this.documentNumber = documentNumber;
        this.documentType = documentType;
        this.country = country;
        this.contactDetails = contactDetails;
    }
}
