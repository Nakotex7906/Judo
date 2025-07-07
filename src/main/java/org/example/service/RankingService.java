package org.example.service;

import org.example.model.user.Judoka;
import org.example.repository.RankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;


/**
 * Servicio que gestiona el ranking de los judokas.
 */
@Service
@RequiredArgsConstructor
public class RankingService {

    private final RankingRepository rankingRepository;

    /**
     * Retorna ranking completo por % de victorias.
     */
    public List<Judoka> obtenerRankingJudokas() {
        return rankingRepository.findAll().stream()
                .sorted(Comparator.comparingDouble(Judoka::calcularPorcentajeVictorias).reversed())
                .toList();
    }

    /**
     * Retorna ranking por categoría específica.
     *
     * @param categoria Categoría de peso
     * @return Lista ordenada por porcentaje de victorias
     */
    public List<Judoka> obtenerRankingPorCategoria(String categoria) {
        return rankingRepository.findAll().stream()
                .filter(j -> j.getCategoria() != null && j.getCategoria().equalsIgnoreCase(categoria))
                .sorted(Comparator.comparingDouble(Judoka::calcularPorcentajeVictorias).reversed())
                .toList();
    }
}
