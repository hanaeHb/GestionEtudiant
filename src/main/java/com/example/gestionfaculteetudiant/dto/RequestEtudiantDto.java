package com.example.gestionfaculteetudiant.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @NoArgsConstructor @AllArgsConstructor
public class RequestEtudiantDto {
    private String nom;
    private String prenom;
    private String cne;
    private Integer filiere_id;
}
