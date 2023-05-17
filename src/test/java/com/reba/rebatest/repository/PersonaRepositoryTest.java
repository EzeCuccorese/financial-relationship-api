package com.reba.rebatest.repository;

import com.reba.rebatest.model.*;
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

    private final Pais pais = new Pais("Argentina");
    @Autowired
    private PersonaRepository personaRepository;
    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    public void setup() {
        entityManager.persist(pais);
        entityManager.flush();

        DatosContacto datosContacto = new DatosContacto("123456", "mail@mail.com");
        entityManager.persist(datosContacto);
        entityManager.flush();

        PersonaId personaId = new PersonaId("12345678", TipoDocumento.DNI, pais);
        Persona persona = new Persona(personaId, "John Doe", LocalDate.of(1990, 5, 10), "Argentino", datosContacto);
        entityManager.persist(persona);
        entityManager.flush();
    }

    @Test
    public void testCreate() {
        // Crear una nueva persona
        Pais pais = new Pais();
        pais.setNombre("Estados Unidos");
        entityManager.persist(pais);
        entityManager.flush();

        DatosContacto datosContacto = new DatosContacto();
        datosContacto.setTelefono("987654321");
        datosContacto.setEmail("new@example.com");
        entityManager.persist(datosContacto);
        entityManager.flush();

        PersonaId personaId = new PersonaId("87654321", TipoDocumento.DNI, pais);
        Persona persona = new Persona(personaId, "Jane Smith", LocalDate.of(1995, 8, 15), "Estadounidense",
                datosContacto);
        personaRepository.save(persona);

        // Verificar que la persona se haya guardado correctamente
        Persona personaGuardada = entityManager.find(Persona.class, persona.getId());
        Assertions.assertEquals("Jane Smith", personaGuardada.getNombre());
        Assertions.assertEquals(LocalDate.of(1995, 8, 15), personaGuardada.getFechaNacimiento());
        Assertions.assertEquals("Estadounidense", personaGuardada.getNacionalidad());
    }

    @Test
    public void testRead() {
        // Leer la persona existente
        Persona persona = personaRepository.findById(new PersonaId("12345678", TipoDocumento.DNI, pais)).orElse(null);
        Assertions.assertNotNull(persona);
        Assertions.assertEquals("John Doe", persona.getNombre());
        Assertions.assertEquals(LocalDate.of(1990, 5, 10), persona.getFechaNacimiento());
        Assertions.assertEquals("Argentino", persona.getNacionalidad());
    }

    @Test
    public void testUpdate() {
        // Actualizar el nombre de la persona existente
        Persona persona = personaRepository.findById(new PersonaId("12345678", TipoDocumento.DNI, pais)).orElse(null);
        Assertions.assertNotNull(persona);
        persona.setNombre("Updated Name");
        personaRepository.save(persona);

        // Verificar que la persona se haya actualizado correctamente
        Persona personaActualizada = entityManager.find(Persona.class, persona.getId());
        Assertions.assertEquals("Updated Name", personaActualizada.getNombre());
    }

    @Test
    public void testDelete() {
        // Eliminar la persona existente
        Persona persona = personaRepository.findById(new PersonaId("12345678", TipoDocumento.DNI, pais)).orElse(null);
        Assertions.assertNotNull(persona);
        personaRepository.delete(persona);

        // Verificar que la persona se haya eliminado correctamente
        Persona personaEliminada = entityManager.find(Persona.class, persona.getId());
        Assertions.assertNull(personaEliminada);
    }
}
