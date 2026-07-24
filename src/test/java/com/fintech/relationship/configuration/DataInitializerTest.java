package com.fintech.relationship.configuration;

import com.fintech.relationship.repository.CountryRepository;
import com.fintech.relationship.repository.PersonRepository;
import com.fintech.relationship.repository.RelationshipRepository;
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
