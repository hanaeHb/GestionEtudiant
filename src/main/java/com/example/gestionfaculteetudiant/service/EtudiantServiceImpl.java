package com.example.gestionfaculteetudiant.service;

import com.example.gestionfaculteetudiant.dto.RequestEtudiantDto;
import com.example.gestionfaculteetudiant.dto.ResponceEtudiantDto;
import com.example.gestionfaculteetudiant.entity.Etudiant;
import com.example.gestionfaculteetudiant.mapper.EtudiantMapper;
import com.example.gestionfaculteetudiant.repository.EtudiantRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EtudiantServiceImpl implements EtudiantService {

    private EtudiantRepository etudiantRepository;
    private EtudiantMapper etudiantMapper;

    public EtudiantServiceImpl(EtudiantRepository etudiantRepository, EtudiantMapper etudiantMapper) {
        this.etudiantMapper = etudiantMapper;
        this.etudiantRepository = etudiantRepository;
    }

    @Override
    public ResponceEtudiantDto AddEtudiant(RequestEtudiantDto requestEtudiantDto) {

        Etudiant etudiant = etudiantMapper.DTO_TO_ENTITY(requestEtudiantDto);
        Etudiant savedEtudiant = etudiantRepository.save(etudiant);
        return etudiantMapper.ENTITY_TO_DTO(savedEtudiant);
    }

    @Override
    public List<ResponceEtudiantDto> GETALLEtudiants() {
        List<Etudiant> etudiants = etudiantRepository.findAll();
        List<ResponceEtudiantDto> etudiantDtos = new ArrayList<>();
        for (Etudiant etudiant : etudiants) {
            etudiantDtos.add(etudiantMapper.ENTITY_TO_DTO(etudiant));
        }
        return etudiantDtos;
    }

    @Override
    public ResponceEtudiantDto GETEtudiantById(Integer id) {
        Etudiant etudiant = etudiantRepository.findById(id).orElseThrow();
        return etudiantMapper.ENTITY_TO_DTO(etudiant);
    }

    @Override
    public ResponceEtudiantDto UPDATEEtudiant(Integer id, RequestEtudiantDto requestEtudiantDto) {
        Etudiant newetudiant = etudiantMapper.DTO_TO_ENTITY(requestEtudiantDto);
        Etudiant etudiant = etudiantRepository.findById(id).orElseThrow();

        if(newetudiant.getNom()!=null) etudiant.setNom(newetudiant.getNom());
        if(newetudiant.getPrenom()!=null) etudiant.setPrenom(newetudiant.getPrenom());
        if(newetudiant.getCne()!=null) etudiant.setCne(newetudiant.getCne());

        Etudiant savedEtudiant = etudiantRepository.save(etudiant);
        return etudiantMapper.ENTITY_TO_DTO(savedEtudiant);
    }

    @Override
    public void DELETEEtudiantBYID(Integer id) {
        etudiantRepository.deleteById(id);
    }
}
