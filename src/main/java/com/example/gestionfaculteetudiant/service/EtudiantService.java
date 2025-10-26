package com.example.gestionfaculteetudiant.service;

import com.example.gestionfaculteetudiant.dto.RequestEtudiantDto;
import com.example.gestionfaculteetudiant.dto.ResponceEtudiantDto;

import java.util.List;

public interface EtudiantService {
    public ResponceEtudiantDto AddEtudiant(RequestEtudiantDto requestEtudiantDto);
    public List<ResponceEtudiantDto> GETALLEtudiants();
    public ResponceEtudiantDto GETEtudiantById(Integer id);
    public ResponceEtudiantDto UPDATEEtudiant(Integer id , RequestEtudiantDto requestEtudiantDto);
    public void DELETEEtudiantBYID(Integer id);
}
