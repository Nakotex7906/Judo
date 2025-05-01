package org.example.model.competencia;
import org.example.model.atleta.Atleta;
import org.example.model.atleta.GestionAtletas;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.logging.Level;
import org.example.model.example.LoggerManager;


public class GestionarCompetencia {

    private static final Logger logger = LoggerManager.getLogger(GestionarCompetencia.class);
    private List<Competencia> competencias;
    private GestionAtletas gestionAtletas;

    public GestionarCompetencia(GestionAtletas gestionAtletas) {
        this.gestionAtletas = gestionAtletas;
        competencias = new ArrayList<>();
    }

    public List<Competencia> getCompetencias() {
        return competencias;
    }

    public void agregarCompetenciaDesdeConsola(Scanner scanner) {
        logger.log(Level.INFO,"Nombre de la Competencia: ");
        String nombre = scanner.nextLine();
        logger.log(Level.INFO,"Fecha de la Competencia (YYYY-MM-DD): ");
        String fecha = scanner.nextLine();

        logger.log(Level.INFO,"Ingrese los nombres de los participantes (separados por comas): ");
        String participantesInput = scanner.nextLine();
        List<String> nombresParticipantes = Arrays.asList(participantesInput.split(","));
        List<Atleta> participantes = new ArrayList<>();

        for (String nombreParticipante : nombresParticipantes) {
            Atleta atleta = gestionAtletas.obtenerAtleta(nombreParticipante.trim());
            if (atleta != null) {
                participantes.add(atleta);
            } else {
                logger.log(Level.WARNING, "Atleta no encontrado: {0}", nombreParticipante.trim());
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