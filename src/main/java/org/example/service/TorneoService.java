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
 * Servicio que maneja la lógica de negocio relacionada con los torneos.
 */
@AllArgsConstructor
@Service
public class TorneoService {

    private final TorneoRepository torneoRepository;

    /**
     * Obtiene la lista completa de torneos registrados.
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
     * @return un Optional con el torneo encontrado, o vacío si no existe
     */
    @Transactional(readOnly = true)
    public Optional<Torneo> buscarPorId(Long id) {
        return torneoRepository.findById(id);
    }

    /**
     * Busca torneos por su nombre exacto.
     *
     * @param nombre el nombre del torneo
     * @return lista de torneos que coinciden con el nombre
     */
    public List<Torneo> buscarPorNombre(String nombre) {
        return torneoRepository.findByNombre(nombre);
    }

    /**
     * Guarda o actualiza un torneo en la base de datos.
     *
     * @param torneo el torneo a guardar
     * @return el torneo guardado con ID actualizado si fue creado
     */
    public Torneo guardarTorneo(Torneo torneo) {
        return torneoRepository.save(torneo);
    }

    /**
     * Elimina un torneo de la base de datos según su ID.
     *
     * @param id el ID del torneo a eliminar
     */
    public void eliminarTorneo(Long id) {
        torneoRepository.deleteById(id);
    }

    /**
     * Elimina participantes de un torneo específico.
     *
     * @param torneoId ID del torneo
     * @param participantesIds lista de IDs de judokas a eliminar del torneo
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
