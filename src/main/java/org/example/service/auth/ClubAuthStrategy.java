package org.example.service.auth;

import lombok.RequiredArgsConstructor;
import org.example.service.ClubService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClubAuthStrategy implements AuthenticationStrategy{

    private final ClubService clubService;

    @Override
    public boolean authenticate(String username, String password) {
        return clubService.validarContrasena(username, password);
    }

    @Override
    public String getTipo() {
        return "club";
    }


}
