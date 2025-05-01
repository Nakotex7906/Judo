package org.example.service;

import org.example.model.atleta.Atleta;
import org.example.repository.AtletaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AtletaService {

    private final AtletaRepository atletaRepository;

    public AtletaService(AtletaRepository atletaRepository) {
        this.atletaRepository = atletaRepository;
    }

    public List<Atleta> listarAtletas() {
        return atletaRepository.findAll();
    }

    public Optional<Atleta> buscarPorId(Long id) {
        return atletaRepository.findById(id);
    }

    public List<Atleta> buscarPorNombre(String nombre) {
        return atletaRepository.findByNombre(nombre);
    }

    public Atleta guardarAtleta(Atleta atleta) {
        return atletaRepository.save(atleta);
    }

    public void eliminarAtleta(Long id) {
        atletaRepository.deleteById(id);
    }

}
