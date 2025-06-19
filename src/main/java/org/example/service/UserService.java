package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.repository.ClubRepository;
import org.example.repository.JudokaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final ClubRepository clubRepository;
    private final JudokaRepository judokaRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Buscar en Club
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
