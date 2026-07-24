package com.fintech.relationship.repository;

import com.fintech.relationship.model.ContactDetails;
import com.fintech.relationship.model.Country;
import com.fintech.relationship.model.DocumentType;
import com.fintech.relationship.model.PeopleStats;
import com.fintech.relationship.model.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
@Import(PersonHqlRepository.class)
public class PersonHqlRepositoryTest {

    @Autowired
    private PersonHqlRepository personHqlRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    public void setup() {
        final Country country = new Country("Argentina", "ARG");
        entityManager.persist(country);

        final ContactDetails contactDetails = new ContactDetails("Mitre 200", "45584422", "test@test.com");
        entityManager.persist(contactDetails);

        final Person person = new Person("Vanesa", LocalDate.of(1987, 8, 25),
                "Argentina", "33554855",
                DocumentType.DNI, country, contactDetails);
        entityManager.persist(person);
        entityManager.flush();
    }

    @Test
    public void testGetPersonPercentageByCountry() {
        final List<PeopleStats> stats = personHqlRepository.getPersonPercentageByCountry();
        Assertions.assertNotNull(stats);
        Assertions.assertFalse(stats.isEmpty());
        Assertions.assertEquals("Argentina", stats.get(0).getCountry());
    }
}
