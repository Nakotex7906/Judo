package org.example.config;

import org.example.model.user.Judoka;
import org.example.service.JudokaService;
import org.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
class RegistrationLoginIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JudokaService judokaService;

    @MockBean
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void testUserRegistersAndThenLogsIn() throws Exception {
        // Registro: no existe previamente y se guarda el judoka
        when(judokaService.findByUsername("nuevo@correo.es")).thenReturn(Optional.empty());
        doNothing().when(judokaService).guardarJudoka(any(Judoka.class));

        mockMvc.perform(post("/registro-judoka").with(csrf())
                        .param("username", "nuevo@correo.es")
                        .param("password", "clave")
                        .param("nombre", "Nuevo")
                        .param("apellido", "Usuario")
                        .param("categoria", "-60 kg")
                        .param("fechaNacimiento", "2000-01-01"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        // Login: el servicio de usuarios retorna un UserDetails válido
        UserDetails userDetails = User.builder()
                .username("nuevo@correo.es")
                .password(passwordEncoder.encode("clave"))
                .authorities("ROLE_JUDOKA") // Corrección aplicada aquí
                .build();
        when(userService.loadUserByUsername("nuevo@correo.es")).thenReturn(userDetails);

        mockMvc.perform(post("/login").with(csrf())
                        .param("username", "nuevo@correo.es")
                        .param("password", "clave"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/index"));
    }
}