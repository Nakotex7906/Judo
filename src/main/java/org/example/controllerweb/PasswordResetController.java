package org.example.controllerweb;

import org.example.service.resetpassword.PasswordResetJudokaService;
import org.example.service.resetpassword.PasswordResetClubService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PasswordResetController {

    public static final String MENSAJE = "mensaje";
    public static final String ERROR = "error";

    private PasswordResetJudokaService judokaService;

    private PasswordResetClubService clubService;

    public PasswordResetController(PasswordResetJudokaService judokaService, PasswordResetClubService clubService) {
        this.judokaService = judokaService;
        this.clubService = clubService;
    }

    // ======== JUDOKA ========

    @GetMapping("/recuperar/judoka")
    public String mostrarFormularioRecuperarJudoka() {
        return "ResetPassword/recuperar-judoka";
    }

    @PostMapping("/recuperar/judoka")
    public String procesarSolicitudJudoka(@RequestParam String username, Model model) {
        try {
            judokaService.crearToken(username);
            model.addAttribute(MENSAJE, "Se envió el enlace al correo registrado (si existe).");
        } catch (RuntimeException _) {
            // No reveles si el usuario existe o no por seguridad
            model.addAttribute(MENSAJE, "Se envió el enlace al correo registrado (si existe).");
        }
        return "ResetPassword/recuperar-judoka";
    }


    @GetMapping("/restablecer/judoka")
    public String mostrarFormularioRestablecerJudoka(@RequestParam String token, Model model) {
        if (!judokaService.validarToken(token)) {
            model.addAttribute(ERROR, "Token inválido o expirado.");
            return ERROR;
        }
        model.addAttribute("token", token);
        return "ResetPassword/restablecer-judoka";
    }

    @PostMapping("/restablecer/judoka")
    public String procesarNuevaPasswordJudoka(@RequestParam String token, @RequestParam String nuevaPassword, Model model) {
        judokaService.actualizarPassword(token, nuevaPassword);
        model.addAttribute(MENSAJE, "Contraseña actualizada correctamente.");
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
        model.addAttribute(MENSAJE, "Se envió el enlace al correo registrado.");
        return "ResetPassword/recuperar-club";
    }

    @GetMapping("/restablecer/club")
    public String mostrarFormularioRestablecerClub(@RequestParam String token, Model model) {
        if (!clubService.validarToken(token)) {
            model.addAttribute(ERROR, "Token inválido o expirado.");
            return ERROR;
        }
        model.addAttribute("token", token);
        return "ResetPassword/restablecer-club";
    }

    @PostMapping("/restablecer/club")
    public String procesarNuevaPasswordClub(@RequestParam String token, @RequestParam String nuevaPassword, Model model) {
        clubService.actualizarPassword(token, nuevaPassword);
        model.addAttribute(MENSAJE, "Contraseña actualizada correctamente.");
        return "Model/login";
    }
}
