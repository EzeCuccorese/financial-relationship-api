package com.reba.rebatest.controller;

import com.reba.rebatest.model.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled("Se deja desahabilitado por usar muchos recursos para iniciar.")
@Testcontainers
@ActiveProfiles("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerIntegrationTest {

    @Container
    private static final MySQLContainer<?> container = new MySQLContainer<>("mysql:8.0.33");

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;


    @DynamicPropertySource
    static void properties(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

    private String getRootUrl() {
        return "http://localhost:" + port;
    }

    private Person createTestPerson() {
        final ContactDetails contactDetails = new ContactDetails("Test Address",
                "123456789", "test@mail.com");
        final Country country = new Country(1L, "Argentina", "AR");
        return new Person("Test", LocalDate.now().minusYears(20),
                "Test Nationality", "1234", DocumentType.DNI, country, contactDetails);
    }
    @Test
    @Order(1) //Para mantener la prueba con los datos iniciales este test se ejecuta primero.
    public void testGetStats() {
        final ParameterizedTypeReference<List<PeopleStats>> responseType = new ParameterizedTypeReference<>() {
        };

        final ResponseEntity<List<PeopleStats>> response = restTemplate.exchange(
                getRootUrl() + "/api/people/stats", HttpMethod.GET, null, responseType);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(Objects.requireNonNull(response.getBody()).get(0).getPercentage()).isEqualTo(33.0);
        assertThat(response.getBody().get(0).getCountry()).isEqualTo("Argentina");
        assertThat(response.getBody().get(1).getPercentage()).isEqualTo(33.0);
        assertThat(response.getBody().get(1).getCountry()).isEqualTo("Uruguay");
    }
    @Test
    public void testCreatePerson() {
        final Person person = createTestPerson();
        person.setDocumentNumber("1234");
        final ResponseEntity<Person> responseEntity = restTemplate.postForEntity(getRootUrl() +
                "/api/people", person, Person.class);
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(201);
        assertThat(Objects.requireNonNull(responseEntity.getBody()).getName()).isEqualTo(person.getName());
    }

    @Test
    public void testGetPersonById() {
        final Person person = createTestPerson();
        person.setDocumentNumber("12345");
        final ResponseEntity<Person> responseEntity = restTemplate.postForEntity(getRootUrl() +
                "/api/people", person, Person.class);
        final Long id = Objects.requireNonNull(responseEntity.getBody()).getId();
        final ResponseEntity<Person> response = restTemplate.getForEntity(getRootUrl() + "/api/people/" +
                id, Person.class);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(Objects.requireNonNull(response.getBody()).getName()).isEqualTo(person.getName());
    }


}
