package org.example.service;

import lombok.AllArgsConstructor;
import org.example.model.user.Judoka;
import org.example.repository.JudokaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * The type Judoka service.
 */
@AllArgsConstructor
@Service
public class JudokaService {

    private final JudokaRepository judokaRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Listar judokas list.
     *
     * @return the list
     */
    public List<Judoka> listarJudokas() {
        return judokaRepository.findAll();
    }

    /**
     * Guardarjudoka judoka.
     *
     * @param judoka the judoka
     */
    public void guardarJudoka(Judoka judoka) {
        if (!judoka.getPassword().startsWith("$2a") && !judoka.getPassword().startsWith("$2b")) {
            judoka.setPassword(passwordEncoder.encode(judoka.getPassword()));
        }
        judokaRepository.save(judoka);
    }

    public Optional<Judoka> findByUsername(String username) {
        return judokaRepository.findByUsername(username);
    }

    public boolean validarContrasena(String username, String password) {
        Optional<Judoka> opt = findByUsername(username);
        return opt.map(j -> passwordEncoder.matches(password, j.getPassword())).orElse(false);
    }

    /**
     * Buscar por ids list.
     *
     * @param participantes the participantes
     * @return the list
     */
    public List<Judoka> buscarPorIds(List<Long> participantes) {
        return judokaRepository.findAllById(participantes);
    }
}
