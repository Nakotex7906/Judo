package org.example.service.resetpassword;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CorreoService {

    private final JavaMailSender mailSender;


    public void enviarCorreo(String destino, String asunto, String mensaje) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(destino);
        email.setSubject(asunto);
        email.setText(mensaje);
        mailSender.send(email);
    }
}