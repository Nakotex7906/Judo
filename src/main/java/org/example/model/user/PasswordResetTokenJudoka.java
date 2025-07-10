package org.example.model.user;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Entidad que representa un token de restablecimiento de contraseña para usuarios de tipo {@link Judoka}.
 * <p>
 * Este token se genera cuando un judoka solicita recuperar su contraseña.
 * Se valida al momento del restablecimiento y tiene una fecha de expiración y un estado de uso.
 * </p>
 *
 * @see org.example.service.resetpassword.PasswordResetJudokaService
 * @see PasswordResetTokenClub
 * @author Benjamin Beroiza
 */
@Data
@Entity
public class PasswordResetTokenJudoka {

    /** Identificador único del token (clave primaria autogenerada). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Cadena única que representa el token. */
    private String token;

    /** Judoka asociado al token. */
    @OneToOne
    private Judoka judoka;

    /** Fecha y hora en que el token expira. */
    private LocalDateTime expiryDate;

    /** Indica si el token ya fue utilizado. */
    private boolean used;

    /**
     * Verifica si el token ya ha expirado.
     *
     * @return {@code true} si el token está vencido, {@code false} en caso contrario
     */
    public boolean isExpired() {
        return expiryDate.isBefore(LocalDateTime.now());
    }
}
