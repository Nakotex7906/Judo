package org.example.controller.web;

import org.example.model.judoka.Judoka;
import org.example.service.JudokaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class JudokaWebController {

    private final JudokaService atletaService;

    public JudokaWebController(JudokaService atletaService) {
        this.atletaService = atletaService;
    }

    @GetMapping("/judokas")
    public String listarAtletas(Model model) {
        List<Judoka> atletas = atletaService.listarAtletas();
        model.addAttribute("atletas", atletas);
        return "judokas";
    }

}
