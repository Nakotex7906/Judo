package org.example.service.resetpassword;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CorreoServiceTest {

    @Test
    void enviarCorreo() {
        // Arrange
        JavaMailSender mailSenderMock = mock(JavaMailSender.class);
        CorreoService correoService = new CorreoService(mailSenderMock);

        String destino = "destinatario@ejemplo.com";
        String asunto = "Asunto de prueba";
        String mensaje = "Mensaje de prueba";

        // Act
        correoService.enviarCorreo(destino, asunto, mensaje);

        // Assert
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSenderMock, times(1)).send(captor.capture());

        SimpleMailMessage enviado = captor.getValue();
        assertArrayEquals(new String[]{destino}, enviado.getTo());
        assertEquals(asunto, enviado.getSubject());
        assertEquals(mensaje, enviado.getText());
    }
}