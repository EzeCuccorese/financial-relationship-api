package com.reba.rebatest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reba.rebatest.model.Persona;
import com.reba.rebatest.services.PersonaService;
import com.reba.rebatest.services.PersonasStats;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonaService personaService;

    private Persona persona;

    @BeforeEach
    public void setUp() {
        persona = new Persona();
        persona.setId(1L);
        persona.setNombre("Juan");
    }

    @Test
    public void testGetAllPersonas() throws Exception {
        given(personaService.findAll()).willReturn(Collections.singletonList(persona));

        mockMvc.perform(get("/api/personas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].nombre").value("Juan"));

    }

    @Test
    public void testGetPersonaById() throws Exception {
        given(personaService.findById(1L)).willReturn(Optional.of(persona));

        mockMvc.perform(get("/api/personas/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value("Juan"));

    }

    @Test
    public void testCreatePersona() throws Exception {
        given(personaService.save(persona)).willReturn(persona);

        mockMvc.perform(post("/api/personas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(persona)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value("Juan"));

    }

    @Test
    public void testUpdatePersona() throws Exception {
        given(personaService.update(1L, persona)).willReturn(persona);

        mockMvc.perform(put("/api/personas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(persona)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value("Juan"));

    }

    @Test
    public void testDeletePersona() throws Exception {
        given(personaService.findById(1L)).willReturn(Optional.of(persona));
        doNothing().when(personaService).delete(persona);

        mockMvc.perform(delete("/api/personas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testEstablecerPadre() throws Exception {
        final Persona hijo = new Persona();
        hijo.setId(2L);
        hijo.setNombre("Pedro");

        given(personaService.findById(1L)).willReturn(Optional.of(persona));
        given(personaService.findById(2L)).willReturn(Optional.of(hijo));
        given(personaService.save(hijo)).willReturn(hijo);

        mockMvc.perform(post("/api/personas/1/padre/2"))
                .andExpect(status().isOk())
                .andExpect(content().string("Se ha establecido a 1 como padre de 2"));
    }

    @Test
    public void getStatsTest() {
        // Given
        final PersonaService personaService = Mockito.mock(PersonaService.class);
        final PersonaController personaController = new PersonaController(personaService);

        final PersonasStats argentinaStats = new PersonasStats("Argentina", 66.67);
        final PersonasStats brasilStats = new PersonasStats("Brasil", 33.33);

        final List<PersonasStats> stats = Arrays.asList(argentinaStats, brasilStats);

        when(personaService.getPercentageByCountry()).thenReturn(stats);

        // When
        final ResponseEntity<List<PersonasStats>> result = personaController.getStats();

        // Then
        Assertions.assertEquals(200, result.getStatusCodeValue());
        Assertions.assertEquals(2, Objects.requireNonNull(result.getBody()).size());
        Assertions.assertEquals("Argentina", result.getBody().get(0).getCountry());
        Assertions.assertEquals(66.67, result.getBody().get(0).getPercentage(), 0.01);
        Assertions.assertEquals("Brasil", result.getBody().get(1).getCountry());
        Assertions.assertEquals(33.33, result.getBody().get(1).getPercentage(), 0.01);
    }
}
