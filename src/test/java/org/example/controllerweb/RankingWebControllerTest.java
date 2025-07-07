package org.example.controllerweb;

import org.example.model.user.Judoka;
import org.example.service.RankingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RankingWebControllerTest {

    @Mock
    private RankingService rankingService;

    @Mock
    private Model model;

    private RankingWebController rankingWebController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        rankingWebController = new RankingWebController(rankingService);
    }

    @Test
    void testMostrarRankingCompleto() {
        // Preparar datos de prueba
        List<Judoka> judokas = Arrays.asList(
            new Judoka(), new Judoka(), new Judoka()
        );
        when(rankingService.obtenerRankingJudokas()).thenReturn(judokas);

        // Ejecutar el método
        String result = rankingWebController.mostrarRanking(null, model);

        // Verificar resultados
        assertEquals("Judoka/ranking", result);
        verify(model).addAttribute("ranking", judokas);
        verify(model).addAttribute("categoriaSeleccionada", null);
        verify(model).addAttribute(eq("categorias"), anyList());
        verify(rankingService).obtenerRankingJudokas();
        verify(rankingService, never()).obtenerRankingPorCategoria(anyString());
    }

    @Test
    void testMostrarRankingPorCategoria() {
        // Preparar datos de prueba
        String categoria = "-73 kg";
        List<Judoka> judokas = Arrays.asList(
            new Judoka(), new Judoka()
        );
        when(rankingService.obtenerRankingPorCategoria(categoria)).thenReturn(judokas);

        // Ejecutar el método
        String result = rankingWebController.mostrarRanking(categoria, model);

        // Verificar resultados
        assertEquals("Judoka/ranking", result);
        verify(model).addAttribute("ranking", judokas);
        verify(model).addAttribute("categoriaSeleccionada", categoria);
        verify(model).addAttribute(eq("categorias"), anyList());
        verify(rankingService).obtenerRankingPorCategoria(categoria);
        verify(rankingService, never()).obtenerRankingJudokas();
    }

    @Test
    void testMostrarRankingVacio() {
        // Preparar datos de prueba
        when(rankingService.obtenerRankingJudokas()).thenReturn(Collections.emptyList());

        // Ejecutar el método
        String result = rankingWebController.mostrarRanking(null, model);

        // Verificar resultados
        assertEquals("Judoka/ranking", result);
        verify(model).addAttribute("ranking", Collections.emptyList());
        verify(model).addAttribute("categoriaSeleccionada", null);
        verify(model).addAttribute(eq("categorias"), anyList());
    }

    @Test
    void testMostrarRankingCategoriaVacia() {
        // Preparar datos de prueba
        String categoriaVacia = "";
        List<Judoka> judokas = Arrays.asList(
            new Judoka(), new Judoka(), new Judoka()
        );
        when(rankingService.obtenerRankingJudokas()).thenReturn(judokas);

        // Ejecutar el método
        String result = rankingWebController.mostrarRanking(categoriaVacia, model);

        // Verificar resultados
        assertEquals("Judoka/ranking", result);
        verify(model).addAttribute("ranking", judokas);
        verify(model).addAttribute("categoriaSeleccionada", categoriaVacia);
        verify(model).addAttribute(eq("categorias"), anyList());
        verify(rankingService).obtenerRankingJudokas();
        verify(rankingService, never()).obtenerRankingPorCategoria(anyString());
    }
}
