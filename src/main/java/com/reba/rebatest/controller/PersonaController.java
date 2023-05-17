package com.reba.rebatest.controller;

import com.reba.rebatest.model.Persona;
import com.reba.rebatest.model.PersonaId;
import com.reba.rebatest.model.TipoDocumento;
import com.reba.rebatest.repository.PaisRepository;
import com.reba.rebatest.repository.PersonaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/personas")
public class PersonaController {

    private final PersonaRepository personaRepository;
    private final PaisRepository paisRepository;

    public PersonaController(PersonaRepository personaRepository, PaisRepository paisRepository) {
        this.personaRepository = personaRepository;
        this.paisRepository = paisRepository;
    }

    @GetMapping
    public List<Persona> getAllPersonas() {
        return personaRepository.findAll();
    }

    @GetMapping("/{numeroDocumento}/{tipoDocumento}/{paisId}")
    public ResponseEntity<Persona> getPersonaById(
            @PathVariable("numeroDocumento") String numeroDocumento,
            @PathVariable("tipoDocumento") TipoDocumento tipoDocumento,
            @PathVariable("paisId") Long paisId) {

        Optional<Persona> optionalPersona = personaRepository.findById(
                new PersonaId(numeroDocumento, tipoDocumento, paisRepository.findById(paisId).orElse(null)));

        return optionalPersona.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createPersona(@RequestBody Persona persona) {
        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaNacimiento = persona.getFechaNacimiento();

        if (fechaNacimiento.plusYears(18).isAfter(fechaActual)) {
            String mensajeError = "La persona debe tener al menos 18 años para ser creada.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensajeError);
        }

        Persona nuevaPersona = personaRepository.save(persona);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaPersona);
    }


    @PutMapping("/{numeroDocumento}/{tipoDocumento}/{paisId}")
    public ResponseEntity<?> updatePersona(
            @PathVariable("numeroDocumento") String numeroDocumento,
            @PathVariable("tipoDocumento") TipoDocumento tipoDocumento,
            @PathVariable("paisId") Long paisId,
            @RequestBody Persona persona) {

        Optional<Persona> optionalPersona = personaRepository.findById(
                new PersonaId(numeroDocumento, tipoDocumento, paisRepository.findById(paisId).orElse(null)));

        if (optionalPersona.isPresent()) {
            Persona personaExistente = optionalPersona.get();

            LocalDate fechaActual = LocalDate.now();
            LocalDate fechaNacimiento = persona.getFechaNacimiento();

            if (fechaNacimiento.plusYears(18).isAfter(fechaActual)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            personaExistente.setNombre(persona.getNombre());
            personaExistente.setFechaNacimiento(persona.getFechaNacimiento());
            personaExistente.setNacionalidad(persona.getNacionalidad());
            personaExistente.setDatosContacto(persona.getDatosContacto());

            Persona personaActualizada = personaRepository.save(personaExistente);
            return ResponseEntity.ok(personaActualizada);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{numeroDocumento}/{tipoDocumento}/{paisId}")
    public ResponseEntity<?> deletePersona(
            @PathVariable("numeroDocumento") String numeroDocumento,
            @PathVariable("tipoDocumento") TipoDocumento tipoDocumento,
            @PathVariable("paisId") Long paisId) {

        Optional<Persona> optionalPersona = personaRepository.findById(
                new PersonaId(numeroDocumento, tipoDocumento, paisRepository.findById(paisId).orElse(null)));

        if (optionalPersona.isPresent()) {
            personaRepository.delete(optionalPersona.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
