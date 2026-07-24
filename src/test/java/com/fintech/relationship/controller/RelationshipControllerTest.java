package com.fintech.relationship.controller;

import com.fintech.relationship.model.Person;
import com.fintech.relationship.model.Relationship;
import com.fintech.relationship.services.PersonService;
import com.fintech.relationship.services.RelationshipService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RelationshipControllerTest {

    @MockitoBean
    private RelationshipService relationshipService;

    @MockitoBean
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
