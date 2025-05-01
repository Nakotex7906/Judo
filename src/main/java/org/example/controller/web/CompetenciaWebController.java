package org.example.controller.web;

import org.example.model.competencia.Competencia;
import org.example.service.CompetenciaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class CompetenciaWebController {

    private final CompetenciaService competenciaService;

    public CompetenciaWebController(CompetenciaService competenciaService) {
        this.competenciaService = competenciaService;
    }

    @GetMapping("/competencias")
    public String listarCompetencias(Model model) {
        List<Competencia> competencias = competenciaService.listarCompetencias();
        model.addAttribute("competencias", competencias);
        return "competencias";
    }

    @GetMapping("/competencias/detalle/{id}")
    public String detalleCompetencia(@PathVariable Long id, Model model) {
        Competencia competencia = competenciaService.buscarPorId(id)
                .orElse(null); // Manejar si no existe
        model.addAttribute("competencia", competencia);
        return "detalle_competencia"; // Nombre del template HTML
    }

}
