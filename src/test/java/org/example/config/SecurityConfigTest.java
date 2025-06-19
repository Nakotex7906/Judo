package org.example.config;

import org.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la clase
 * Se valida la configuración de seguridad, la funcionalidad del codificador de contraseñas
 * y las reglas de acceso a rutas públicas y protegidas.
 */
@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Mock
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    private SecurityConfig securityConfig;

    /**
     * Inicializa los componentes necesarios antes de cada prueba.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        securityConfig = new SecurityConfig(userService);
    }

    /**
     * Verifica que las rutas públicas (/login, /, /registro) sean accesibles
     * y respondan correctamente con código 200 OK.
     *
     * @throws Exception en caso de error en el mock MVC
     */
    @Test
    void testPublicAccess() throws Exception {
        mockMvc.perform(get("/login","/","/registro")).andExpect(status().isOk());
    }

    /**
     * Verifica que el acceso a una ruta protegida sin autenticación
     * redirige apropiadamente al inicio de sesión (código de redirección 3xx).
     *
     * @throws Exception en caso de error en el mock MVC
     */
    @Test
    void testProtectedAccess() throws Exception {
        mockMvc.perform(get("/club_home"))
                .andExpect(status().is3xxRedirection()); // Debería redirigir al login si no está autenticado
    }

    /**
     * Prueba que el método filterChain devuelve correctamente una instancia de SecurityFilterChain
     * al recibir un objeto HttpSecurity.
     *
     * @throws Exception en caso de error en la configuración de seguridad
     */
    @Test
    void testFilterChain() throws Exception {
        // Given
        HttpSecurity httpSecurity = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);

        // When
        SecurityFilterChain result = securityConfig.filterChain(httpSecurity);

        // Then
        assertNotNull(result);
    }

    /**
     * Prueba que el método passwordEncoder devuelve una instancia válida de BCryptPasswordEncoder.
     */
    @Test
    void testPasswordEncoder() {
        // When
        PasswordEncoder result = securityConfig.passwordEncoder();

        // Then
        assertNotNull(result);
        assertTrue(result instanceof BCryptPasswordEncoder);
    }

    /**
     * Verifica que el PasswordEncoder codifica correctamente una contraseña y
     * permite validar si una contraseña corresponde a su hash.
     */
    @Test
    void testPasswordEncoderFunctionality() {
        // Given
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        String rawPassword = "testPassword123";

        // When
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Then
        assertNotNull(encodedPassword);
        assertNotEquals(rawPassword, encodedPassword);
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
        assertFalse(passwordEncoder.matches("wrongPassword", encodedPassword));
    }

    /**
     * Verifica que el constructor de SecurityConfig inicializa correctamente el UserService.
     */
    @Test
    void testSecurityConfigConstructor() {
        // Given & When
        SecurityConfig config = new SecurityConfig(userService);

        // Then
        assertNotNull(config);
    }

    /**
     * Prueba que se pueden crear múltiples instancias de PasswordEncoder
     * y todas funcionan de forma consistente.
     */
    @Test
    void testMultiplePasswordEncoderInstances() {
        // Given
        PasswordEncoder encoder1 = securityConfig.passwordEncoder();
        PasswordEncoder encoder2 = securityConfig.passwordEncoder();
        String password = "samePassword";

        // When
        String encoded1 = encoder1.encode(password);
        String encoded2 = encoder2.encode(password);

        // Then
        assertNotNull(encoded1);
        assertNotNull(encoded2);

        // Validaciones cruzadas
        assertTrue(encoder1.matches(password, encoded1));
        assertTrue(encoder2.matches(password, encoded2));
        assertTrue(encoder1.matches(password, encoded2));
        assertTrue(encoder2.matches(password, encoded1));
    }

    /**
     * Verifica el comportamiento del codificador de contraseñas ante una contraseña vacía.
     */
    @Test
    void testPasswordEncoderWithEmptyPassword() {
        // Given
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        // When
        String emptyEncoded = passwordEncoder.encode("");

        // Then
        assertNotNull(emptyEncoded);
        assertTrue(passwordEncoder.matches("", emptyEncoded));
        assertFalse(passwordEncoder.matches("notEmpty", emptyEncoded));
    }

    /**
     * Comprueba que el codificador de contraseñas maneja correctamente contraseñas
     * que contienen caracteres especiales.
     */
    @Test
    void testPasswordEncoderWithSpecialCharacters() {
        // Given
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        String specialPassword = "P@ssw0rd!@#$%^&*()";

        // When
        String encodedPassword = passwordEncoder.encode(specialPassword);

        // Then
        assertNotNull(encodedPassword);
        assertTrue(passwordEncoder.matches(specialPassword, encodedPassword));
        assertFalse(passwordEncoder.matches("wrongPassword", encodedPassword));
    }
}