package com.reba.rebatest.services;

import com.reba.rebatest.exceptions.ContactDataNonexistentException;
import com.reba.rebatest.exceptions.IncorrectAgeException;
import com.reba.rebatest.model.ContactDetails;
import com.reba.rebatest.model.PeopleStats;
import com.reba.rebatest.model.Person;
import com.reba.rebatest.repository.PersonHqlRepository;
import com.reba.rebatest.repository.PersonRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing operations related to the Person entity.
 *
 * @author Ezequiel Cuccorese
 */
@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final PersonHqlRepository personHqlRepository;
    private final RelationshipService relationshipService;

    /**
     * Constructor that injects the Person repository {@link PersonRepository}.
     *
     * @param personRepository    the Person repository.
     * @param personHqlRepository the HQL Person repository.
     * @param relationshipService manages the relationships of people.
     */
    public PersonService(final PersonRepository personRepository, final PersonHqlRepository personHqlRepository, final RelationshipService relationshipService) {
        this.personRepository = personRepository;
        this.personHqlRepository = personHqlRepository;
        this.relationshipService = relationshipService;
    }

    /**
     * Find a {@link Person} by their ID.
     *
     * @param id the ID of the {@link Person}.
     * @return the {@link Person} if found, or an empty Optional if not.
     */
    public Optional<Person> findById(final Long id) {
        return personRepository.findById(id);
    }

    /**
     * Save a new {@link Person}.
     *
     * @param person the {@link Person} to save.
     * @return the saved {@link Person}.
     */
    public Person save(final Person person) {
        validateAge(person);
        validateContactData(person.getContactDetails());
        return personRepository.save(person);
    }

    /**
     * Get all {@link Person}.
     *
     * @return a list of all {@link Person}.
     */
    public List<Person> findAll() {
        return personRepository.findAll();
    }

    /**
     * Delete a {@link Person}.
     *
     * @param person the {@link Person} to delete.
     */
    @Transactional
    public void delete(final Person person) {
        relationshipService.deleteRelationships(person);
        personRepository.delete(person);
    }

    /**
     * Update an existing {@link Person}.
     *
     * @param personId the ID of the {@link Person} to update.
     * @param person   the {@link Person} with the new data.
     * @return the updated {@link Person}, or null if the {@link Person} is not found.
     */
    public Person update(final Long personId, final Person person) {
        validateAge(person);
        validateContactData(person.getContactDetails());
        final Optional<Person> optionalPerson = personRepository.findById(personId);
        if (optionalPerson.isPresent()) {
            final Person existingPerson = optionalPerson.get();
            existingPerson.setName(person.getName());
            existingPerson.setBirthDate(person.getBirthDate());
            existingPerson.setNationality(person.getNationality());
            existingPerson.setContactDetails(person.getContactDetails());

            return personRepository.save(existingPerson);
        }
        return null;
    }

    /**
     * Validate that a {@link Person} is at least 18 years old.
     *
     * @param person the {@link Person} to validate.
     * @throws IncorrectAgeException if the {@link Person} is less than 18 years old.
     */
    private static void validateAge(final Person person) {
        final LocalDate currentDate = LocalDate.now();
        final LocalDate birthDate = person.getBirthDate();

        if (birthDate.plusYears(18).isAfter(currentDate)) {
            throw new IncorrectAgeException();
        }
    }

    /**
     * Validate contact data.
     *
     * @param contactDetails the contact data to validate
     * @throws ContactDataNonexistentException if all required attributes are null or blank
     */
    private static void validateContactData(final ContactDetails contactDetails) {
        if (contactDetails == null ||
                StringUtils.isBlank(contactDetails.getAddress())
                        && StringUtils.isBlank(contactDetails.getEmail())
                        && StringUtils.isBlank(contactDetails.getPhone())) {
            throw new ContactDataNonexistentException();
        }
    }

    /**
     * Get all people, group them by nationality, and calculate the percentage of people from each
     * nationality.
     *
     * @return the grouping of people by percentages.
     */
    public List<PeopleStats> getPercentageByCountry() {
        return personHqlRepository.getPersonPercentageByCountry();
    }
}
