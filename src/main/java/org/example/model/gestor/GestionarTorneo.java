package org.example.model.gestor;

import lombok.Getter;
import org.example.model.competencia.Torneo;
import org.example.model.user.Judoka;
import org.example.model.logger.LoggerManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Clase para gestionar torneos.
 */
@Getter
public class GestionarTorneo {

    private static final Logger logger = LoggerManager.getLogger(GestionarTorneo.class);
    private List<Torneo> torneos;
    private GestorJudokas gestorJudokas;

    /**
     * Instancia el gestor de torneos.
     *
     * @param gestorJudokas el gestor de judokas
     */
    public GestionarTorneo(GestorJudokas gestorJudokas) {
        this.gestorJudokas = gestorJudokas;
        this.torneos = new ArrayList<>();
    }

    /**
     * Agrega un torneo desde la consola.
     *
     * @param scanner el escáner para entrada de datos
     */
    public void agregarTorneoDesdeConsola(Scanner scanner) {
        String nombre = solicitarNombreTorneo(scanner);
        String fecha = solicitarFechaTorneo(scanner);
        List<Judoka> participantes = solicitarParticipantes(scanner);

        if (participantes.isEmpty()) {
            logger.log(Level.WARNING, "No se agregaron participantes válidos, torneo no creado");
            return;
        }

        Torneo torneo = new Torneo(nombre, fecha, participantes);
        torneos.add(torneo);
        logger.log(Level.INFO, "Torneo agregado con éxito.");
    }

    private String solicitarNombreTorneo(Scanner scanner) {
        logger.log(Level.INFO, "Nombre del Torneo: ");
        return scanner.nextLine();
    }

    private String solicitarFechaTorneo(Scanner scanner) {
        logger.log(Level.INFO, "Fecha del Torneo (YYYY-MM-DD): ");
        return scanner.nextLine();
    }

    private List<Judoka> solicitarParticipantes(Scanner scanner) {
        logger.log(Level.INFO, "Ingrese los nombres de los participantes (separados por comas): ");
        String participantesInput = scanner.nextLine();
        List<String> nombresParticipantes = Arrays.asList(participantesInput.split(","));
        List<Judoka> participantes = new ArrayList<>();

        for (String nombreParticipante : nombresParticipantes) {
            Judoka judoka = gestorJudokas.obtenerJudoka(nombreParticipante.trim());
            if (judoka != null) {
                participantes.add(judoka);
            } else {
                logger.log(Level.WARNING, "Judoka no encontrado: {0}", nombreParticipante.trim());
            }
        }
        return participantes;
    }

    /**
     * Registra al ganador de un torneo desde la consola.
     *
     * @param scanner el escáner para entrada de datos
     */
    public void registrarGanadorDesdeConsola(Scanner scanner) {
        logger.log(Level.INFO, "Nombre del Torneo: ");
        String nombreTorneo = scanner.nextLine();
        logger.log(Level.INFO, "Nombre del Ganador: ");
        String nombreGanador = scanner.nextLine();

        for (Torneo torneo : torneos) {
            if (torneo.getNombre().equalsIgnoreCase(nombreTorneo)) {
                torneo.registrarGanador(nombreGanador);
                logger.log(Level.INFO, "Ganador registrado");
                return;
            }
        }
        logger.log(Level.INFO, "Torneo no encontrado");
    }
}
