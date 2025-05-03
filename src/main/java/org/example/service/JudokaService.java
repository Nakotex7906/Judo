package org.example.service;

import lombok.AllArgsConstructor;
import org.example.model.judoka.Judoka;
import org.example.repository.JudokaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class JudokaService {

    private final JudokaRepository judokaRepository;

    public List<Judoka> listarJudokas() {
        return judokaRepository.findAll();
    }

    public Optional<Judoka> buscarPorId(Long id) {
        return judokaRepository.findById(id);
    }

    public List<Judoka> buscarPorNombre(String nombre) {
        return judokaRepository.findByNombre(nombre);
    }

    public Judoka guardarjudoka(Judoka judoka) {
        return judokaRepository.save(judoka);
    }

    public void eliminarJudoka(Long id) {
        judokaRepository.deleteById(id);
    }

    public List<Judoka> buscarPorIds(List<Long> participantes) {
        return judokaRepository.findAllById(participantes);
    }
}
