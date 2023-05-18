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
    public DataInitializer(final PaisRepository paisRepository) {
        this.paisRepository = paisRepository;
    }

    @Override
    public void run(final String... args) throws Exception {
        cargarPaisesDeEjemplo();
    }

    private void cargarPaisesDeEjemplo() {
        final Pais pais1 = new Pais("Argentina", "ARG");
        final Pais pais2 = new Pais("Brasil", "BRA");
        final Pais pais3 = new Pais("Chile", "CHI");

        paisRepository.save(pais1);
        paisRepository.save(pais2);
        paisRepository.save(pais3);
    }
}

