package org.example.controllerweb;

import org.example.service.resetpassword.PasswordResetJudokaService;
import org.example.service.resetpassword.PasswordResetClubService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador web encargado de gestionar el proceso de recuperación y restablecimiento de contraseñas
 * para los usuarios de tipo Judoka y Club.
 *
 * <p>Incluye rutas para mostrar formularios de recuperación, procesar solicitudes,
 * validar tokens y actualizar contraseñas.</p>
 *
 * <p>Por razones de seguridad, el sistema no revela si un usuario existe al solicitar recuperación.</p>
 *
 * @author Benjamin Beroiza, Ignacio Essus
 */
@Controller
public class PasswordResetController {

    /** Atributo de modelo para mensajes de éxito o información */
    public static final String MENSAJE = "mensaje";

    /** Atributo de modelo para mensajes de error */
    public static final String ERROR = "error";

    private final PasswordResetJudokaService judokaService;
    private final PasswordResetClubService clubService;

    /**
     * Constructor que inyecta los servicios de restablecimiento para Judoka y Club.
     *
     * @param judokaService servicio de recuperación para judokas
     * @param clubService servicio de recuperación para clubes
     */
    public PasswordResetController(PasswordResetJudokaService judokaService, PasswordResetClubService clubService) {
        this.judokaService = judokaService;
        this.clubService = clubService;
    }

    // ======== JUDOKA ========

    /**
     * Muestra el formulario para solicitar recuperación de contraseña para judokas.
     */
    @GetMapping("/recuperar/judoka")
    public String mostrarFormularioRecuperarJudoka() {
        return "ResetPassword/recuperar-judoka";
    }

    /**
     * Procesa la solicitud de recuperación de contraseña de un judoka.
     *
     * @param username nombre de usuario (correo) del judoka
     * @param model modelo de atributos para la vista
     */
    @PostMapping("/recuperar/judoka")
    public String procesarSolicitudJudoka(@RequestParam String username, Model model) {
        try {
            judokaService.crearToken(username);
        } catch (RuntimeException _) {
            // Por seguridad, se oculta si el usuario existe o no
        }
        model.addAttribute(MENSAJE, "Se envió el enlace al correo registrado (si existe).");
        return "ResetPassword/recuperar-judoka";
    }

    /**
     * Muestra el formulario de restablecimiento de contraseña para judokas.
     *
     * @param token token recibido por correo
     * @param model modelo de atributos para la vista
     */
    @GetMapping("/restablecer/judoka")
    public String mostrarFormularioRestablecerJudoka(@RequestParam String token, Model model) {
        if (!judokaService.validarToken(token)) {
            model.addAttribute(ERROR, "Token inválido o expirado.");
            return ERROR;
        }
        model.addAttribute("token", token);
        return "ResetPassword/restablecer-judoka";
    }

    /**
     * Procesa el nuevo password para el judoka después de la validación del token.
     *
     * @param token token de restablecimiento válido
     * @param nuevaPassword nueva contraseña elegida por el usuario
     * @param model modelo de atributos para la vista
     */
    @PostMapping("/restablecer/judoka")
    public String procesarNuevaPasswordJudoka(@RequestParam String token, @RequestParam String nuevaPassword, Model model) {
        judokaService.actualizarPassword(token, nuevaPassword);
        model.addAttribute(MENSAJE, "Contraseña actualizada correctamente.");
        return "Model/login";
    }

    // ======== CLUB ========

    /**
     * Muestra el formulario para solicitar recuperación de contraseña para clubes.
     */
    @GetMapping("/recuperar/club")
    public String mostrarFormularioRecuperarClub() {
        return "ResetPassword/recuperar-club";
    }

    /**
     * Procesa la solicitud de recuperación de contraseña de un club.
     *
     * @param username nombre de usuario (correo) del club
     * @param model modelo de atributos para la vista
     */
    @PostMapping("/recuperar/club")
    public String procesarSolicitudClub(@RequestParam String username, Model model) {
        clubService.crearToken(username);
        model.addAttribute(MENSAJE, "Se envió el enlace al correo registrado.");
        return "ResetPassword/recuperar-club";
    }

    /**
     * Muestra el formulario de restablecimiento de contraseña para clubes.
     *
     * @param token token recibido por correo
     * @param model modelo de atributos para la vista
     */
    @GetMapping("/restablecer/club")
    public String mostrarFormularioRestablecerClub(@RequestParam String token, Model model) {
        if (!clubService.validarToken(token)) {
            model.addAttribute(ERROR, "Token inválido o expirado.");
            return ERROR;
        }
        model.addAttribute("token", token);
        return "ResetPassword/restablecer-club";
    }

    /**
     * Procesa el nuevo password para el club después de la validación del token.
     *
     * @param token token de restablecimiento válido
     * @param nuevaPassword nueva contraseña elegida por el usuario
     * @param model modelo de atributos para la vista
     */
    @PostMapping("/restablecer/club")
    public String procesarNuevaPasswordClub(@RequestParam String token, @RequestParam String nuevaPassword, Model model) {
        clubService.actualizarPassword(token, nuevaPassword);
        model.addAttribute(MENSAJE, "Contraseña actualizada correctamente.");
        return "Model/login";
    }
}
