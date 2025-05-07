package org.example.model.competencia;
import lombok.Getter;
import org.example.model.judoka.Judoka;
import org.example.model.judoka.GestionJudokas;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.logging.Level;
import org.example.model.example.LoggerManager;

/**
 * The type Gestionar competencia.
 */
@Getter
public class GestionarCompetencia {

    private static final Logger logger = LoggerManager.getLogger(GestionarCompetencia.class);
    private List<Competencia> competencias;
    private GestionJudokas gestionJudokas;

    /**
     * Instantiates a new Gestionar competencia.
     *
     * @param gestionJudokas the gestion judokas
     */
    public GestionarCompetencia(GestionJudokas gestionJudokas) {
        this.gestionJudokas = gestionJudokas;
        competencias = new ArrayList<>();
    }

    /**
     * Agregar competencia desde consola.
     *
     * @param scanner the scanner
     */
    public void agregarCompetenciaDesdeConsola(Scanner scanner) {
        logger.log(Level.INFO,"Nombre de la Competencia: ");
        String nombre = scanner.nextLine();
        logger.log(Level.INFO,"Fecha de la Competencia (YYYY-MM-DD): ");
        String fecha = scanner.nextLine();

        logger.log(Level.INFO,"Ingrese los nombres de los participantes (separados por comas): ");
        String participantesInput = scanner.nextLine();
        List<String> nombresParticipantes = Arrays.asList(participantesInput.split(","));
        List<Judoka> participantes = new ArrayList<>();

        for (String nombreParticipante : nombresParticipantes) {
            Judoka judoka = gestionJudokas.obtenerJudoka(nombreParticipante.trim());
            if (judoka != null) {
                participantes.add(judoka);
            } else {
                logger.log(Level.WARNING, "Judoka no encontrado: {0}", nombreParticipante.trim());
            }
        }

        if (participantes.isEmpty()) {
            logger.log(Level.WARNING,"No se agregaron participantes validos, competencia no creada");
            return;
        }

        Competencia competencia = new Competencia(nombre, fecha, participantes);
        competencias.add(competencia);
        logger.log(Level.INFO,"Competencia agregada con exito.");
    }

    /**
     * Registrar ganador desde consola.
     *
     * @param scanner the scanner
     */
    public void registrarGanadorDesdeConsola(Scanner scanner) {
        logger.log(Level.INFO,"Nombre de la Competencia: ");
        String nombreCompetencia = scanner.nextLine();
        logger.log(Level.INFO,"Nombre del Ganador: ");
        String nombreGanador = scanner.nextLine();

        for (Competencia competencia : competencias) {
            if (competencia.getNombre().equalsIgnoreCase(nombreCompetencia)) {
                competencia.registrarGanador(nombreGanador);
                logger.log(Level.INFO,"Ganador registrado");
                return;

            }
        }
        logger.log(Level.INFO,"Competencia no encontrada");
    }
}