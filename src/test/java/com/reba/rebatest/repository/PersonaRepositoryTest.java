package com.reba.rebatest.repository;

import com.reba.rebatest.model.DatosContacto;
import com.reba.rebatest.model.Pais;
import com.reba.rebatest.model.Persona;
import com.reba.rebatest.model.TipoDocumento;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

@DataJpaTest
@ActiveProfiles("test")
public class PersonaRepositoryTest {


    @Autowired
    private PersonaRepository personaRepository;
    @Autowired
    private TestEntityManager entityManager;

    private Long personaId;

    @BeforeEach
    public void setup() {
        final Pais pais = new Pais("Argentina");
        entityManager.persist(pais);
        entityManager.flush();

        final DatosContacto datosContacto = new DatosContacto("123456", "mail@mail.com");
        entityManager.persist(datosContacto);
        entityManager.flush();

        Persona persona = new Persona("Vanesa", LocalDate.of(1987, 8, 25),
                "Argentina", "33554855",
                TipoDocumento.DNI, pais, datosContacto);
        persona = entityManager.persist(persona);
        entityManager.flush();

        personaId = persona.getId();
    }

    @Test
    public void testCreatePersona() {
        //Given
        final Pais pais = new Pais();
        pais.setNombre("Estados Unidos");
        entityManager.persist(pais);
        entityManager.flush();

        final DatosContacto datosContacto = new DatosContacto();
        datosContacto.setTelefono("1155667788");
        datosContacto.setEmail("cuccoed@outlook.com");
        entityManager.persist(datosContacto);
        entityManager.flush();

        final Persona persona = new Persona("Ezequiel", LocalDate.of(1985, 8, 30),
                "Argentina", "31898856",
                TipoDocumento.DNI, pais, datosContacto);

        //When
        personaRepository.save(persona);

        // Then
        final Persona personaGuardada = entityManager.find(Persona.class, persona.getId());
        Assertions.assertEquals("Ezequiel", personaGuardada.getNombre());
        Assertions.assertEquals(LocalDate.of(1985, 8, 30), personaGuardada.getFechaNacimiento());
        Assertions.assertEquals("Estadounidense", personaGuardada.getNacionalidad());
    }

    @Test
    public void testRead() {
        //Given and When
        final Persona persona = personaRepository.findById(personaId).orElse(null);

        //Then
        Assertions.assertNotNull(persona);
        Assertions.assertEquals("John Doe", persona.getNombre());
        Assertions.assertEquals(LocalDate.of(1990, 5, 10), persona.getFechaNacimiento());
        Assertions.assertEquals("Argentino", persona.getNacionalidad());
    }

    @Test
    public void testUpdate() {
        //Given
        final Persona persona = personaRepository.findById(personaId).orElse(null);
        Assertions.assertNotNull(persona);
        persona.setNombre("Updated Name");

        //When
        personaRepository.save(persona);

        //Then
        final Persona personaActualizada = entityManager.find(Persona.class, persona.getId());
        Assertions.assertEquals("Updated Name", personaActualizada.getNombre());
    }

    @Test
    public void testDelete() {
        //Given
        final Persona persona = personaRepository.findById(personaId).orElse(null);
        Assertions.assertNotNull(persona);

        //When
        personaRepository.delete(persona);

        // Then
        final Persona personaEliminada = entityManager.find(Persona.class, persona.getId());
        Assertions.assertNull(personaEliminada);
    }
}
