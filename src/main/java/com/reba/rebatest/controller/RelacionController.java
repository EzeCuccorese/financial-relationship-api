package com.reba.rebatest.controller;

import com.reba.rebatest.model.Persona;
import com.reba.rebatest.model.Relacion;
import com.reba.rebatest.services.PersonaService;
import com.reba.rebatest.services.RelacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/relaciones")
public class RelacionController {

    private final RelacionService relacionService;
    private final PersonaService personaService;

    public RelacionController(final RelacionService relacionService, final PersonaService personaService) {
        this.relacionService = relacionService;
        this.personaService = personaService;
    }

    @GetMapping("/{id1}/{id2}")
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
