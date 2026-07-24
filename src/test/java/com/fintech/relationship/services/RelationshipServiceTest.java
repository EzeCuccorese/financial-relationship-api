package com.fintech.relationship.services;

import com.fintech.relationship.model.Person;
import com.fintech.relationship.model.Relationship;
import com.fintech.relationship.repository.RelationshipRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class RelationshipServiceTest {

    @Mock
    RelationshipRepository relationshipRepository;

    @InjectMocks
    RelationshipService relationshipService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetRelation() {
        final Person person1 = new Person();
        final Person person2 = new Person();
        final Relationship relationship = new Relationship();
        relationship.setPerson1(person1);
        relationship.setPerson2(person2);
        when(relationshipRepository.findByPerson1AndPerson2OrPerson2AndPerson1(
                person1, person2, person2, person1)).thenReturn(relationship);
        final Relationship result = relationshipService.getRelation(person1, person2);
        assertEquals(relationship, result);
    }

    @Test
    public void testGetRelationNotFound() {
        final Person person1 = new Person();
        final Person person2 = new Person();
        when(relationshipRepository.findByPerson1AndPerson2OrPerson2AndPerson1(
                person1, person2, person2, person1)).thenReturn(null);
        final Relationship result = relationshipService.getRelation(person1, person2);
        assertNull(result);
    }

    @Test
    public void testCreateRelation() {
        final Person person1 = new Person();
        final Person person2 = new Person();
        final Relationship relationship = new Relationship();
        relationship.setPerson1(person1);
        relationship.setPerson2(person2);
        relationship.setRelationshipType(Relationship.RelationshipType.AUNT_UNCLE);
        when(relationshipRepository.save(any(Relationship.class))).thenReturn(relationship);
        final Relationship result = relationshipService.createRelation(person1, person2,
                Relationship.RelationshipType.AUNT_UNCLE);
        assertEquals(relationship, result);
    }

    @Test
    public void testDeleteRelationships() {
        final Person person = new Person(); // Debe rellenarse según la implementación de Person
        relationshipService.deleteRelationships(person);
        verify(relationshipRepository, times(1)).deleteAllByPerson1OrPerson2(person, person);
    }

    @Test
    public void testGetRelations() {
        final Person person = new Person(); // Debe rellenarse según la implementación de Person
        final List<Relationship> relacionesEsperadas = new ArrayList<>(); // Se puede rellenar con relaciones de prueba
        when(relationshipRepository.findByPerson1Id(person.getId())).thenReturn(relacionesEsperadas);

        final List<Relationship> relacionesObtenidas = relationshipService.getRelations(person);

        assertEquals(relacionesEsperadas, relacionesObtenidas);
        verify(relationshipRepository, times(1)).findByPerson1Id(person.getId());
    }
}
