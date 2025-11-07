package com.example.gestionfaculteetudiant.mapper;

import com.example.gestionfaculteetudiant.dto.RequestEtudiantDto;
import com.example.gestionfaculteetudiant.dto.ResponceEtudiantDto;
import com.example.gestionfaculteetudiant.entity.Etudiant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class EtudiantMapper {

    private final RestTemplate restTemplate;

    public Etudiant DTO_TO_ENTITY(RequestEtudiantDto requestEtudiantDto) {
        Etudiant etudiant = new Etudiant();
        etudiant.setNom(requestEtudiantDto.getNom());
        etudiant.setPrenom(requestEtudiantDto.getPrenom());
        etudiant.setCne(requestEtudiantDto.getCne());
        etudiant.setFiliereId(requestEtudiantDto.getFiliere_id());
        return etudiant;
    }


    public ResponceEtudiantDto ENTITY_TO_DTO(Etudiant etudiant) {
        ResponceEtudiantDto dto = new ResponceEtudiantDto();
        dto.setId(etudiant.getId());
        dto.setNom(etudiant.getNom());
        dto.setPrenom(etudiant.getPrenom());
        dto.setCne(etudiant.getCne());

        if (etudiant.getFiliereId() != null) {
            try {

                FiliereResponse filiere = restTemplate.getForObject(
                        "http://localhost:8084/v1/filieres/" + etudiant.getFiliereId(),
                        FiliereResponse.class
                );
                if (filiere != null) {
                    dto.setFiliereCode(filiere.getCode());
                    dto.setFiliereLibelle(filiere.getLibelle());
                }
            } catch (Exception e) {
                dto.setFiliereCode("Erreur service");
                dto.setFiliereLibelle("Service filière indisponible");
            }
        }

        return dto;
    }

    static class FiliereResponse {
        private Integer id;
        private String code;
        private String libelle;

        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public String getLibelle() { return libelle; }
        public void setLibelle(String libelle) { this.libelle = libelle; }
    }
}