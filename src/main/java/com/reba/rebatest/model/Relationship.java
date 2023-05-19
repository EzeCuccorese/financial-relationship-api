package com.reba.rebatest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Relationship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idPerson1")
    private Person person1;

    @ManyToOne
    @JoinColumn(name = "idPerson2")
    private Person person2;

    @Enumerated(EnumType.STRING)
    private RelationshipType relationshipType;


    public enum RelationshipType {
        SIBLING("HERMAN@"),
        COUSIN("PRIM@"),
        AUNT_UNCLE("TI@");

        private final String description;

        RelationshipType(final String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

}
