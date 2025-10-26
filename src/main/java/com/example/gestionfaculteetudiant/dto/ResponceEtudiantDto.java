package com.example.gestionfaculteetudiant.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @NoArgsConstructor @AllArgsConstructor
public class ResponceEtudiantDto {
    private Integer id;
    private String nom;
    private String prenom;
    private String cne;
    private String filiereCode;
    private String filiereLibelle;
}
