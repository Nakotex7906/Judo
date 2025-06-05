package org.example.service.auth;

public interface AuthenticationStrategy {
    boolean authenticate(String username, String password);
    String getTipo();

}
