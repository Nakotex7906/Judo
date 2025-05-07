package org.example.model.competencia;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.model.judoka.Judoka;
import org.example.model.logger.LoggerManager;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The type Competencia.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Competencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nombre;
    private String fecha;
    @ManyToMany
    @JoinTable(
            name = "competencia_judoka",
            joinColumns = @JoinColumn(name = "competencia_id"),
            inverseJoinColumns = @JoinColumn(name = "judoka_id")
    )
    private List<Judoka> participantes;

    @ManyToOne
    @JoinColumn(name = "ganador_id")
    @JsonBackReference // Sirve para que no sea infinito
    private Judoka ganador;

    private static final Logger logger = LoggerManager.getLogger(Competencia.class);

    /**
     * Instantiates a new Competencia.
     *
     * @param nombre        the nombre
     * @param fecha         the fecha
     * @param participantes the participantes
     */
    public Competencia(String nombre, String fecha, List<Judoka> participantes) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.participantes = participantes;
    }

    /**
     * Registrar ganador.
     *
     * @param nombreJudoka the nombre judoka
     */
    public void registrarGanador(String nombreJudoka) {
        for (Judoka judoka : participantes) {
            if (judoka.getNombre().equalsIgnoreCase(nombreJudoka)) {
                this.ganador = judoka;
                return;
            }
        }
        logger.log(Level.INFO,"El judoka no esta inscrito en esta competencia");
    }
}