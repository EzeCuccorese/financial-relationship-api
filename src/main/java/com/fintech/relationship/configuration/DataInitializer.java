package com.fintech.relationship.configuration;

import com.fintech.relationship.model.*;
import com.fintech.relationship.repository.CountryRepository;
import com.fintech.relationship.repository.PersonRepository;
import com.fintech.relationship.repository.RelationshipRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CountryRepository countryRepository;
    private final PersonRepository personRepository;
    private final RelationshipRepository relationshipRepository;

    public DataInitializer(final CountryRepository countryRepository, final PersonRepository personRepository,
                           final RelationshipRepository relationshipRepository) {
        this.countryRepository = countryRepository;
        this.personRepository = personRepository;
        this.relationshipRepository = relationshipRepository;
    }

    @Override
    public void run(final String... args) {
        loadExampleCountries();
    }

    private void loadExampleCountries() {
        // Creación de personas de ejemplo
        final Person person1 = new Person("Juan Pérez", LocalDate.of(1990, 1, 1), "Argentina", "123456789",
                DocumentType.DNI, null, null);
        final Person person2 = new Person("María Rodríguez", LocalDate.of(1995, 5, 10), "Uruguay", "987654321",
                DocumentType.PASSPORT, null, null);
        final Person person3 = new Person("Diego González", LocalDate.of(1985, 3, 15), "Chile", "456789123",
                DocumentType.CI, null, null);
        final Person person4 = new Person("Camila Fernández", LocalDate.of(1992, 9, 20), "Brasil", "789123456",
                DocumentType.LC, null, null);
        final Person person5 = new Person("Lucas Silva", LocalDate.of(1988, 6, 25), "Argentina", "321654987",
                DocumentType.LE, null, null);
        final Person person6 = new Person("Valentina Lima", LocalDate.of(1997, 11, 30), "Uruguay", "654789321",
                DocumentType.DNI, null, null);

        // Creación de datos de contacto de ejemplo
        final ContactDetails contactDetails1 = new ContactDetails("Calle 123", "123456789", "juan.perez@example.com");
        final ContactDetails contactDetails2 = new ContactDetails("Avenida Uruguay", "987654321", "maria.rodriguez" +
                "@example.com");
        final ContactDetails contactDetails3 = new ContactDetails("Calle Santiago", "456789123", "diego.gonzalez" +
                "@example" +
                ".com");
        final ContactDetails contactDetails4 = new ContactDetails("Rua Brasil", "789123456", "camila" +
                ".fernandez@example" +
                ".com");
        final ContactDetails contactDetails5 = new ContactDetails("Avenida Buenos Aires", "321654987", "lucas" +
                ".silva@example" +
                ".com");
        final ContactDetails contactDetails6 = new ContactDetails("Calle Montevideo", "654789321", "valentina" +
                ".lima@example.com");

        person1.setContactDetails(contactDetails1);
        person2.setContactDetails(contactDetails2);
        person3.setContactDetails(contactDetails3);
        person4.setContactDetails(contactDetails4);
        person5.setContactDetails(contactDetails5);
        person6.setContactDetails(contactDetails6);

        // Creación de países de ejemplo
        final Country country1 = new Country("Argentina", "AR");
        final Country country2 = new Country("Uruguay", "UY");
        final Country country3 = new Country("Chile", "CL");
        final Country country4 = new Country("Brasil", "BR");

        // Asignación de países a las personas
        person1.setCountry(country1);
        person2.setCountry(country2);
        person3.setCountry(country3);
        person4.setCountry(country4);
        person5.setCountry(country1);
        person6.setCountry(country2);

        // Creación de relaciones de ejemplo
        final Relationship relationship1 = new Relationship(null, person1, person2,
                Relationship.RelationshipType.SIBLING);
        final Relationship relationship2 = new Relationship(null, person3, person4,
                Relationship.RelationshipType.COUSIN);
        final Relationship relationship3 = new Relationship(null, person5, person6,
                Relationship.RelationshipType.AUNT_UNCLE);

        person1.setPather(person4);
        // Persistir los datos en la base de datos
        countryRepository.saveAll(Arrays.asList(country1, country2, country3, country4));
        personRepository.saveAll(Arrays.asList(person1, person2, person3, person4, person5, person6));
        relationshipRepository.saveAll(Arrays.asList(relationship1, relationship2, relationship3));

    }
}

