package org.example.controllerWeb;

import lombok.AllArgsConstructor;
import org.example.model.Main;
import org.example.model.judoka.Judoka;
import org.example.service.JudokaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.example.model.logger.LoggerManager;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The type Judoka web controller.
 */
@AllArgsConstructor
@Controller

public class JudokaWebController {
    private static final Logger logger = LoggerManager.getLogger(JudokaWebController.class);
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
        //Mensaje por si no hay judokas
        if(judokas.isEmpty()){
            logger.log(Level.INFO,"No hay judokas registradas");
        }
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
    @PostMapping("/judokas/agregar")//Se mejoro el metodo para parametros invalidos
    public String agregarJudoka(@RequestParam String nombre,
                                @RequestParam String apellido,
                                @RequestParam String categoria,
                                @RequestParam String fechaNacimiento) {
        logger.log(Level.INFO,"Intentando agregar Judoka: " + nombre
                + " " + apellido + " " +
                categoria + " " + fechaNacimiento + "");
        // Validacion de los campos
        if(     nombre == null || nombre.isEmpty() ||
                apellido == null || apellido.isEmpty() ||
                categoria == null || categoria.isEmpty() ||
                fechaNacimiento == null || fechaNacimiento.isEmpty()){
            return "redirect:/judokas?error=DatosInvalidos";// Redirige con error

        }
        // Verificar el formato de la fecha
        try {
            LocalDate.parse(fechaNacimiento);
            // Valida el formato yyyy-MM-dd
        } catch (DateTimeParseException e) {
            logger.log(Level.WARNING, "Error en formato de fecha");
            return "redirect:/judokas?error=FechaInvalida";// Redirige con error de fecha

        }
        try {
            Judoka nuevo = new Judoka(nombre, apellido, categoria, fechaNacimiento);
            judokaService.guardarJudoka(nuevo);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error en guardado de Judoka");
            return "redirect:/judokas?error=ErrorGuardado";
        }
        logger.log(Level.INFO,"Judoka guardado correctamente");
        return "redirect:/judokas";
    }

}
