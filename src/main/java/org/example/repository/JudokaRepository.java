package org.example.repository;

import org.example.model.judoka.Judoka;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JudokaRepository extends JpaRepository<Judoka, Long> {

    List<Judoka> findByNombre(String nombre);

}
