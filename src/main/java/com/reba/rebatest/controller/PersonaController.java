package com.reba.rebatest.controller;

import com.reba.rebatest.model.Persona;
import com.reba.rebatest.services.PersonaService;
import com.reba.rebatest.services.PersonasStats;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/personas")
public class PersonaController {

    private final PersonaService personaService;

    public PersonaController(final PersonaService personaService) {
        this.personaService = personaService;
    }

    @GetMapping
    public List<Persona> getAllPersonas() {
        return personaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Persona> getPersonaById(@PathVariable("id") final Long id) {
        final Optional<Persona> optionalPersona = personaService.findById(id);
        return optionalPersona.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createPersona(@RequestBody final Persona persona) {
        final Persona nuevaPersona = personaService.save(persona);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaPersona);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePersona(@PathVariable("id") final Long id, @RequestBody final Persona persona) {
        final Persona personaActualizada = personaService.update(id, persona);
        if (personaActualizada != null) {
            return ResponseEntity.ok(personaActualizada);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePersona(@PathVariable("id") final Long id) {
        final Optional<Persona> optionalPersona = personaService.findById(id);

        if (optionalPersona.isPresent()) {
            personaService.delete(optionalPersona.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/{idPadre}/padre/{idHijo}")
    public ResponseEntity<String> establecerPadre(@PathVariable("idPadre") final Long idPadre,
                                                  @PathVariable("idHijo") final Long idHijo) {
        final Optional<Persona> padre = personaService.findById(idPadre);
        final Optional<Persona> hijo = personaService.findById(idHijo);

        if (padre.isEmpty() || hijo.isEmpty()) {
            return ResponseEntity.badRequest().body("Los IDs proporcionados no corresponden a personas existentes");
        }

        hijo.get().setPadre(padre.get());
        personaService.save(hijo.get());

        return ResponseEntity.ok("Se ha establecido a " + idPadre + " como padre de " + idHijo);
    }

    @GetMapping("/stats")
    public ResponseEntity<List<PersonasStats>> getStats() {
        final List<PersonasStats> stats = personaService.getPercentageByCountry();
        return ResponseEntity.ok(stats);
    }
}
