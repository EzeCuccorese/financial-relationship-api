package com.reba.rebatest.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reba.rebatest.model.Person;
import com.reba.rebatest.services.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Test
    void whenRuntimeExceptionOccurs_thenGlobalExceptionHandlerIsInvoked() throws Exception {

        final Person person = new Person();
        given(personService.save(person)).willThrow(new RuntimeException("Problemitas"));

        mockMvc.perform(post("/api/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(person)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(MockMvcResultMatchers.content().string("Ocurrió un error. Problemitas"));
    }
}
