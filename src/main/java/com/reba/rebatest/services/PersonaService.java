package com.reba.rebatest.services;

import com.reba.rebatest.exceptions.EdadIncorectaException;
import com.reba.rebatest.model.Persona;
import com.reba.rebatest.repository.PersonaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PersonaService {

    private final PersonaRepository personaRepository;

    public PersonaService(final PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    public Optional<Persona> findById(final Long id) {
        return personaRepository.findById(id);
    }

    public Persona save(final Persona persona) {
        validarEdad(persona);
        return personaRepository.save(persona);
    }

    public List<Persona> findAll() {
        return personaRepository.findAll();
    }

    public void delete(final Persona persona) {
        personaRepository.delete(persona);
    }

    public Persona update(final Long idPersona, final Persona persona) {
        validarEdad(persona);
        final Optional<Persona> optionalPersona = personaRepository.findById(idPersona);
        if (optionalPersona.isPresent()) {
            final Persona personaExistente = optionalPersona.get();
            personaExistente.setNombre(persona.getNombre());
            personaExistente.setFechaNacimiento(persona.getFechaNacimiento());
            personaExistente.setNacionalidad(persona.getNacionalidad());
            personaExistente.setDatosContacto(persona.getDatosContacto());

            return personaRepository.save(personaExistente);
        }
        return null;
    }

    private static void validarEdad(final Persona persona) {
        final LocalDate fechaActual = LocalDate.now();
        final LocalDate fechaNacimiento = persona.getFechaNacimiento();

        if (fechaNacimiento.plusYears(18).isAfter(fechaActual)) {
            throw new EdadIncorectaException();
        }
    }
}

