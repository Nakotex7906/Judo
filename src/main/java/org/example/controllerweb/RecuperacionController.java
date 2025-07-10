package org.example.controllerweb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador web para gestionar la selección del tipo de usuario en el proceso de recuperación de contraseña.
 * <p>
 * Esta vista permite al usuario elegir si desea recuperar su contraseña como Judoka o como Club.
 * </p>
 *
 * Ruta: <code>/recuperar</code>
 * Vista asociada: <code>ResetPassword/seleccion-recuperacion.html</code>
 *
 * @author Benjamin Beroiza
 */
@Controller
public class RecuperacionController {

    /**
     * Muestra la vista para seleccionar el tipo de usuario que desea recuperar su contraseña.
     *
     * @return nombre de la vista con el formulario de selección
     */
    @GetMapping("/recuperar")
    public String mostrarSeleccionTipoUsuario() {
        return "ResetPassword/seleccion-recuperacion";
    }
}
