package com.example.gestionfaculteetudiant.service;

import com.example.gestionfaculteetudiant.dto.RequestEtudiantDto;
import com.example.gestionfaculteetudiant.dto.ResponceEtudiantDto;
import com.example.gestionfaculteetudiant.entity.Etudiant;
import com.example.gestionfaculteetudiant.mapper.EtudiantMapper;
import com.example.gestionfaculteetudiant.repository.EtudiantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class EtudiantServiceImpl implements EtudiantService {

    private final EtudiantRepository etudiantRepository;
    private final EtudiantMapper etudiantMapper;
    private final RestTemplate restTemplate;

    @Autowired
    public EtudiantServiceImpl(EtudiantRepository etudiantRepository,
                               EtudiantMapper etudiantMapper,
                               RestTemplate restTemplate) {
        this.etudiantRepository = etudiantRepository;
        this.etudiantMapper = etudiantMapper;
        this.restTemplate = restTemplate;
    }

    @Override
    public ResponceEtudiantDto AddEtudiant(RequestEtudiantDto dto) {
        Etudiant etudiant = etudiantMapper.DTO_TO_ENTITY(dto);

        etudiant.setFiliereId(dto.getFiliere_id());

        Etudiant saved = etudiantRepository.save(etudiant);

        ResponceEtudiantDto responseDto = etudiantMapper.ENTITY_TO_DTO(saved);
        try {
            ResponseEntity<FiliereResponse> response = restTemplate.exchange(
                    "http://localhost:8084/v1/filieres/" + dto.getFiliere_id(),
                    HttpMethod.GET,
                    null,
                    FiliereResponse.class
            );
            FiliereResponse filiere = response.getBody();
            if (filiere != null) {
                responseDto.setFiliereCode(filiere.getCode());
                responseDto.setFiliereLibelle(filiere.getLibelle());
            }
        } catch (Exception e) {
            responseDto.setFiliereCode("Erreur service");
            responseDto.setFiliereLibelle("Service filière indisponible");
        }

        return responseDto;
    }



    @Override
    public List<ResponceEtudiantDto> GETALLEtudiants() {
        return etudiantRepository.findAll()
                .stream()
                .map(this::getEtudiantWithFiliere)
                .toList();
    }

    @Override
    public ResponceEtudiantDto GETEtudiantById(Integer id) {
        Etudiant etudiant = etudiantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Etudiant introuvable"));
        return getEtudiantWithFiliere(etudiant);
    }

    @Override
    public ResponceEtudiantDto UPDATEEtudiant(Integer id, RequestEtudiantDto dto) {
        Etudiant etudiant = etudiantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Etudiant introuvable"));

        if (dto.getNom() != null) etudiant.setNom(dto.getNom());
        if (dto.getPrenom() != null) etudiant.setPrenom(dto.getPrenom());
        if (dto.getCne() != null) etudiant.setCne(dto.getCne());
        if (dto.getFiliere_id() != null) etudiant.setFiliereId(dto.getFiliere_id());

        Etudiant saved = etudiantRepository.save(etudiant);
        return getEtudiantWithFiliere(saved);
    }

    @Override
    public void DELETEEtudiantBYID(Integer id) {
        etudiantRepository.deleteById(id);
    }

    private ResponceEtudiantDto getEtudiantWithFiliere(Etudiant etudiant) {
        ResponceEtudiantDto dto = etudiantMapper.ENTITY_TO_DTO(etudiant);

        if (etudiant.getFiliereId() == null) {
            dto.setFiliereCode("Non attribué");
            dto.setFiliereLibelle("Aucune filière");
            return dto;
        }

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String token = null;
            if (auth instanceof JwtAuthenticationToken jwtAuth) {
                token = jwtAuth.getToken().getTokenValue();
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (token != null) {
                headers.setBearerAuth(token);
            }

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            String url = "http://localhost:8084/v1/filieres/" + etudiant.getFiliereId();

            ResponseEntity<FiliereResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    FiliereResponse.class
            );

            FiliereResponse filiere = response.getBody();

            if (filiere != null && filiere.getCode() != null) {
                dto.setFiliereCode(filiere.getCode());
                dto.setFiliereLibelle(filiere.getLibelle());
            } else {
                dto.setFiliereCode("Inconnue");
                dto.setFiliereLibelle("Filière non trouvée");
            }

        } catch (HttpClientErrorException.Unauthorized e) {
            dto.setFiliereCode("Erreur Auth");
            dto.setFiliereLibelle("Token invalide ou expiré");
        } catch (Exception e) {
            dto.setFiliereCode("Erreur service");
            dto.setFiliereLibelle("Service filière indisponible");
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
