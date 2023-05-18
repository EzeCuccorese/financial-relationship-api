package com.reba.rebatest.controller;

import com.reba.rebatest.model.Persona;
import com.reba.rebatest.model.Relacion;
import com.reba.rebatest.services.PersonaService;
import com.reba.rebatest.services.RelacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RelacionControllerTest {

    @Mock
    private RelacionService relacionService;

    @Mock
    private PersonaService personaService;

    @InjectMocks
    private RelacionController relacionController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(relacionController).build();
    }

    @Test
    public void testObtenerRelacion() throws Exception {
        final Persona persona1 = new Persona();
        persona1.setId(1L);

        final Persona persona2 = new Persona();
        persona2.setId(2L);

        final Relacion relacion = new Relacion();
        relacion.setTipoRelacion(Relacion.TipoRelacion.HERMANX);

        given(personaService.findById(1L)).willReturn(Optional.of(persona1));
        given(personaService.findById(2L)).willReturn(Optional.of(persona2));
        given(relacionService.obtenerRelacion(persona1, persona2)).willReturn(relacion);

        mockMvc.perform(get("/relaciones/1/2"))
                .andExpect(status().isOk())
                .andExpect(content().string(Relacion.TipoRelacion.HERMANX.getDescripcion()));
    }

    @Test
    public void testObtenerRelacion_NotFound() throws Exception {
        final Persona persona1 = new Persona();
        persona1.setId(1L);

        final Persona persona2 = new Persona();
        persona2.setId(2L);

        given(personaService.findById(1L)).willReturn(Optional.of(persona1));
        given(personaService.findById(2L)).willReturn(Optional.of(persona2));
        given(relacionService.obtenerRelacion(persona1, persona2)).willReturn(null);

        mockMvc.perform(get("/relaciones/1/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testObtenerRelacion_BadRequest() throws Exception {
        given(personaService.findById(1L)).willReturn(Optional.empty());

        mockMvc.perform(get("/relaciones/1/2"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Los IDs proporcionados no corresponden a personas existentes"));
    }
}
