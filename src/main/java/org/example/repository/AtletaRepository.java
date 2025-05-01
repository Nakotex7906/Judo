package org.example.repository;

import org.example.model.atleta.Atleta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AtletaRepository extends JpaRepository<Atleta, Long> {

    List<Atleta> findByNombre(String nombre);

}
