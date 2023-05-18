package com.reba.rebatest.controller;

import com.reba.rebatest.model.Persona;
import com.reba.rebatest.services.PersonaService;
import com.reba.rebatest.model.PersonasStats;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/personas")
@Tag(name = "Personas", description = "API para gestionar personas")
public class PersonaController {

    private final PersonaService personaService;

    public PersonaController(final PersonaService personaService) {
        this.personaService = personaService;
    }

    @GetMapping
    @Operation(summary = "Obtener todas las personas", responses = {
            @ApiResponse(description = "Lista de personas", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Persona.class)))
    })
    public List<Persona> getAllPersonas() {
        return personaService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una persona por su ID", responses = {
            @ApiResponse(description = "Persona encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Persona.class))),
            @ApiResponse(description = "Persona no encontrada", responseCode = "404")
    })
    public ResponseEntity<Persona> getPersonaById(@PathVariable("id") final Long id) {
        final Optional<Persona> optionalPersona = personaService.findById(id);
        return optionalPersona.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear una nueva persona", responses = {
            @ApiResponse(description = "Persona creada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Persona.class)))
    })
    public ResponseEntity<?> createPersona(@RequestBody final Persona persona) {
        final Persona nuevaPersona = personaService.save(persona);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaPersona);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una persona existente por su ID", responses = {
            @ApiResponse(description = "Persona actualizada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Persona.class))),
            @ApiResponse(description = "Persona no encontrada", responseCode = "404")
    })
    public ResponseEntity<?> updatePersona(@PathVariable("id") final Long id, @RequestBody final Persona persona) {
        final Persona personaActualizada = personaService.update(id, persona);
        if (personaActualizada != null) {
            return ResponseEntity.ok(personaActualizada);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una persona por su ID", responses = {
            @ApiResponse(description = "Persona eliminada", responseCode = "204"),
            @ApiResponse(description = "Persona no encontrada", responseCode = "404")
    })
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
    @Operation(summary = "Establecer relación de padre entre dos personas", responses = {
            @ApiResponse(description = "Relación establecida", content = @Content(mediaType = "text/plain")),
            @ApiResponse(description = "IDs de personas no válidos", responseCode = "400")
    })
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
    @Operation(summary = "Obtener estadísticas de personas por país", responses = {
            @ApiResponse(description = "Estadísticas de personas por país", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonasStats.class)))
    })
    public ResponseEntity<List<PersonasStats>> getStats() {
        final List<PersonasStats> stats = personaService.getPercentageByCountry();
        return ResponseEntity.ok(stats);
    }
}
