package org.example.service.resetPassword;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class CorreoService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarCorreo(String destino, String asunto, String mensaje) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(destino);
        email.setSubject(asunto);
        email.setText(mensaje);
        mailSender.send(email);
    }
}
