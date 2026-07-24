package com.reba.rebatest.controller;

import com.reba.rebatest.model.Person;
import com.reba.rebatest.services.PersonService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest
public class PersonControllerTest {

    @MockitoBean
    private PersonService personService;

    @Test
    void getAllPeopleTest() {
        final PersonController personController = new PersonController(personService);
        final Person person1 = new Person();
        final Person person2 = new Person();
        final List<Person> expectedPeople = Arrays.asList(person1, person2);

        Mockito.when(personService.findAll()).thenReturn(expectedPeople);

        final List<Person> actualPeople = personController.getAllPeople();

        assertEquals(expectedPeople, actualPeople);
    }

    @Test
    void getPersonByIdTest() {
        final PersonController personController = new PersonController(personService);
        final Person person = new Person();
        final Long id = 1L;

        Mockito.when(personService.findById(id)).thenReturn(Optional.of(person));

        final ResponseEntity<Person> actualResponse = personController.getPersonById(id);

        assertEquals(ResponseEntity.ok(person), actualResponse);
    }

    @Test
    void createPersonTest() {
        final PersonController personController = new PersonController(personService);
        final Person person = new Person();

        Mockito.when(personService.save(any(Person.class))).thenReturn(person);

        final ResponseEntity<?> actualResponse = personController.createPerson(person);

        assertEquals( ResponseEntity.status(HttpStatus.CREATED).body(person), actualResponse);
    }

    @Test
    void updatePersonTest() {
        final PersonController personController = new PersonController(personService);
        final Person person = new Person();
        final Long id = 1L;

        Mockito.when(personService.update(eq(id), any(Person.class))).thenReturn(person);

        final ResponseEntity<?> actualResponse = personController.updatePerson(id, person);

        assertEquals(ResponseEntity.ok(person), actualResponse);
    }

    @Test
    void deletePersonTest() {
        final PersonController personController = new PersonController(personService);
        final Person person = new Person();
        final Long id = 1L;

        Mockito.when(personService.findById(id)).thenReturn(Optional.of(person));

        final ResponseEntity<?> actualResponse = personController.deletePerson(id);

        assertEquals(ResponseEntity.noContent().build(), actualResponse);
    }

    @Test
    void establishFatherTest() {
        final PersonController personController = new PersonController(personService);
        final Person father = new Person();
        final Person child = new Person();
        final Long idFather = 1L;
        final Long idChild = 2L;

        Mockito.when(personService.findById(idFather)).thenReturn(Optional.of(father));
        Mockito.when(personService.findById(idChild)).thenReturn(Optional.of(child));

        final ResponseEntity<String> actualResponse = personController.establishFather(idFather, idChild);

        assertEquals(ResponseEntity.ok("Set " + father.getName()
                + " as father of " + child.getName()), actualResponse);
    }

    @Test
    void getPersonByIdNotFoundTest() {
        final PersonController personController = new PersonController(personService);
        final Long id = 99L;
        Mockito.when(personService.findById(id)).thenReturn(Optional.empty());

        final ResponseEntity<Person> actualResponse = personController.getPersonById(id);

        assertEquals(ResponseEntity.notFound().build(), actualResponse);
    }

    @Test
    void updatePersonNotFoundTest() {
        final PersonController personController = new PersonController(personService);
        final Person person = new Person();
        final Long id = 99L;
        Mockito.when(personService.update(eq(id), any(Person.class))).thenReturn(null);

        final ResponseEntity<?> actualResponse = personController.updatePerson(id, person);

        assertEquals(ResponseEntity.notFound().build(), actualResponse);
    }

    @Test
    void deletePersonNotFoundTest() {
        final PersonController personController = new PersonController(personService);
        final Long id = 99L;
        Mockito.when(personService.findById(id)).thenReturn(Optional.empty());

        final ResponseEntity<?> actualResponse = personController.deletePerson(id);

        assertEquals(ResponseEntity.notFound().build(), actualResponse);
    }

    @Test
    void establishFatherBadRequestTest() {
        final PersonController personController = new PersonController(personService);
        final Long idFather = 1L;
        final Long idChild = 2L;
        Mockito.when(personService.findById(idFather)).thenReturn(Optional.empty());
        Mockito.when(personService.findById(idChild)).thenReturn(Optional.of(new Person()));

        final ResponseEntity<String> actualResponse = personController.establishFather(idFather, idChild);

        assertEquals(ResponseEntity.badRequest().body("The provided IDs do not correspond to existing people"), actualResponse);
    }

    @Test
    void getStatsTest() {
        final PersonController personController = new PersonController(personService);
        final List<com.reba.rebatest.model.PeopleStats> stats = List.of(new com.reba.rebatest.model.PeopleStats("Argentina", 100));
        Mockito.when(personService.getPercentageByCountry()).thenReturn(stats);

        final ResponseEntity<List<com.reba.rebatest.model.PeopleStats>> actualResponse = personController.getStats();

        assertEquals(ResponseEntity.ok(stats), actualResponse);
    }
}
