package org.example.controller.web;

import lombok.AllArgsConstructor;
import org.example.model.judoka.Judoka;
import org.example.service.JudokaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * The type Judoka web controller.
 */
@AllArgsConstructor
@Controller
public class JudokaWebController {

    private final JudokaService judokaService;

    /**
     * Listar judokas string.
     *
     * @param model the model
     * @return the string
     */
    @GetMapping("/judokas")
    public String listarJudokas(Model model) {
        List<Judoka> judokas = judokaService.listarJudokas();
        model.addAttribute("judokas", judokas);
        return "judokas";
    }

    /**
     * Agregar judoka string.
     *
     * @param nombre          the nombre
     * @param apellido        the apellido
     * @param categoria       the categoria
     * @param fechaNacimiento the fecha nacimiento
     * @return the string
     */
    @PostMapping("/judokas/agregar")
    public String agregarJudoka(@RequestParam String nombre,
                                @RequestParam String apellido,
                                @RequestParam String categoria,
                                @RequestParam String fechaNacimiento) {
        Judoka nuevo = new Judoka(nombre, apellido, categoria, fechaNacimiento);
        judokaService.guardarjudoka(nuevo);
        return "redirect:/judokas";
    }

}
