package org.example.model.competencia;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.example.demo.model.logger.LoggerManager;
import org.example.model.user.Judoka;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * La clase Torneo representa un evento competitivo entre Judokas.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Torneo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nombre;
    private String fecha;

    @ManyToMany
    @JoinTable(
            name = "torneo_judoka",
            joinColumns = @JoinColumn(name = "torneo_id"),
            inverseJoinColumns = @JoinColumn(name = "judoka_id")
    )
    private List<Judoka> participantes;

    @ManyToOne
    @JoinColumn(name = "ganador_id")
    @JsonBackReference // Evita referencias cíclicas al serializar
    private Judoka ganador;

    private static final Logger logger = LoggerManager.getLogger(Torneo.class);

    /**
     * Crea un nuevo Torneo con nombre, fecha y participantes.
     *
     * @param nombre        el nombre del torneo
     * @param fecha         la fecha del torneo
     * @param participantes la lista de judokas participantes
     */
    public Torneo(String nombre, String fecha, List<Judoka> participantes) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.participantes = participantes;
    }

    /**
     * Registra al judoka ganador del torneo.
     *
     * @param nombreJudoka el nombre del judoka ganador
     */
    public void registrarGanador(String nombreJudoka) {
        for (Judoka judoka : participantes) {
            if (judoka.getNombre().equalsIgnoreCase(nombreJudoka)) {
                this.ganador = judoka;
                return;
            }
        }
        logger.log(Level.INFO, "El judoka no está inscrito en este torneo");
    }
}
