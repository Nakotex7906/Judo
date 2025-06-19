package org.example.controllerweb;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class RecuperacionController {

    @GetMapping("/recuperar")
    public String mostrarSeleccionTipoUsuario() {
        return "ResetPassword/seleccion-recuperacion";
    }

}