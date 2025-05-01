package org.example.service;

import org.example.model.competencia.Competencia;
import org.example.repository.AtletaRepository;
import org.example.repository.CompetenciaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompetenciaService {

    private final CompetenciaRepository competenciaRepository;

    public CompetenciaService(CompetenciaRepository competenciaRepository) {
        this.competenciaRepository = competenciaRepository;
    }

    public List<Competencia> listarCompetencias() {
        return competenciaRepository.findAll();
    }

    public Optional<Competencia> buscarPorId(Long id) {
        return competenciaRepository.findById(id);
    }

    public List<Competencia> buscarPorNombre(String nombre) {
        return competenciaRepository.findByNombre(nombre);
    }

    public Competencia guardarCompetencia(Competencia competencia) {
        return competenciaRepository.save(competencia);
    }

    public void eliminarCompetencia(Long id) {
        competenciaRepository.deleteById(id);
    }


}
