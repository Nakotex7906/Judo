package org.example.service;

import org.example.model.competencia.Torneo;
import org.example.repository.TorneoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * The type Torneo service.
 */
@Service
public class TorneoService {

    private final TorneoRepository torneoRepository;

    /**
     * Instantiates a new Torneo service.
     *
     * @param torneoRepository the torneo repository
     */
    public TorneoService(TorneoRepository torneoRepository) {
        this.torneoRepository = torneoRepository;
    }

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

}
