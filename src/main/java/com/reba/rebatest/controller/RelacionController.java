package com.reba.rebatest.controller;

import com.reba.rebatest.model.Persona;
import com.reba.rebatest.model.Relacion;
import com.reba.rebatest.services.PersonaService;
import com.reba.rebatest.services.RelacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/relaciones")
@Tag(name = "Relaciones", description = "API para gestionar relaciones entre personas")
public class RelacionController {

    private final RelacionService relacionService;
    private final PersonaService personaService;

    public RelacionController(final RelacionService relacionService, final PersonaService personaService) {
        this.relacionService = relacionService;
        this.personaService = personaService;
    }

    @GetMapping("/{id1}/{id2}")
    @Operation(summary = "Obtener relación entre dos personas por sus IDs", responses = {
            @ApiResponse(description = "Relación encontrada", content = @Content(mediaType = "text/plain")),
            @ApiResponse(description = "IDs de personas no válidos", responseCode = "400"),
            @ApiResponse(description = "Relación no encontrada", responseCode = "404")
    })
    public ResponseEntity<String> obtenerRelacion(@PathVariable("id1") final Long id1,
                                                  @PathVariable("id2") final Long id2) {
        final Optional<Persona> persona1 = personaService.findById(id1);
        final Optional<Persona> persona2 = personaService.findById(id2);

        if (persona1.isEmpty() || persona2.isEmpty()) {
            return ResponseEntity.badRequest().body("Los IDs proporcionados no corresponden a personas existentes");
        }

        final Relacion relacion = relacionService.obtenerRelacion(persona1.get(), persona2.get());

        if (relacion == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(relacion.getTipoRelacion().getDescripcion());
    }
}
