package com.fintech.relationship.repository;

import com.fintech.relationship.model.ContactDetails;
import com.fintech.relationship.model.Country;
import com.fintech.relationship.model.DocumentType;
import com.fintech.relationship.model.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

@DataJpaTest
@ActiveProfiles("test")
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private TestEntityManager entityManager;

    private Long personId;

    @BeforeEach
    public void setup() {
        final Country country = new Country("Argentina", "ARG");
        entityManager.persist(country);
        entityManager.flush();

        final ContactDetails contactDetails = new ContactDetails("Mitre 200", "45584422", null);
        entityManager.persist(contactDetails);
        entityManager.flush();

        Person person = new Person("Vanesa", LocalDate.of(1987, 8, 25),
                "Argentina", "33554855",
                DocumentType.DNI, country, contactDetails);
        person = entityManager.persist(person);
        entityManager.flush();

        personId = person.getId();
    }

    @Test
    public void testCreatePerson() {
        //Given
        final Country country = new Country();
        country.setName("United States");
        entityManager.persist(country);
        entityManager.flush();

        final ContactDetails contactDetails = new ContactDetails();
        contactDetails.setPhone("1155667788");
        contactDetails.setEmail("cuccoed@outlook.com");
        entityManager.persist(contactDetails);
        entityManager.flush();

        final Person person = new Person("Ezequiel", LocalDate.of(1985, 8, 30),
                "Argentina", "31898856",
                DocumentType.DNI, country, contactDetails);

        //When
        personRepository.save(person);

        // Then
        final Person savedPerson = entityManager.find(Person.class, person.getId());
        Assertions.assertEquals("Ezequiel", savedPerson.getName());
        Assertions.assertEquals(LocalDate.of(1985, 8, 30), savedPerson.getBirthDate());
        Assertions.assertEquals("Argentina", savedPerson.getNationality());
    }

    @Test
    public void testRead() {
        //Given and When
        final Person person = personRepository.findById(personId).orElse(null);

        //Then
        Assertions.assertNotNull(person);
        Assertions.assertEquals("Vanesa", person.getName());
        Assertions.assertEquals(LocalDate.of(1987, 8, 25), person.getBirthDate());
        Assertions.assertEquals("Argentina", person.getNationality());
    }

    @Test
    public void testUpdate() {
        //Given
        final Person person = personRepository.findById(personId).orElse(null);
        Assertions.assertNotNull(person);
        person.setName("Updated Name");

        //When
        personRepository.save(person);

        //Then
        final Person updatedPerson = entityManager.find(Person.class, person.getId());
        Assertions.assertEquals("Updated Name", updatedPerson.getName());
    }

    @Test
    public void testDelete() {
        //Given
        final Person person = personRepository.findById(personId).orElse(null);
        Assertions.assertNotNull(person);

        //When
        personRepository.delete(person);

        // Then
        final Person deletedPerson = entityManager.find(Person.class, person.getId());
        Assertions.assertNull(deletedPerson);
    }
}
