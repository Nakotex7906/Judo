package org.example.model;

import org.example.model.judoka.GestionJudokas;
import org.example.model.competencia.GestionarCompetencia;
import org.example.model.example.LoggerManager;
import org.example.model.ranking.EstadisticasRanking;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = LoggerManager.getLogger(Main.class);
    public static void main(String[] args) {
        GestionJudokas gestionAtletas = new GestionJudokas();

        try {
            gestionAtletas.cargarAtletasDesdeCSV("atletas.csv");
        } catch (IOException e) {
            logger.log(Level.SEVERE, String.format("Error al cargar atletas desde CSV: %s", e.getMessage()), e);
        }

        GestionarCompetencia gestionarCompetencia = new GestionarCompetencia(gestionAtletas);
        EstadisticasRanking estadisticasRanking = new EstadisticasRanking(gestionAtletas);

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            logger.log(Level.INFO,"\n=== Sistema de Gestión de Judo ===");
            logger.log(Level.INFO,"1. Agregar Atleta");
            logger.log(Level.INFO,"2. Mostrar Atletas");
            logger.log(Level.INFO,"3. Registrar Resultado de Atleta");
            logger.log(Level.INFO,"4. Agregar Competencia");
            logger.log(Level.INFO,"5. Registrar Ganador de Competencia");
            logger.log(Level.INFO,"6. Mostrar Estadisticas de un Atleta");
            logger.log(Level.INFO,"7. Mostrar Ranking");
            logger.log(Level.INFO,"8. Salir");
            logger.log(Level.INFO,"Seleccione una opcion: ");
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    gestionAtletas.agregarAtletaDesdeConsola(scanner);
                    break;
                case 2:
                    gestionAtletas.mostrarAtletas();
                    break;
                case 3:
                    gestionAtletas.registrarResultadoAtleta(scanner);
                    break;
                case 4:
                    gestionarCompetencia.agregarCompetenciaDesdeConsola(scanner);
                    break;
                case 5:
                    gestionarCompetencia.registrarGanadorDesdeConsola(scanner);
                    break;
                case 6:
                    estadisticasRanking.mostrarEstadisticasDesdeConsola(scanner);
                    break;
                case 7:
                    estadisticasRanking.calcularRanking();
                    break;
                case 8:
                    logger.log(Level.INFO,"Saliendo del sistema");
                    break;
                default:
                    logger.log(Level.INFO,"Opcion no valida, intente nuevamente");
            }
        } while (opcion != 8);

        scanner.close();
    }
}