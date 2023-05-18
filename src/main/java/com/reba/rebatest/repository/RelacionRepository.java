package com.reba.rebatest.repository;

import com.reba.rebatest.model.Persona;
import com.reba.rebatest.model.Relacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RelacionRepository extends JpaRepository<Relacion, Long> {
    Optional<Relacion> findByPersona1AndPersona2OrPersona2AndPersona1(Persona persona1, Persona persona2,
                                                                      Persona persona1B, Persona persona2B);

}
