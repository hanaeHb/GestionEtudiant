package com.example.gestionfaculteetudiant;

import com.example.gestionfaculteetudiant.configuration.RsaKeys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeys.class)
public class GestionFaculteEtudiantApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestionFaculteEtudiantApplication.class, args);
    }

}
