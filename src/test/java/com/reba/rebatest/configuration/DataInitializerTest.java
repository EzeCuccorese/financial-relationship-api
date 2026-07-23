package com.reba.rebatest.configuration;

import com.reba.rebatest.repository.CountryRepository;
import com.reba.rebatest.repository.PersonRepository;
import com.reba.rebatest.repository.RelationshipRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;

class DataInitializerTest {

    @Test
    void testRun() {
        final CountryRepository countryRepository = Mockito.mock(CountryRepository.class);
        final PersonRepository personRepository = Mockito.mock(PersonRepository.class);
        final RelationshipRepository relationshipRepository = Mockito.mock(RelationshipRepository.class);

        final DataInitializer dataInitializer = new DataInitializer(countryRepository, personRepository, relationshipRepository);
        dataInitializer.run();

        verify(countryRepository).saveAll(anyList());
        verify(personRepository).saveAll(anyList());
        verify(relationshipRepository).saveAll(anyList());
    }
}
