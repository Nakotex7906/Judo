package org.example.model.competencia;

import jakarta.persistence.*;
import lombok.*;
import org.example.model.user.Judoka;

import java.util.ArrayList;
import java.util.List;

/**
 * La clase Combate representa un enfrentamiento entre dos Judokas en un Torneo.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Combate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "judoka_blanco_id")
    private Judoka judokaBlanco;

    @ManyToOne
    @JoinColumn(name = "judoka_azul_id")
    private Judoka judokaAzul;

    @ManyToOne
    @JoinColumn(name = "torneo_id")
    private Torneo torneo;

    @ManyToOne
    @JoinColumn(name = "ganador_id")
    private Judoka ganador;

    private int duracionSegundos; // Duración del combate en segundos

    /**
     * Constructor para crear un combate sin ganador todavía.
     */
    public Combate(Judoka blanco, Judoka azul, Torneo torneo) {
        this.judokaBlanco = blanco;
        this.judokaAzul = azul;
        this.torneo = torneo;
        this.duracionSegundos = 0;
    }

    /**
     * Registra el ganador del combate.
     */
    public void registrarGanador(Judoka ganador) {
        if (ganador.equals(judokaBlanco) || ganador.equals(judokaAzul)) {
            this.ganador = ganador;
            ganador.aumentarVictoria();
            if (ganador.equals(judokaBlanco)) {
                judokaAzul.aumentarDerrota();
            } else {
                judokaBlanco.aumentarDerrota();
            }
        }
    }

    /**
     * Método estático para generar una lista de combates entre todos los judokas del torneo (round-robin).
     */
    public static List<Combate> generarCombates(List<Judoka> participantes, Torneo torneo) {
        List<Combate> combates = new ArrayList<>();
        for (int i = 0; i < participantes.size(); i++) {
            for (int j = i + 1; j < participantes.size(); j++) {
                Judoka blanco = participantes.get(i);
                Judoka azul = participantes.get(j);
                combates.add(new Combate(blanco, azul, torneo));
            }
        }
        return combates;
    }


    /**
     * Muestra la información del combate.
     */
    public String mostrarInformacion() {
        String resultado = (ganador != null) ? "Ganador: " + ganador.getNombre() : "Sin ganador";
        return "Combate: " + judokaBlanco.getNombre() + " vs " + judokaAzul.getNombre() +
                ", Duración: " + duracionSegundos + "s, " + resultado;
    }
}
