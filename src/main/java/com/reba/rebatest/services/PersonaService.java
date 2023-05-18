package com.reba.rebatest.services;

import com.reba.rebatest.exceptions.DatoContactoInexistenteException;
import com.reba.rebatest.exceptions.EdadIncorectaException;
import com.reba.rebatest.model.DatosContacto;
import com.reba.rebatest.model.Persona;
import com.reba.rebatest.model.PersonasStats;
import com.reba.rebatest.repository.PersonaRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar operaciones relacionadas con la entidad Persona.
 */
@Service
public class PersonaService {

    private final PersonaRepository personaRepository;

    /**
     * Constructor que inyecta el repositorio de Personas {@link PersonaRepository}.
     *
     * @param personaRepository el repositorio de Personas.
     */
    public PersonaService(final PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    /**
     * Encuentra una {@link Persona} por su ID.
     *
     * @param id el ID de la {@link Persona}.
     * @return la {@link Persona} si se encuentra, o un Optional vacío si no.
     */
    public Optional<Persona> findById(final Long id) {
        return personaRepository.findById(id);
    }

    /**
     * Guarda una nueva {@link Persona}.
     *
     * @param persona la {@link Persona} a guardar.
     * @return la {@link Persona} guardada.
     */
    public Persona save(final Persona persona) {
        validarEdad(persona);
        validarDatoContacto(persona.getDatosContacto());
        return personaRepository.save(persona);
    }


    /**
     * Obtiene todas las {@link Persona}.
     *
     * @return una lista de todas las {@link Persona}.
     */
    public List<Persona> findAll() {
        return personaRepository.findAll();
    }

    /**
     * Elimina una {@link Persona}.
     *
     * @param persona la {@link Persona} a eliminar.
     */
    public void delete(final Persona persona) {
        personaRepository.delete(persona);
    }

    /**
     * Actualiza una {@link Persona} existente.
     *
     * @param idPersona el ID de la {@link Persona} a actualizar.
     * @param persona   la {@link Persona} con los nuevos datos.
     * @return la {@link Persona} actualizada, o null si no se encuentra la {@link Persona}.
     */
    public Persona update(final Long idPersona, final Persona persona) {
        validarEdad(persona);
        validarDatoContacto(persona.getDatosContacto());
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

    /**
     * Valida que una {@link Persona} tenga al menos 18 años.
     *
     * @param persona la {@link Persona} a validar.
     * @throws EdadIncorectaException si la {@link Persona} tiene menos de 18 años.
     */
    private static void validarEdad(final Persona persona) {
        final LocalDate fechaActual = LocalDate.now();
        final LocalDate fechaNacimiento = persona.getFechaNacimiento();

        if (fechaNacimiento.plusYears(18).isAfter(fechaActual)) {
            throw new EdadIncorectaException();
        }
    }

    /**
     * Valida los datos de contacto.
     *
     * @param datosContacto los datos de contacto a validar
     * @throws DatoContactoInexistenteException si todos los atributos requeridos son null o estan en blanco
     */
    private static void validarDatoContacto(final DatosContacto datosContacto) {
        if (datosContacto == null ||
                StringUtils.isBlank(datosContacto.getDireccion())
                        && StringUtils.isBlank(datosContacto.getEmail())
                        && StringUtils.isBlank(datosContacto.getTelefono())) {
            throw new DatoContactoInexistenteException();
        }
    }


    /**
     * Obtiene todas las personas, las agrupa por nacionalidad y calcula el porcentaje de personas de cada
     * nacionalidad.
     *
     * @return la agrupacin de las personas por porcentajes.
     */
    public List<PersonasStats> getPercentageByCountry() {
        final List<Persona> allPersonas = this.findAll();
        final Map<String, Long> countByCountry = allPersonas.stream()
                .collect(Collectors.groupingBy(Persona::getNacionalidad, Collectors.counting()));
        final long total = allPersonas.size();
        final List<PersonasStats> porcentajePorPais = new ArrayList<>();
        for (final Map.Entry<String, Long> entry : countByCountry.entrySet()) {
            porcentajePorPais.add(new PersonasStats(entry.getKey(), (double) entry.getValue() / total * 100));
        }
        return porcentajePorPais;
    }
}
