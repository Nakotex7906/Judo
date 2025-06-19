package org.example.service.auth;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService {

    private final List<AuthenticationStrategy> strategies;

    public AuthenticationService(List<AuthenticationStrategy> strategies) {
        this.strategies = strategies;
    }

    public boolean authenticate(String tipo, String username, String password) {
        return strategies.stream()
                .filter(s -> s.getTipo().equalsIgnoreCase(tipo))
                .findFirst()
                .map(s -> s.authenticate(username, password))
                .orElse(false);
    }

    public boolean tipoValido(String tipo) {
        return strategies.stream().anyMatch(s -> s.getTipo().equalsIgnoreCase(tipo));
    }
}