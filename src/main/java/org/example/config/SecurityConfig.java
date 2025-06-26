package org.example.config;

import lombok.RequiredArgsConstructor;
import org.example.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final UserService userService;
    private final CustomAuthenticationSuccessHandler successHandler; // MODIFICADO: Inyectamos nuestro nuevo manejador.


    // Configuración de seguridad para la aplicación, aquí se definen las reglas de acceso a las rutas
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Se reordenan y especifican las reglas de seguridad.
                        // 1. Rutas públicas que no requieren autenticación.
                        .requestMatchers("/", "/login", "/registro", "/css/**", "/js/**",
                                "/recuperar/**", "/restablecer/**", "/registro-judoka", "/registro-club").permitAll()

                        // 2. Rutas que puede ver cualquier usuario que haya iniciado sesión (sea Judoka o Club).
                        .requestMatchers("/lista", "/judokas", "/club/publico/**").authenticated()

                        // 3. Rutas específicas para cada rol. La regla más específica de "/club/publico/**" ya fue procesada.
                        //    Esta regla ahora se aplica al resto de URLs bajo /club/, como editar horarios, etc.
                        .requestMatchers("/club/**").hasRole("CLUB")
                        .requestMatchers("/judoka/**").hasRole("JUDOKA")

                        // 4. Cualquier otra petición que no coincida con las anteriores, requiere autenticación.
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

    // Configuración del AuthenticationManager, que se encarga de autenticar los usuarios
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder
                .userDetailsService(userService)
                .passwordEncoder(passwordEncoder());
        return authBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
