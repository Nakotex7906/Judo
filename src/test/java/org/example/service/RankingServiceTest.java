package org.example.service;

import org.example.model.user.Judoka;
import org.example.repository.RankingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la clase {@link RankingService}, encargada de obtener y ordenar el ranking de judokas
 * según su porcentaje de victorias, ya sea en general o filtrado por categoría.
 */
@ExtendWith(MockitoExtension.class)
class RankingServiceTest {

    @Mock
    private RankingRepository rankingRepository;

    @InjectMocks
    private RankingService rankingService;

    /**
     * Verifica que {@code obtenerRankingJudokas} retorne correctamente una lista ordenada de judokas
     * en orden descendente según su porcentaje de victorias.
     */
    @Test
    void obtenerRankingJudokas_shouldReturnSortedJudokasByWinPercentage() {
        // Arrange
        Judoka judoka1 = mock(Judoka.class);
        Judoka judoka2 = mock(Judoka.class);
        Judoka judoka3 = mock(Judoka.class);

        when(judoka1.calcularPorcentajeVictorias()).thenReturn(70.0);
        when(judoka2.calcularPorcentajeVictorias()).thenReturn(85.0);
        when(judoka3.calcularPorcentajeVictorias()).thenReturn(60.0);
        when(rankingRepository.findAll()).thenReturn(List.of(judoka1, judoka2, judoka3));

        // Act
        List<Judoka> resultado = rankingService.obtenerRankingJudokas();

        // Assert
        assertEquals(3, resultado.size());
        assertEquals(judoka2, resultado.get(0));
        assertEquals(judoka1, resultado.get(1));
        assertEquals(judoka3, resultado.get(2));
        verify(rankingRepository).findAll();
    }

    /**
     * Verifica que {@code obtenerRankingPorCategoria} filtre correctamente los judokas por categoría
     * y los ordene según su porcentaje de victorias.
     */
    @Test
    void obtenerRankingPorCategoria_shouldReturnFilteredAndSortedJudokas() {
        // Arrange
        String categoria = "Ligero";

        Judoka judoka1 = mock(Judoka.class);
        Judoka judoka2 = mock(Judoka.class);
        Judoka judoka3 = mock(Judoka.class);

        when(judoka1.getCategoria()).thenReturn("Ligero");
        when(judoka2.getCategoria()).thenReturn("Ligero");
        when(judoka3.getCategoria()).thenReturn("Pesado");
        when(judoka1.calcularPorcentajeVictorias()).thenReturn(50.0);
        when(judoka2.calcularPorcentajeVictorias()).thenReturn(80.0);
        when(rankingRepository.findAll()).thenReturn(List.of(judoka1, judoka2, judoka3));

        // Act
        List<Judoka> resultado = rankingService.obtenerRankingPorCategoria(categoria);

        // Assert
        assertEquals(2, resultado.size());
        assertEquals(judoka2, resultado.get(0));
        assertEquals(judoka1, resultado.get(1));
        verify(rankingRepository).findAll();
    }
}
