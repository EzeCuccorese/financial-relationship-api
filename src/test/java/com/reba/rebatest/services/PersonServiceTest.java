package com.reba.rebatest.services;

import com.reba.rebatest.exceptions.ContactDataNonexistentException;
import com.reba.rebatest.exceptions.IncorrectAgeException;
import com.reba.rebatest.model.ContactDetails;
import com.reba.rebatest.model.PeopleStats;
import com.reba.rebatest.model.Person;
import com.reba.rebatest.repository.PersonHqlRepository;
import com.reba.rebatest.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;
    @Mock
    private PersonHqlRepository personHqlRepository;
    @Mock
    private RelationshipService relationshipService;

    @InjectMocks
    private PersonService personService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById_ExistingId_ShouldReturnPerson() {
        // Given
        final Long id = 1L;
        final Person person = new Person();
        person.setId(id);

        when(personRepository.findById(id)).thenReturn(Optional.of(person));

        // When
        final Optional<Person> foundPerson = personService.findById(id);

        //Then
        assertTrue(foundPerson.isPresent());
        assertEquals(person.getId(), foundPerson.get().getId());
        verify(personRepository, times(1)).findById(id);
    }

    @Test
    void testFindById_NonExistingId_ShouldReturnEmptyOptional() {
        // Given
        final Long id = 1L;

        when(personRepository.findById(id)).thenReturn(Optional.empty());

        // When
        final Optional<Person> foundPerson = personService.findById(id);

        //Then
        assertFalse(foundPerson.isPresent());
        verify(personRepository, times(1)).findById(id);
    }

    @Test
    void testSave_ValidPerson_ShouldSavePerson() {
        // Given
        final Person person = new Person();
        person.setName("John Doe");
        person.setBirthDate(LocalDate.of(1990, 1, 1));
        final ContactDetails contactDetails = new ContactDetails();
        contactDetails.setAddress("123 Main St");
        contactDetails.setEmail("john.doe@example.com");
        contactDetails.setPhone("123456789");
        person.setContactDetails(contactDetails);

        when(personRepository.save(person)).thenReturn(person);

        // When
        final Person savedPerson = personService.save(person);

        //Then
        assertNotNull(savedPerson);
        assertEquals(person.getName(), savedPerson.getName());
        assertEquals(person.getBirthDate(), savedPerson.getBirthDate());
        assertEquals(person.getContactDetails(), savedPerson.getContactDetails());
        verify(personRepository, times(1)).save(person);
    }

    @Test
    void testSave_InvalidAge_ShouldThrowAgeIncorectaException() {
        // Given
        final Person person = new Person();
        person.setBirthDate(LocalDate.now());

        // When & Then
        assertThrows(IncorrectAgeException.class, () -> personService.save(person));
        verify(personRepository, never()).save(person);
    }

    @Test
    void testSave_InvalidContactDetails_ShouldThrowDatoContactoInexistenteException() {
        // Given
        final Person person = new Person();
        person.setName("John Doe");
        person.setBirthDate(LocalDate.of(1990, 1, 1));

        // When & Then
        assertThrows(ContactDataNonexistentException.class, () -> personService.save(person));
        verify(personRepository, never()).save(person);
    }

    @Test
    void testSave_InvalidContactDetails_ShouldNoThrowDatoContactoInexistenteException() {
        // Given
        final Person person = new Person();
        person.setName("John Doe");
        person.setContactDetails(new ContactDetails("Marconi 12", null, null));
        person.setBirthDate(LocalDate.of(1990, 1, 1));

        // When & Then
        final Person personSaved = personService.save(person);
        verify(personRepository, times(1)).save(person);

    }

    @Test
    void testDelete_ValidPerson_ShouldDeletePerson() {
        // Given
        final Person person = new Person();
        person.setId(1L);

        // When
        personService.delete(person);

        //Then
        verify(personRepository, times(1)).delete(person);
        verify(relationshipService, times(1)).deleteRelationships(person);
    }

    @Test
    void testUpdate_ExistingId_ValidPerson_ShouldUpdatePerson() {
        // Given
        final Long idPerson = 1L;
        final Person person = new Person();
        person.setName("John Doe");
        person.setBirthDate(LocalDate.of(1990, 1, 1));
        final ContactDetails contactDetails = new ContactDetails();
        contactDetails.setAddress("123 Main St");
        contactDetails.setEmail("john.doe@example.com");
        contactDetails.setPhone("123456789");
        person.setContactDetails(contactDetails);

        when(personRepository.findById(idPerson)).thenReturn(Optional.of(person));
        when(personRepository.save(person)).thenReturn(person);

        // When
        final Person updatedPerson = personService.update(idPerson, person);

        //Then
        assertNotNull(updatedPerson);
        assertEquals(person.getName(), updatedPerson.getName());
        assertEquals(person.getBirthDate(), updatedPerson.getBirthDate());
        assertEquals(person.getContactDetails(), updatedPerson.getContactDetails());
        verify(personRepository, times(1)).findById(idPerson);
        verify(personRepository, times(1)).save(person);
    }

    @Test
    void testUpdate_NonExistingId_ValidPerson_ShouldReturnNull() {
        // Given
        final Long idPerson = 1L;
        final Person person = new Person();
        person.setName("John Doe");
        person.setBirthDate(LocalDate.of(1990, 1, 1));
        final ContactDetails contactDetails = new ContactDetails();
        contactDetails.setAddress("123 Main St");
        contactDetails.setEmail("john.doe@example.com");
        contactDetails.setPhone("123456789");
        person.setContactDetails(contactDetails);

        when(personRepository.findById(idPerson)).thenReturn(Optional.empty());

        // When
        final Person updatedPerson = personService.update(idPerson, person);

        //Then
        assertNull(updatedPerson);
        verify(personRepository, times(1)).findById(idPerson);
        verify(personRepository, never()).save(person);
    }

    @Test
    void testGetPercentageByCountry_EmptyList_ShouldReturnEmptyList() {
        // Given
        when(personHqlRepository.getPersonPercentageByCountry()).thenReturn(Collections.emptyList());

        // When
        final List<PeopleStats> PeopleStatsList = personService.getPercentageByCountry();

        //Then
        assertTrue(PeopleStatsList.isEmpty());
        verify(personHqlRepository, times(1)).getPersonPercentageByCountry();
    }

    @Test
    void testGetPercentageByCountry_List_ShouldReturnList() {
        // Given
        final List<PeopleStats> list = Arrays.asList(new PeopleStats("Argentina", 22),
                new PeopleStats("Brazil", 78));
        when(personHqlRepository.getPersonPercentageByCountry()).thenReturn(list);

        // When
        final List<PeopleStats> PeopleStatsList = personService.getPercentageByCountry();

        //Then
        assertFalse(PeopleStatsList.isEmpty());
        verify(personHqlRepository, times(1)).getPersonPercentageByCountry();
    }

    @Test
    void testSave_InvalidContactDetailsEmailOnly_ShouldNoThrowDatoContactoInexistenteException() {
        // Given
        final Person person = new Person();
        person.setName("John Doe");
        person.setContactDetails(new ContactDetails(null, "john.doe@example.com", null));
        person.setBirthDate(LocalDate.of(1990, 1, 1));

        // When & Then
        final Person save = personService.save(person);
        verify(personRepository, times(1)).save(person);
    }

    @Test
    void testSave_InvalidContactDetailsTelefonoOnly_ShouldNoThrowDatoContactoInexistenteException() {
        // Given
        final Person person = new Person();
        person.setName("John Doe");
        person.setContactDetails(new ContactDetails(null, null, "123456789"));
        person.setBirthDate((LocalDate.of(1990, 1, 1)));

        // When & Then
        final Person save = personService.save(person);
        verify(personRepository, times(1)).save(person);
    }

}
