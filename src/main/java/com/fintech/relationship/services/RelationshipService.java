package com.fintech.relationship.services;

import com.fintech.relationship.model.Person;
import com.fintech.relationship.model.Relationship;
import com.fintech.relationship.repository.RelationshipRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service that manages operations of the Relationship entity.
 */
@Service
public class RelationshipService {

    private final RelationshipRepository relationshipRepository;

    /**
     * Constructor of the RelationshipService class.
     *
     * @param relationshipRepository relationship repository
     */
    public RelationshipService(final RelationshipRepository relationshipRepository) {
        this.relationshipRepository = relationshipRepository;
    }

    /**
     * Gets a relationship between two people.
     *
     * @param person1 the first person in the relationship
     * @param person2 the second person in the relationship
     * @return a Relationship entity if a relationship exists between the two people, or null if it doesn't
     */
    public Relationship getRelation(final Person person1, final Person person2) {
        return relationshipRepository.findByPerson1AndPerson2OrPerson2AndPerson1(person1, person2, person2,
                person1);
    }

    /**
     * Creates a new relationship between two people.
     *
     * @param person1          the first person in the new relationship
     * @param person2          the second person in the new relationship
     * @param relationshipType the type of relationship (e.g., FRIENDS)
     * @return the new Relationship entity
     */
    public Relationship createRelation(final Person person1, final Person person2,
                                       final Relationship.RelationshipType relationshipType) {
        final Relationship relationship = new Relationship();
        relationship.setPerson1(person1);
        relationship.setPerson2(person2);
        relationship.setRelationshipType(relationshipType);
        return relationshipRepository.save(relationship);
    }

    /**
     * Deletes all relationships in which a specific person is involved.
     *
     * @param person the person whose relationships you want to delete
     */
    public void deleteRelationships(final Person person) {
        relationshipRepository.deleteAllByPerson1OrPerson2(person, person);
    }

    /**
     * Gets all relationships in which a specific person is the first person.
     *
     * @param person the person whose relationships you want to get
     * @return a list of Relationship entities where the specified person is the first person in the relationship
     */
    public List<Relationship> getRelations(final Person person) {
        final List<Relationship> relationships = relationshipRepository.findByPerson1Id(person.getId());
        relationships.addAll(relationshipRepository.findByPerson2Id(person.getId()));
        return relationships;
    }
}
