package org.example.model.competencia;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.model.logger.LoggerManager;
import org.example.model.user.Judoka;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Entidad que representa un Torneo de judo en el sistema.
 * <p>
 * Un torneo tiene un nombre, una fecha, una lista de judokas participantes y un ganador.
 * Se almacena en la base de datos mediante JPA y puede relacionarse con múltiples judokas.
 * </p>
 *
 * @author Benjamin Beroiza, Ignacio Essus, Alonso Romero
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Torneo {

    /** Identificador único del torneo (clave primaria autogenerada) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /** Nombre del torneo */
    private String nombre;

    /** Fecha en la que se realiza el torneo */
    private String fecha;

    /**
     * Lista de judokas que participan en el torneo.
     * <p>
     * Relación muchos a muchos con la entidad Judoka.
     * Se utiliza {@link FetchType#EAGER} para cargar los participantes junto con el torneo.
     * </p>
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "torneo_judoka",
            joinColumns = @JoinColumn(name = "torneo_id"),
            inverseJoinColumns = @JoinColumn(name = "judoka_id")
    )
    private List<Judoka> participantes;

    /**
     * Judoka ganador del torneo.
     * <p>
     * Relación muchos a uno. Se anota con {@code @JsonBackReference} para evitar
     * ciclos infinitos en la serialización JSON.
     * </p>
     */
    @ManyToOne
    @JoinColumn(name = "ganador_id")
    @JsonBackReference
    private Judoka ganador;

    /** Logger para registrar eventos relacionados al torneo */
    private static final Logger logger = LoggerManager.getLogger(Torneo.class);

    /**
     * Constructor para crear un nuevo torneo con nombre, fecha y lista de participantes.
     *
     * @param nombre        nombre del torneo
     * @param fecha         fecha del torneo
     * @param participantes lista de judokas participantes
     */
    public Torneo(String nombre, String fecha, List<Judoka> participantes) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.participantes = participantes;
    }

    /**
     * Registra al judoka ganador del torneo, buscando por nombre.
     * <p>
     * Si el judoka no está inscrito en el torneo, se muestra un log informativo.
     * </p>
     *
     * @param nombreJudoka nombre del judoka ganador
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
