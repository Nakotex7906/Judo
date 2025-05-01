package org.example.repository;

import org.example.model.competencia.Competencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompetenciaRepository extends JpaRepository<Competencia, Long> {

    List<Competencia> findByNombre(String nombre);

}
