package com.reba.rebatest.configuration;

import com.reba.rebatest.model.*;
import com.reba.rebatest.repository.PaisRepository;
import com.reba.rebatest.repository.PersonaRepository;
import com.reba.rebatest.repository.RelacionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    private final PaisRepository paisRepository;
    private final PersonaRepository personaRepository;
    private final RelacionRepository relacionRepository;

    public DataInitializer(final PaisRepository paisRepository, final PersonaRepository personaRepository,
                           final RelacionRepository relacionRepository) {
        this.paisRepository = paisRepository;
        this.personaRepository = personaRepository;
        this.relacionRepository = relacionRepository;
    }

    @Override
    public void run(final String... args) throws Exception {
        cargarPaisesDeEjemplo();
    }

    private void cargarPaisesDeEjemplo() {
        // Creación de personas de ejemplo
        final Persona persona1 = new Persona("Juan Pérez", LocalDate.of(1990, 1, 1), "Argentina", "123456789",
                TipoDocumento.DNI, null, null);
        final Persona persona2 = new Persona("María Rodríguez", LocalDate.of(1995, 5, 10), "Uruguay", "987654321",
                TipoDocumento.PASAPORTE, null, null);
        final Persona persona3 = new Persona("Diego González", LocalDate.of(1985, 3, 15), "Chile", "456789123",
                TipoDocumento.CI, null, null);
        final Persona persona4 = new Persona("Camila Fernández", LocalDate.of(1992, 9, 20), "Brasil", "789123456",
                TipoDocumento.LC, null, null);
        final Persona persona5 = new Persona("Lucas Silva", LocalDate.of(1988, 6, 25), "Argentina", "321654987",
                TipoDocumento.LE, null, null);
        final Persona persona6 = new Persona("Valentina Lima", LocalDate.of(1997, 11, 30), "Uruguay", "654789321",
                TipoDocumento.DNI, null, null);

        // Creación de datos de contacto de ejemplo
        final DatosContacto datosContacto1 = new DatosContacto("Calle 123", "123456789", "juan.perez@example.com");
        final DatosContacto datosContacto2 = new DatosContacto("Avenida Uruguay", "987654321", "maria.rodriguez" +
                "@example.com");
        final DatosContacto datosContacto3 = new DatosContacto("Calle Santiago", "456789123", "diego.gonzalez@example" +
                ".com");
        final DatosContacto datosContacto4 = new DatosContacto("Rua Brasil", "789123456", "camila.fernandez@example" +
                ".com");
        final DatosContacto datosContacto5 = new DatosContacto("Avenida Buenos Aires", "321654987", "lucas" +
                ".silva@example" +
                ".com");
        final DatosContacto datosContacto6 = new DatosContacto("Calle Montevideo", "654789321", "valentina" +
                ".lima@example.com");

        persona1.setDatosContacto(datosContacto1);
        persona2.setDatosContacto(datosContacto2);
        persona3.setDatosContacto(datosContacto3);
        persona4.setDatosContacto(datosContacto4);
        persona5.setDatosContacto(datosContacto5);
        persona6.setDatosContacto(datosContacto6);

        // Creación de países de ejemplo
        final Pais pais1 = new Pais("Argentina", "AR");
        final Pais pais2 = new Pais("Uruguay", "UY");
        final Pais pais3 = new Pais("Chile", "CL");
        final Pais pais4 = new Pais("Brasil", "BR");

        // Asignación de países a las personas
        persona1.setPais(pais1);
        persona2.setPais(pais2);
        persona3.setPais(pais3);
        persona4.setPais(pais4);
        persona5.setPais(pais1);
        persona6.setPais(pais2);

        // Creación de relaciones de ejemplo
        final Relacion relacion1 = new Relacion(null, persona1, persona2, Relacion.TipoRelacion.HERMANX);
        final Relacion relacion2 = new Relacion(null, persona3, persona4, Relacion.TipoRelacion.PRIMX);
        final Relacion relacion3 = new Relacion(null, persona5, persona6, Relacion.TipoRelacion.TIX);

        persona1.setMapadre(persona4);
        // Persistir los datos en la base de datos
        paisRepository.saveAll(Arrays.asList(pais1, pais2, pais3, pais4));
        personaRepository.saveAll(Arrays.asList(persona1, persona2, persona3, persona4, persona5, persona6));
        relacionRepository.saveAll(Arrays.asList(relacion1, relacion2, relacion3));

    }
}

