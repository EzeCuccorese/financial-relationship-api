package com.reba.rebatest.repository;

import com.reba.rebatest.model.Person;
import com.reba.rebatest.model.Relationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, Long> {
    List<Relationship> findByPerson2Id(Long personId);

    List<Relationship> findByPerson1Id(final Long personId);

    void deleteAllByPerson1OrPerson2(Person person, Person person1);

    Relationship findByPerson1AndPerson2OrPerson2AndPerson1(Person person1, Person person2,
                                                            Person person1B, Person personO2B);

}
