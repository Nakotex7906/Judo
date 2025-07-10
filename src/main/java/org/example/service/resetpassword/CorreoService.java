package org.example.service.resetpassword;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Servicio encargado de enviar correos electrónicos.
 * <p>
 * Utiliza {@link JavaMailSender} para componer y enviar mensajes simples
 * de texto como parte del proceso de recuperación de contraseña u otras notificaciones.
 * </p>
 *
 * Este servicio puede ser reutilizado por otras funcionalidades que requieran notificación por email.
 *
 * @author Benjamin Beroiza, Ignacio Essus
 */
@AllArgsConstructor
@Service
public class CorreoService {

    private final JavaMailSender mailSender;

    /**
     * Envía un correo electrónico con los datos proporcionados.
     *
     * @param destino dirección de correo del destinatario
     * @param asunto  asunto del mensaje
     * @param mensaje contenido del mensaje (en texto plano)
     */
    public void enviarCorreo(String destino, String asunto, String mensaje) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(destino);
        email.setSubject(asunto);
        email.setText(mensaje);
        mailSender.send(email);
    }
}
