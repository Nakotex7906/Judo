package org.example.service;

import org.example.model.competencia.Torneo;
import org.example.repository.TorneoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para manejar operaciones relacionadas con los torneos.
 */
@Service
public class TorneoService {

    private final TorneoRepository torneoRepository;

    /**
     * Constructor del servicio de torneos.
     *
     * @param torneoRepository el repositorio de torneos
     */
    public TorneoService(TorneoRepository torneoRepository) {
        this.torneoRepository = torneoRepository;
    }

    /**
     * Lista todos los torneos.
     *
     * @return lista de torneos
     */
    public List<Torneo> listarTorneos() {
        return torneoRepository.findAll();
    }

    /**
     * Busca un torneo por su ID.
     *
     * @param id el ID del torneo
     * @return un Optional con el torneo encontrado
     */
    public Optional<Torneo> buscarPorId(Long id) {
        return torneoRepository.findById(id);
    }

    /**
     * Busca torneos por nombre.
     *
     * @param nombre el nombre del torneo
     * @return lista de torneos que coincidan con el nombre
     */
    public List<Torneo> buscarPorNombre(String nombre) {
        return torneoRepository.findByNombre(nombre);
    }

    /**
     * Guarda un nuevo torneo o actualiza uno existente.
     *
     * @param torneo el torneo a guardar
     * @return el torneo guardado
     */
    public Torneo guardarTorneo(Torneo torneo) {
        return torneoRepository.save(torneo);
    }

    /**
     * Elimina un torneo por su ID.
     *
     * @param id el ID del torneo a eliminar
     */
    public void eliminarTorneo(Long id) {
        torneoRepository.deleteById(id);
    }

}
