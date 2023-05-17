package com.reba.rebatest.configuration;

import com.reba.rebatest.model.Pais;
import com.reba.rebatest.repository.PaisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final PaisRepository paisRepository;

    @Autowired
    public DataInitializer(PaisRepository paisRepository) {
        this.paisRepository = paisRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        cargarPaisesDeEjemplo();
    }

    private void cargarPaisesDeEjemplo() {
        Pais pais1 = new Pais(1L, "Argentina");
        Pais pais2 = new Pais(2L, "Brasil");
        Pais pais3 = new Pais(3L, "Chile");

        paisRepository.save(pais1);
        paisRepository.save(pais2);
        paisRepository.save(pais3);
    }
}

