package com.example.gestionfaculteetudiant.repository;

import com.example.gestionfaculteetudiant.entity.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EtudiantRepository extends JpaRepository<Etudiant, Integer> {
}
