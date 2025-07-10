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

/**
 * Pruebas unitarias para {@link RankingWebController}.
 * Verifica el comportamiento del controlador al mostrar rankings de judokas,
 * tanto completos como por categoría.
 */
class RankingWebControllerTest {

    @Mock
    private RankingService rankingService;

    @Mock
    private Model model;

    private RankingWebController rankingWebController;

    /**
     * Inicializa los mocks antes de cada prueba.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        rankingWebController = new RankingWebController(rankingService);
    }

    /**
     * Verifica que se retorne el ranking completo cuando no se especifica categoría.
     */
    @Test
    void testMostrarRankingCompleto() {
        List<Judoka> judokas = Arrays.asList(new Judoka(), new Judoka(), new Judoka());
        when(rankingService.obtenerRankingJudokas()).thenReturn(judokas);

        String result = rankingWebController.mostrarRanking(null, model);

        assertEquals("Judoka/ranking", result);
        verify(model).addAttribute("ranking", judokas);
        verify(model).addAttribute("categoriaSeleccionada", null);
        verify(model).addAttribute(eq("categorias"), anyList());
        verify(rankingService).obtenerRankingJudokas();
        verify(rankingService, never()).obtenerRankingPorCategoria(anyString());
    }

    /**
     * Verifica que se retorne el ranking filtrado por categoría.
     */
    @Test
    void testMostrarRankingPorCategoria() {
        String categoria = "-73 kg";
        List<Judoka> judokas = Arrays.asList(new Judoka(), new Judoka());
        when(rankingService.obtenerRankingPorCategoria(categoria)).thenReturn(judokas);

        String result = rankingWebController.mostrarRanking(categoria, model);

        assertEquals("Judoka/ranking", result);
        verify(model).addAttribute("ranking", judokas);
        verify(model).addAttribute("categoriaSeleccionada", categoria);
        verify(model).addAttribute(eq("categorias"), anyList());
        verify(rankingService).obtenerRankingPorCategoria(categoria);
        verify(rankingService, never()).obtenerRankingJudokas();
    }

    /**
     * Verifica que se maneje correctamente un ranking vacío.
     */
    @Test
    void testMostrarRankingVacio() {
        when(rankingService.obtenerRankingJudokas()).thenReturn(Collections.emptyList());

        String result = rankingWebController.mostrarRanking(null, model);

        assertEquals("Judoka/ranking", result);
        verify(model).addAttribute("ranking", Collections.emptyList());
        verify(model).addAttribute("categoriaSeleccionada", null);
        verify(model).addAttribute(eq("categorias"), anyList());
    }

    /**
     * Verifica que al pasar una cadena vacía como categoría,
     * se retorne el ranking completo.
     */
    @Test
    void testMostrarRankingCategoriaVacia() {
        String categoriaVacia = "";
        List<Judoka> judokas = Arrays.asList(new Judoka(), new Judoka(), new Judoka());
        when(rankingService.obtenerRankingJudokas()).thenReturn(judokas);

        String result = rankingWebController.mostrarRanking(categoriaVacia, model);

        assertEquals("Judoka/ranking", result);
        verify(model).addAttribute("ranking", judokas);
        verify(model).addAttribute("categoriaSeleccionada", categoriaVacia);
        verify(model).addAttribute(eq("categorias"), anyList());
        verify(rankingService).obtenerRankingJudokas();
        verify(rankingService, never()).obtenerRankingPorCategoria(anyString());
    }
}
