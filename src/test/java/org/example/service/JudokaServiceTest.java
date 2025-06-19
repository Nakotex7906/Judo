package org.example.service;

import org.example.model.user.Judoka;
import org.example.repository.JudokaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para {@link JudokaService}, encargado de operaciones sobre usuarios tipo Judoka.
 * Se utilizan mocks para garantizar bajo acoplamiento con {@link JudokaRepository} y {@link PasswordEncoder}.
 */
class JudokaServiceTest {

    private JudokaRepository judokaRepositoryMock;
    private PasswordEncoder passwordEncoderMock;
    private JudokaService judokaService;

    /**
     * Inicializa los mocks y la instancia de servicio antes de cada test.
     */
    @BeforeEach
    void setUp() {
        judokaRepositoryMock = mock(JudokaRepository.class);
        passwordEncoderMock = mock(PasswordEncoder.class);
        judokaService = new JudokaService(judokaRepositoryMock, passwordEncoderMock);
    }

    /**
     * Verifica que se devuelva la lista completa de judokas desde el repositorio.
     */
    @Test
    void listarJudokas_devuelveListaCompleta() {
        List<Judoka> lista = List.of(new Judoka(), new Judoka());
        when(judokaRepositoryMock.findAll()).thenReturn(lista);

        List<Judoka> resultado = judokaService.listarJudokas();

        assertEquals(2, resultado.size());
    }

    /**
     * Verifica que si la contraseña no está encriptada, se encripta y se guarda.
     */
    @Test
    void guardarJudoka_conPasswordNoEncriptada_encriptaYGuarda() {
        Judoka judoka = new Judoka();
        judoka.setPassword("clave123");

        when(passwordEncoderMock.encode("clave123")).thenReturn("$2a$encriptada");

        judokaService.guardarJudoka(judoka);

        assertEquals("$2a$encriptada", judoka.getPassword());
        verify(judokaRepositoryMock).save(judoka);
    }

    /**
     * Verifica que si la contraseña ya está encriptada, no se vuelve a encriptar.
     */
    @Test
    void guardarJudoka_conPasswordEncriptada_noModificaNiEncripta() {
        Judoka judoka = new Judoka();
        judoka.setPassword("$2a$hash");

        judokaService.guardarJudoka(judoka);

        verify(passwordEncoderMock, never()).encode(anyString());
        verify(judokaRepositoryMock).save(judoka);
    }

    /**
     * Verifica que se retorne un judoka si el usuario existe.
     */
    @Test
    void findByUsername_existente_devuelveOptionalConJudoka() {
        Judoka j = new Judoka();
        j.setUsername("judoka1");

        when(judokaRepositoryMock.findByUsername("judoka1")).thenReturn(Optional.of(j));

        Optional<Judoka> resultado = judokaService.findByUsername("judoka1");

        assertTrue(resultado.isPresent());
        assertEquals("judoka1", resultado.get().getUsername());
    }

    /**
     * Verifica que validarContrasena devuelva true si el password coincide.
     */
    @Test
    void validarContrasena_credencialesValidas_devuelveTrue() {
        Judoka j = new Judoka();
        j.setPassword("$2a$hash");

        when(judokaRepositoryMock.findByUsername("judoka1")).thenReturn(Optional.of(j));
        when(passwordEncoderMock.matches("1234", "$2a$hash")).thenReturn(true);

        boolean resultado = judokaService.validarContrasena("judoka1", "1234");

        assertTrue(resultado);
    }

    /**
     * Verifica que validarContrasena devuelva false si el password no coincide.
     */
    @Test
    void validarContrasena_credencialesInvalidas_devuelveFalse() {
        Judoka j = new Judoka();
        j.setPassword("$2a$hash");

        when(judokaRepositoryMock.findByUsername("judoka1")).thenReturn(Optional.of(j));
        when(passwordEncoderMock.matches("incorrecta", "$2a$hash")).thenReturn(false);

        boolean resultado = judokaService.validarContrasena("judoka1", "incorrecta");

        assertFalse(resultado);
    }

    /**
     * Verifica que validarContrasena devuelva false si el usuario no existe.
     */
    @Test
    void validarContrasena_usuarioNoExiste_devuelveFalse() {
        when(judokaRepositoryMock.findByUsername("noexiste")).thenReturn(Optional.empty());

        boolean resultado = judokaService.validarContrasena("noexiste", "pass");

        assertFalse(resultado);
    }

    /**
     * Verifica que se retornen todos los judokas por sus IDs.
     */
    @Test
    void buscarPorIds_devuelveJudokas() {
        List<Judoka> judokas = List.of(new Judoka(), new Judoka());
        when(judokaRepositoryMock.findAllById(List.of(1L, 2L))).thenReturn(judokas);

        List<Judoka> resultado = judokaService.buscarPorIds(List.of(1L, 2L));

        assertEquals(2, resultado.size());
    }
}
