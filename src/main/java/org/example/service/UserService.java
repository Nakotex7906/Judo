package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.repository.ClubRepository;
import org.example.repository.JudokaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Servicio personalizado que implementa {@link UserDetailsService}
 * para autenticar usuarios de tipo Club o Judoka.
 */
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final ClubRepository clubRepository;
    private final JudokaRepository judokaRepository;

    /**
     * Carga los detalles de un usuario (Club o Judoka) por su nombre de usuario.
     *
     * @param username el nombre de usuario (correo electrónico)
     * @return los detalles del usuario para autenticación
     * @throws UsernameNotFoundException si no se encuentra el usuario
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Buscar primero en Club
        return clubRepository.findByUsername(username)
                .map(club -> User.builder()
                        .username(club.getUsername())
                        .password(club.getPassword())
                        .roles("CLUB")
                        .build())
                // Si no es club, buscar en Judoka
                .orElseGet(() -> judokaRepository.findByUsername(username)
                        .map(judoka -> User.builder()
                                .username(judoka.getUsername())
                                .password(judoka.getPassword())
                                .roles("JUDOKA")
                                .build())
                        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username)));
    }
}
