package com.reba.rebatest.controller;

import com.reba.rebatest.model.Person;
import com.reba.rebatest.model.Relationship;
import com.reba.rebatest.services.PersonService;
import com.reba.rebatest.services.RelationshipService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RelationshipControllerTest {

    @MockBean
    private RelationshipService relationshipService;

    @MockBean
    private PersonService personService;

    @Test
    void getRelationshipTest() {
        final RelationshipController relationshipController = new RelationshipController(relationshipService,
                personService);
        final Person person1 = new Person();
        final Person person2 = new Person();
        final Relationship relationship = new Relationship();
        relationship.setRelationshipType(Relationship.RelationshipType.SIBLING);
        final Long id1 = 1L;
        final Long id2 = 2L;

        when(personService.findById(id1)).thenReturn(Optional.of(person1));
        when(personService.findById(id2)).thenReturn(Optional.of(person2));
        when(relationshipService.getRelation(person1, person2)).thenReturn(relationship);

        final ResponseEntity<String> actualResponse = relationshipController.getRelationship(id1, id2);

        assertEquals(ResponseEntity.ok(relationship.getRelationshipType().getDescription()), actualResponse);
    }

    @Test
    void getRelationshipBadRequestTest() {
        final RelationshipController relationshipController = new RelationshipController(relationshipService, personService);
        final Long id1 = 1L;
        final Long id2 = 2L;

        when(personService.findById(id1)).thenReturn(Optional.empty());
        when(personService.findById(id2)).thenReturn(Optional.of(new Person()));

        final ResponseEntity<String> actualResponse = relationshipController.getRelationship(id1, id2);

        assertEquals(ResponseEntity.badRequest().body("The provided IDs do not correspond to existing people"), actualResponse);
    }

    @Test
    void getRelationshipNotFoundTest() {
        final RelationshipController relationshipController = new RelationshipController(relationshipService, personService);
        final Person person1 = new Person();
        final Person person2 = new Person();
        final Long id1 = 1L;
        final Long id2 = 2L;

        when(personService.findById(id1)).thenReturn(Optional.of(person1));
        when(personService.findById(id2)).thenReturn(Optional.of(person2));
        when(relationshipService.getRelation(person1, person2)).thenReturn(null);

        final ResponseEntity<String> actualResponse = relationshipController.getRelationship(id1, id2);

        assertEquals(ResponseEntity.notFound().build(), actualResponse);
    }
}
