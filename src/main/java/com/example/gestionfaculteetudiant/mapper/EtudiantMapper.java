package com.example.gestionfaculteetudiant.mapper;

import com.example.gestionfaculteetudiant.dto.RequestEtudiantDto;
import com.example.gestionfaculteetudiant.dto.ResponceEtudiantDto;
import com.example.gestionfaculteetudiant.entity.Etudiant;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class EtudiantMapper {
    public Etudiant DTO_TO_ENTITY(RequestEtudiantDto dto){
        Etudiant etudiant = new Etudiant();
        BeanUtils.copyProperties(dto, etudiant);
        return etudiant;
    }

    public ResponceEtudiantDto ENTITY_TO_DTO(Etudiant etudiant){
        ResponceEtudiantDto dto = new ResponceEtudiantDto();
        BeanUtils.copyProperties(etudiant, dto);
        dto.setId(etudiant.getId());
        return dto;
    }
}

