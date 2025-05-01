package org.example.service;

import org.example.model.judoka.Judoka;
import org.example.repository.JudokaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JudokaService {

    private final JudokaRepository atletaRepository;

    public JudokaService(JudokaRepository atletaRepository) {
        this.atletaRepository = atletaRepository;
    }

    public List<Judoka> listarAtletas() {
        return atletaRepository.findAll();
    }

    public Optional<Judoka> buscarPorId(Long id) {
        return atletaRepository.findById(id);
    }

    public List<Judoka> buscarPorNombre(String nombre) {
        return atletaRepository.findByNombre(nombre);
    }

    public Judoka guardarAtleta(Judoka atleta) {
        return atletaRepository.save(atleta);
    }

    public void eliminarAtleta(Long id) {
        atletaRepository.deleteById(id);
    }

}
