package com.reba.rebatest.services;

import com.reba.rebatest.model.Persona;
import com.reba.rebatest.model.Relacion;
import com.reba.rebatest.repository.RelacionRepository;
import org.springframework.stereotype.Service;

@Service
public class RelacionService {

    private final RelacionRepository relacionRepository;

    public RelacionService(final RelacionRepository relacionRepository) {
        this.relacionRepository = relacionRepository;
    }

    public Relacion obtenerRelacion(final Persona persona1, final Persona persona2) {
        return relacionRepository.findByPersona1AndPersona2OrPersona2AndPersona1(persona1, persona2, persona2,
                persona1).orElseGet(null);
    }

    public Relacion crearRelacion(final Persona persona1, final Persona persona2,
                                  final Relacion.TipoRelacion tipoRelacion) {
        final Relacion relacion = new Relacion();
        relacion.setPersona1(persona1);
        relacion.setPersona2(persona2);
        relacion.setTipoRelacion(tipoRelacion);
        return relacionRepository.save(relacion);
    }
}
