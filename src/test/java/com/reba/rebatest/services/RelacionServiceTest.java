package com.reba.rebatest.services;

import com.reba.rebatest.model.Persona;
import com.reba.rebatest.model.Relacion;
import com.reba.rebatest.repository.RelacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class RelacionServiceTest {

    @Mock
    RelacionRepository relacionRepository;

    @InjectMocks
    RelacionService relacionService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testObtenerRelacion() {
        final Persona persona1 = new Persona();
        final Persona persona2 = new Persona();
        final Relacion relacion = new Relacion();
        relacion.setPersona1(persona1);
        relacion.setPersona2(persona2);
        when(relacionRepository.findByPersona1AndPersona2OrPersona2AndPersona1(
                persona1, persona2, persona2, persona1)).thenReturn(Optional.of(relacion));
        final Relacion result = relacionService.obtenerRelacion(persona1, persona2);
        assertEquals(relacion, result);
    }

    @Test
    public void testObtenerRelacionNotFound() {
        final Persona persona1 = new Persona();
        final Persona persona2 = new Persona();
        when(relacionRepository.findByPersona1AndPersona2OrPersona2AndPersona1(
                persona1, persona2, persona2, persona1)).thenReturn(Optional.empty());
        final Relacion result = relacionService.obtenerRelacion(persona1, persona2);
        assertNull(result);
    }

    @Test
    public void testCrearRelacion() {
        final Persona persona1 = new Persona();
        final Persona persona2 = new Persona();
        final Relacion relacion = new Relacion();
        relacion.setPersona1(persona1);
        relacion.setPersona2(persona2);
        relacion.setTipoRelacion(Relacion.TipoRelacion.TIX);
        when(relacionRepository.save(any(Relacion.class))).thenReturn(relacion);
        final Relacion result = relacionService.crearRelacion(persona1, persona2, Relacion.TipoRelacion.TIX);
        assertEquals(relacion, result);
    }

    @Test
    public void testEliminarRelaciones() {
        final Persona persona = new Persona(); // Debe rellenarse según la implementación de Persona
        relacionService.eliminarRelaciones(persona);
        verify(relacionRepository, times(1)).deleteAllByPersona1OrPersona2(persona, persona);
    }

    @Test
    public void testObtenerRelaciones() {
        final Persona persona = new Persona(); // Debe rellenarse según la implementación de Persona
        final List<Relacion> relacionesEsperadas = new ArrayList<>(); // Se puede rellenar con relaciones de prueba
        when(relacionRepository.findByPersona1(persona)).thenReturn(relacionesEsperadas);

        final List<Relacion> relacionesObtenidas = relacionService.obtenerRelaciones(persona);

        assertEquals(relacionesEsperadas, relacionesObtenidas);
        verify(relacionRepository, times(1)).findByPersona1(persona);
    }
}
