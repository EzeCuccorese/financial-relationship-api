package com.reba.rebatest.repository;

import com.reba.rebatest.model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {
}

