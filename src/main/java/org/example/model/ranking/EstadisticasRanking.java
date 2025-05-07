package org.example.model.ranking;

import org.example.model.judoka.Judoka;
import org.example.model.judoka.GestionJudokas;
import org.example.model.example.LoggerManager;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The type Estadisticas ranking.
 */
public class EstadisticasRanking {

    private static final Logger logger = LoggerManager.getLogger(EstadisticasRanking.class);

    private GestionJudokas gestorJudokas;

    /**
     * Instantiates a new Estadisticas ranking.
     *
     * @param gestorJudokass the gestor judokass
     */
    public EstadisticasRanking(GestionJudokas gestorJudokass) {
        this.gestorJudokas = gestorJudokass;
    }

    /**
     * Mostrar estadisticas desde consola.
     *
     * @param scanner the scanner
     */
    public void mostrarEstadisticasDesdeConsola(Scanner scanner) {
        logger.log(Level.INFO,"Nombre del Judoka: ");
        String nombre = scanner.nextLine();

        Judoka judoka = gestorJudokas.obtenerJudoka(nombre);
        if (judoka != null) {
            if (logger.isLoggable(Level.INFO)) {
                logger.log(Level.INFO, judoka.mostrarInformacion());
            }
        } else {
            logger.log(Level.INFO,"El judoka no esta registrado.");
        }
    }

    /**
     * Calcular ranking.
     */
    public void calcularRanking() {
        List<Judoka> judokas = gestorJudokas.getListaJudokas();

        judokas.sort(Comparator.comparingInt(Judoka::getVictorias).reversed());

        logger.log(Level.INFO,"Ranking de judokas (por numero de victorias):");
        for (Judoka a : judokas) {
            logger.log(Level.INFO, "{0} {1} - Victorias: {2}, Derrotas: {3}, Empates: {4}",
         new Object[]{a.getNombre(), a.getApellido(), a.getVictorias(), a.getDerrotas(), a.getEmpates()});
        }
    }
}