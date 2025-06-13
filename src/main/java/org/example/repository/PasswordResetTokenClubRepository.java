package org.example.repository;

import org.example.model.user.PasswordResetTokenClub;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenClubRepository extends JpaRepository<PasswordResetTokenClub, Long> {
    Optional<PasswordResetTokenClub> findByToken(String token);
}
