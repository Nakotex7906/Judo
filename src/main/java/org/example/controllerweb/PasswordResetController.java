package org.example.controllerweb;

import org.example.service.resetPassword.PasswordResetJudokaService;
import org.example.service.resetPassword.PasswordResetClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PasswordResetController {

    @Autowired
    private PasswordResetJudokaService judokaService;

    @Autowired
    private PasswordResetClubService clubService;

    // ======== JUDOKA ========

    @GetMapping("/recuperar/judoka")
    public String mostrarFormularioRecuperarJudoka() {
        return "ResetPassword/recuperar-judoka";
    }

    @PostMapping("/recuperar/judoka")
    public String procesarSolicitudJudoka(@RequestParam String username, Model model) {
        judokaService.crearToken(username);
        model.addAttribute("mensaje", "Se envió el enlace al correo registrado.");
        return "ResetPassword/recuperar-judoka";
    }

    @GetMapping("/restablecer/judoka")
    public String mostrarFormularioRestablecerJudoka(@RequestParam String token, Model model) {
        if (!judokaService.validarToken(token)) {
            model.addAttribute("error", "Token inválido o expirado.");
            return "error";
        }
        model.addAttribute("token", token);
        return "ResetPassword/restablecer-judoka";
    }

    @PostMapping("/restablecer/judoka")
    public String procesarNuevaPasswordJudoka(@RequestParam String token, @RequestParam String nuevaPassword, Model model) {
        judokaService.actualizarPassword(token, nuevaPassword);
        model.addAttribute("mensaje", "Contraseña actualizada correctamente.");
        return "Model/login";
    }

    // ======== CLUB ========

    @GetMapping("/recuperar/club")
    public String mostrarFormularioRecuperarClub() {
        return "ResetPassword/recuperar-club";
    }

    @PostMapping("/recuperar/club")
    public String procesarSolicitudClub(@RequestParam String username, Model model) {
        clubService.crearToken(username);
        model.addAttribute("mensaje", "Se envió el enlace al correo registrado.");
        return "ResetPassword/recuperar-club";
    }

    @GetMapping("/restablecer/club")
    public String mostrarFormularioRestablecerClub(@RequestParam String token, Model model) {
        if (!clubService.validarToken(token)) {
            model.addAttribute("error", "Token inválido o expirado.");
            return "error";
        }
        model.addAttribute("token", token);
        return "ResetPassword/restablecer-club";
    }

    @PostMapping("/restablecer/club")
    public String procesarNuevaPasswordClub(@RequestParam String token, @RequestParam String nuevaPassword, Model model) {
        clubService.actualizarPassword(token, nuevaPassword);
        model.addAttribute("mensaje", "Contraseña actualizada correctamente.");
        return "Model/login";
    }
}
