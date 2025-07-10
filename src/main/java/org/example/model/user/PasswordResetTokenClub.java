package org.example.model.user;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Entidad que representa un token de restablecimiento de contraseña para usuarios de tipo {@link Club}.
 * <p>
 * Este token es generado al solicitar la recuperación de contraseña y se valida
 * al momento de realizar el cambio. Tiene una fecha de expiración y un flag de uso.
 * </p>
 *
 * @see org.example.service.resetpassword.PasswordResetClubService
 * @see PasswordResetTokenJudoka
 * @author Benjamin Beroiza
 */
@Data
@Entity
public class PasswordResetTokenClub {

    /** Identificador único del token (clave primaria autogenerada). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Cadena única que representa el token. */
    private String token;

    /** Club asociado al token de restablecimiento. */
    @OneToOne
    private Club club;

    /** Fecha y hora de expiración del token. */
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
