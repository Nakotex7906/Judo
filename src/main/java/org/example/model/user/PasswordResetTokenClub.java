package org.example.model.user;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class PasswordResetTokenClub {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne
    private Club club;

    private LocalDateTime expiryDate;

    private boolean used;

    public boolean isExpired() {
        return expiryDate.isBefore(LocalDateTime.now());
    }

}

