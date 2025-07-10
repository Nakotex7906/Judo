package org.example.service;

import org.example.model.user.Judoka;
import org.example.repository.RankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * Servicio encargado de calcular y entregar el ranking de judokas,
 * ya sea general o filtrado por categoría de peso.
 */
@Service
@RequiredArgsConstructor
public class RankingService {

    private final RankingRepository rankingRepository;

    /**
     * Obtiene el ranking completo de judokas ordenado por porcentaje de victorias.
     *
     * @return lista de judokas ordenada de mayor a menor según su porcentaje de victorias
     */
    public List<Judoka> obtenerRankingJudokas() {
        return rankingRepository.findAll().stream()
                .sorted(Comparator.comparingDouble(Judoka::calcularPorcentajeVictorias).reversed())
                .toList();
    }

    /**
     * Obtiene el ranking de judokas filtrado por categoría de peso.
     * Los judokas se ordenan de mayor a menor según su porcentaje de victorias.
     *
     * @param categoria la categoría de peso por la que se desea filtrar (por ejemplo: "-66 kg")
     * @return lista de judokas de la categoría indicada, ordenada por rendimiento
     */
    public List<Judoka> obtenerRankingPorCategoria(String categoria) {
        return rankingRepository.findAll().stream()
                .filter(j -> j.getCategoria() != null && j.getCategoria().equalsIgnoreCase(categoria))
                .sorted(Comparator.comparingDouble(Judoka::calcularPorcentajeVictorias).reversed())
                .toList();
    }
}
