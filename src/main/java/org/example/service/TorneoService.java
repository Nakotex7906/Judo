package org.example.service;

import lombok.AllArgsConstructor;
import org.example.model.competencia.Torneo;
import org.example.model.user.Judoka;
import org.example.repository.TorneoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

/**
 * The type Torneo service.
 */
@AllArgsConstructor
@Service
public class TorneoService {

    private final TorneoRepository torneoRepository;

    /**
     * Listar torneos list.
     *
     * @return the list
     */
    public List<Torneo> listarTorneos() {
        return torneoRepository.findAll();
    }

    /**
     * Buscar por id optional.
     *
     * @param id the id
     * @return the optional
     */
    @Transactional(readOnly = true)
    public Optional<Torneo> buscarPorId(Long id) {
        return torneoRepository.findById(id);
    }

    /**
     * Buscar por nombre list.
     *
     * @param nombre the nombre
     * @return the list
     */
    public List<Torneo> buscarPorNombre(String nombre) {
        return torneoRepository.findByNombre(nombre);
    }

    /**
     * Guardar torneo.
     *
     * @param torneo the torneo
     * @return the torneo
     */
    public Torneo guardarTorneo(Torneo torneo) {
        return torneoRepository.save(torneo);
    }

    /**
     * Eliminar torneo.
     *
     * @param id the id
     */
    public void eliminarTorneo(Long id) {
        torneoRepository.deleteById(id);
    }

    /**
     * Elimina participantes de un torneo específico.
     *
     * @param torneoId id del torneo
     * @param participantesIds lista de ids de judokas a eliminar
     */
    @Transactional
    public void eliminarParticipantesDeTorneo(Long torneoId, List<Long> participantesIds) {
        Optional<Torneo> torneoOpt = torneoRepository.findById(torneoId);
        if (torneoOpt.isPresent()) {
            Torneo torneo = torneoOpt.get();
            List<Judoka> participantes = torneo.getParticipantes();
            participantes.removeIf(j -> participantesIds.contains(j.getId()));
            torneo.setParticipantes(participantes);
            torneoRepository.save(torneo);
        }
    }

}
