package com.reba.rebatest.services;

import com.reba.rebatest.exceptions.EdadIncorectaException;
import com.reba.rebatest.model.Persona;
import com.reba.rebatest.repository.PersonaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PersonaServiceTest {

    @Mock
    PersonaRepository personaRepository;

    @InjectMocks
    PersonaService personaService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindById() {
        final Long id = 1L;
        final Persona persona = new Persona();
        persona.setId(id);
        when(personaRepository.findById(id)).thenReturn(Optional.of(persona));
        final Optional<Persona> result = personaService.findById(id);
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
    }

    @Test
    public void testSave() {
        final Persona persona = new Persona();
        persona.setFechaNacimiento(LocalDate.now().minusYears(20));
        when(personaRepository.save(persona)).thenReturn(persona);
        final Persona result = personaService.save(persona);
        assertEquals(persona, result);
    }

    @Test
    public void testSaveUnderAge() {
        final Persona persona = new Persona();
        persona.setFechaNacimiento(LocalDate.now().minusYears(15));
        assertThrows(EdadIncorectaException.class, () -> personaService.save(persona));
    }

    @Test
    public void testFindAll() {
        when(personaRepository.findAll()).thenReturn(Arrays.asList(new Persona(), new Persona()));
        final List<Persona> result = personaService.findAll();
        assertEquals(2, result.size());
    }

    @Test
    public void testDelete() {
        final Persona persona = new Persona();
        doNothing().when(personaRepository).delete(persona);
        personaService.delete(persona);
        verify(personaRepository, times(1)).delete(persona);
    }

    @Test
    public void testUpdate() {
        final Long id = 1L;
        final Persona persona = new Persona();
        persona.setId(id);
        persona.setFechaNacimiento(LocalDate.now().minusYears(20));
        when(personaRepository.findById(id)).thenReturn(Optional.of(persona));
        when(personaRepository.save(persona)).thenReturn(persona);
        final Persona result = personaService.update(id, persona);
        assertEquals(persona, result);
    }

    @Test
    public void testUpdateUnderAge() {
        final Long id = 1L;
        final Persona persona = new Persona();
        persona.setId(id);
        persona.setFechaNacimiento(LocalDate.now().minusYears(15));
        when(personaRepository.findById(id)).thenReturn(Optional.of(persona));
        assertThrows(EdadIncorectaException.class, () -> personaService.update(id, persona));
    }

    @Test
    public void testUpdateNotFound() {
        final Long id = 1L;
        final Persona persona = new Persona();
        persona.setId(id);
        persona.setFechaNacimiento((LocalDate.now().minusYears(35)));
        when(personaRepository.findById(id)).thenReturn(Optional.empty());
        final Persona result = personaService.update(id, persona);
        assertNull(result);
    }

    @Test
    public void getPercentageByCountryTest() {
        // Given
        final Persona p1 = new Persona();
        p1.setNacionalidad("Argentina");

        final Persona p2 = new Persona();
        p2.setNacionalidad("Argentina");

        final Persona p3 = new Persona();
        p3.setNacionalidad("Brasil");

        final List<Persona> allPersonas = Arrays.asList(p1, p2, p3);

        when(personaService.findAll()).thenReturn(allPersonas);

        // When
        final List<PersonasStats> result = personaService.getPercentageByCountry();

        // Then
        assertEquals(2, result.size());

        final PersonasStats argentinaStats = result.stream()
                .filter(ps -> ps.getCountry().equals("Argentina"))
                .findFirst()
                .orElse(null);

        final PersonasStats brasilStats = result.stream()
                .filter(ps -> ps.getCountry().equals("Brasil"))
                .findFirst()
                .orElse(null);

        assertNotNull(argentinaStats);
        assertEquals(66.67, argentinaStats.getPercentage(), 0.01);
        assertNotNull(brasilStats);
        assertEquals(33.33, brasilStats.getPercentage(), 0.01);
    }
}
