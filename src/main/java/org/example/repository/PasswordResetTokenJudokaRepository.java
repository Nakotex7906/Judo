package org.example.repository;

import org.example.model.user.Judoka;
import org.example.model.user.PasswordResetTokenJudoka;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenJudokaRepository extends JpaRepository<PasswordResetTokenJudoka, Long> {
    Optional<PasswordResetTokenJudoka> findByToken(String token);
    Optional<PasswordResetTokenJudoka> findByJudoka(Judoka judoka);

}
