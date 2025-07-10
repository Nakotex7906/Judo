package org.example.config;

import lombok.RequiredArgsConstructor;
import org.example.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración principal de seguridad para la aplicación.
 * Define las reglas de acceso a las rutas, manejo de sesiones y autenticación con Spring Security.
 * <p>
 * Esta clase utiliza inyección de dependencias para personalizar el proceso de login y protección de recursos,
 * incluyendo un manejador de autenticación personalizado y un servicio de usuarios propio.
 * </p>
 *
 * @author Ignacio Essus
 */
@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    /** Servicio personalizado que carga los datos del usuario desde la base de datos */
    private final UserService userService;

    /** Manejador personalizado que redirige después de un login exitoso */
    private final CustomAuthenticationSuccessHandler successHandler;

    /**
     * Define las reglas de autorización y configuración del formulario de login.
     *
     * @param http instancia de {@link HttpSecurity} proporcionada por Spring Security
     * @return el filtro de seguridad configurado
     * @throws Exception en caso de error en la configuración
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/registro", "/css/**", "/js/**",
                                "/recuperar/**", "/restablecer/**", "/registro-judoka", "/registro-club",
                                "/judoka/publico/**").permitAll()
                        .requestMatchers("/lista", "/judokas", "/club/publico/**").authenticated()
                        .requestMatchers("/club/**").hasRole("CLUB")
                        .requestMatchers("/judoka/**").hasRole("JUDOKA")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(successHandler)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                );
        return http.build();
    }

    /**
     * Configura el administrador de autenticación que usará el servicio de usuarios personalizado
     * y el codificador de contraseñas.
     *
     * @param http instancia de {@link HttpSecurity}
     * @return el administrador de autenticación
     * @throws Exception en caso de error en la construcción
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder
                .userDetailsService(userService)
                .passwordEncoder(passwordEncoder());
        return authBuilder.build();
    }

    /**
     * Proporciona el codificador de contraseñas que utiliza BCrypt para mayor seguridad.
     *
     * @return una instancia de {@link PasswordEncoder}
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
