package com.reba.rebatest.services;

import com.reba.rebatest.model.Persona;
import com.reba.rebatest.model.Relacion;
import com.reba.rebatest.repository.RelacionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio que gestiona las operaciones de la entidad Relacion.
 */
@Service
public class RelacionService {

    private final RelacionRepository relacionRepository;

    /**
     * Constructor de la clase RelacionService.
     *
     * @param relacionRepository repositorio de relaciones
     */
    public RelacionService(final RelacionRepository relacionRepository) {
        this.relacionRepository = relacionRepository;
    }

    /**
     * Obtiene una relación entre dos personas.
     *
     * @param persona1 la primera persona en la relación
     * @param persona2 la segunda persona en la relación
     * @return una entidad Relacion si existe una relación entre las dos personas, o null si no existe
     */
    public Relacion obtenerRelacion(final Persona persona1, final Persona persona2) {
        return relacionRepository.findByPersona1AndPersona2OrPersona2AndPersona1(persona1, persona2, persona2,
                persona1).orElse(null);
    }

    /**
     * Crea una nueva relación entre dos personas.
     *
     * @param persona1     la primera persona en la nueva relación
     * @param persona2     la segunda persona en la nueva relación
     * @param tipoRelacion el tipo de la relación (por ejemplo, AMIGOS)
     * @return la nueva entidad Relacion
     */
    public Relacion crearRelacion(final Persona persona1, final Persona persona2,
                                  final Relacion.TipoRelacion tipoRelacion) {
        final Relacion relacion = new Relacion();
        relacion.setPersona1(persona1);
        relacion.setPersona2(persona2);
        relacion.setTipoRelacion(tipoRelacion);
        return relacionRepository.save(relacion);
    }

    /**
     * Elimina todas las relaciones en las que una persona específica está involucrada.
     *
     * @param persona la persona cuyas relaciones se quieren eliminar
     */
    public void eliminarRelaciones(final Persona persona) {
        relacionRepository.deleteAllByPersona1OrPersona2(persona, persona);
    }

    /**
     * Obtiene todas las relaciones en las que una persona específica es la primera persona.
     *
     * @param persona la persona cuyas relaciones se quieren obtener
     * @return una lista de entidades Relacion donde la persona especificada es la primera persona en la relación
     */
    public List<Relacion> obtenerRelaciones(final Persona persona) {
        return relacionRepository.findByPersona1(persona);
    }
}
