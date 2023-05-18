package com.reba.rebatest.services;

import com.reba.rebatest.exceptions.DatoContactoInexistenteException;
import com.reba.rebatest.exceptions.EdadIncorectaException;
import com.reba.rebatest.model.DatosContacto;
import com.reba.rebatest.model.Persona;
import com.reba.rebatest.model.PersonasStats;
import com.reba.rebatest.repository.PersonaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PersonaServiceTest {

    @Mock
    private PersonaRepository personaRepository;

    @Mock
    private RelacionService relacionService;

    @InjectMocks
    private PersonaService personaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById_ExistingId_ShouldReturnPersona() {
        // Given
        final Long id = 1L;
        final Persona persona = new Persona();
        persona.setId(id);

        when(personaRepository.findById(id)).thenReturn(Optional.of(persona));

        // When
        final Optional<Persona> foundPersona = personaService.findById(id);

        //Then
        assertTrue(foundPersona.isPresent());
        assertEquals(persona.getId(), foundPersona.get().getId());
        verify(personaRepository, times(1)).findById(id);
    }

    @Test
    void testFindById_NonExistingId_ShouldReturnEmptyOptional() {
        // Given
        final Long id = 1L;

        when(personaRepository.findById(id)).thenReturn(Optional.empty());

        // When
        final Optional<Persona> foundPersona = personaService.findById(id);

        //Then
        assertFalse(foundPersona.isPresent());
        verify(personaRepository, times(1)).findById(id);
    }

    @Test
    void testSave_ValidPersona_ShouldSavePersona() {
        // Given
        final Persona persona = new Persona();
        persona.setNombre("John Doe");
        persona.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        final DatosContacto datosContacto = new DatosContacto();
        datosContacto.setDireccion("123 Main St");
        datosContacto.setEmail("john.doe@example.com");
        datosContacto.setTelefono("123456789");
        persona.setDatosContacto(datosContacto);

        when(personaRepository.save(persona)).thenReturn(persona);

        // When
        final Persona savedPersona = personaService.save(persona);

        //Then
        assertNotNull(savedPersona);
        assertEquals(persona.getNombre(), savedPersona.getNombre());
        assertEquals(persona.getFechaNacimiento(), savedPersona.getFechaNacimiento());
        assertEquals(persona.getDatosContacto(), savedPersona.getDatosContacto());
        verify(personaRepository, times(1)).save(persona);
    }

    @Test
    void testSave_InvalidEdad_ShouldThrowEdadIncorectaException() {
        // Given
        final Persona persona = new Persona();
        persona.setFechaNacimiento(LocalDate.now());

        // When & Then
        assertThrows(EdadIncorectaException.class, () -> personaService.save(persona));
        verify(personaRepository, never()).save(persona);
    }

    @Test
    void testSave_InvalidDatosContacto_ShouldThrowDatoContactoInexistenteException() {
        // Given
        final Persona persona = new Persona();
        persona.setNombre("John Doe");
        persona.setFechaNacimiento(LocalDate.of(1990, 1, 1));

        // When & Then
        assertThrows(DatoContactoInexistenteException.class, () -> personaService.save(persona));
        verify(personaRepository, never()).save(persona);
    }

    @Test
    void testSave_InvalidDatosContacto_ShouldNoThrowDatoContactoInexistenteException() {
        // Given
        final Persona persona = new Persona();
        persona.setNombre("John Doe");
        persona.setDatosContacto(new DatosContacto("Marconi 12", null, null));
        persona.setFechaNacimiento(LocalDate.of(1990, 1, 1));

        // When & Then
        final Persona save = personaService.save(persona);
        verify(personaRepository, times(1)).save(persona);
    }
    @Test
    void testDelete_ValidPersona_ShouldDeletePersona() {
        // Given
        final Persona persona = new Persona();
        persona.setId(1L);

        // When
        personaService.delete(persona);

        //Then
        verify(personaRepository, times(1)).delete(persona);
    }

    @Test
    void testUpdate_ExistingId_ValidPersona_ShouldUpdatePersona() {
        // Given
        final Long idPersona = 1L;
        final Persona persona = new Persona();
        persona.setNombre("John Doe");
        persona.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        final DatosContacto datosContacto = new DatosContacto();
        datosContacto.setDireccion("123 Main St");
        datosContacto.setEmail("john.doe@example.com");
        datosContacto.setTelefono("123456789");
        persona.setDatosContacto(datosContacto);

        when(personaRepository.findById(idPersona)).thenReturn(Optional.of(persona));
        when(personaRepository.save(persona)).thenReturn(persona);

        // When
        final Persona updatedPersona = personaService.update(idPersona, persona);

        //Then
        assertNotNull(updatedPersona);
        assertEquals(persona.getNombre(), updatedPersona.getNombre());
        assertEquals(persona.getFechaNacimiento(), updatedPersona.getFechaNacimiento());
        assertEquals(persona.getDatosContacto(), updatedPersona.getDatosContacto());
        verify(personaRepository, times(1)).findById(idPersona);
        verify(personaRepository, times(1)).save(persona);
    }

    @Test
    void testUpdate_NonExistingId_ValidPersona_ShouldReturnNull() {
        // Given
        final Long idPersona = 1L;
        final Persona persona = new Persona();
        persona.setNombre("John Doe");
        persona.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        final DatosContacto datosContacto = new DatosContacto();
        datosContacto.setDireccion("123 Main St");
        datosContacto.setEmail("john.doe@example.com");
        datosContacto.setTelefono("123456789");
        persona.setDatosContacto(datosContacto);

        when(personaRepository.findById(idPersona)).thenReturn(Optional.empty());

        // When
        final Persona updatedPersona = personaService.update(idPersona, persona);

        //Then
        assertNull(updatedPersona);
        verify(personaRepository, times(1)).findById(idPersona);
        verify(personaRepository, never()).save(persona);
    }

    @Test
    void testGetPercentageByCountry_EmptyList_ShouldReturnEmptyList() {
        // Given
        when(personaRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        final List<PersonasStats> personasStatsList = personaService.getPercentageByCountry();

        //Then
        assertTrue(personasStatsList.isEmpty());
        verify(personaRepository, times(1)).findAll();
    }

    @Test
    void testGetPercentageByCountry_NonEmptyList_ShouldReturnPercentageByCountry() {
        // Given
        final List<Persona> personaList = new ArrayList<>();
        final Persona persona1 = new Persona();
        persona1.setNacionalidad("USA");
        personaList.add(persona1);
        final Persona persona2 = new Persona();
        persona2.setNacionalidad("USA");
        personaList.add(persona2);
        final Persona persona3 = new Persona();
        persona3.setNacionalidad("Canada");
        personaList.add(persona3);

        when(personaRepository.findAll()).thenReturn(personaList);

        // When
        final List<PersonasStats> personasStatsList = personaService.getPercentageByCountry();

        //Then
        assertEquals(2, personasStatsList.size());
        for (final PersonasStats personasStats : personasStatsList) {
            if (personasStats.getCountry().equals("USA")) {
                assertEquals(66.6666, personasStats.getPercentage(), 0.001);
            } else if (personasStats.getCountry().equals("Canada")) {
                assertEquals(33.3333, personasStats.getPercentage(), 0.001);
            }
        }
        verify(personaRepository, times(1)).findAll();
    }

    @Test
    void testSave_InvalidDatosContactoEmailOnly_ShouldNoThrowDatoContactoInexistenteException() {
        // Given
        final Persona persona = new Persona();
        persona.setNombre("John Doe");
        persona.setDatosContacto(new DatosContacto(null, "john.doe@example.com", null));
        persona.setFechaNacimiento(LocalDate.of(1990, 1, 1));

        // When & Then
        final Persona save = personaService.save(persona);
        verify(personaRepository, times(1)).save(persona);
    }

    @Test
    void testSave_InvalidDatosContactoTelefonoOnly_ShouldNoThrowDatoContactoInexistenteException() {
        // Given
        final Persona persona = new Persona();
        persona.setNombre("John Doe");
        persona.setDatosContacto(new DatosContacto(null, null, "123456789"));
        persona.setFechaNacimiento(LocalDate.of(1990, 1, 1));

        // When & Then
        final Persona save = personaService.save(persona);
        verify(personaRepository, times(1)).save(persona);
    }

}
