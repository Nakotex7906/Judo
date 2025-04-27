package org.example;

import org.example.Atleta.GestionAtletas;
import org.example.Competencia.GestionarCompetencia;
import org.example.Example.LoggerManager;
import org.example.Ranking.EstadisticasRanking;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = LoggerManager.getLogger(Main.class);
    public static void main(String[] args) {
        GestionAtletas gestionAtletas = new GestionAtletas();
        gestionAtletas.cargarAtletasDesdeCSV("atletas.csv");
        GestionarCompetencia gestionarCompetencia = new GestionarCompetencia(gestionAtletas);
        EstadisticasRanking estadisticasRanking = new EstadisticasRanking(gestionAtletas);

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            logger.log(Level.INFO,"\n=== Sistema de Gestión de Judo ===");
            logger.log(Level.INFO,"1. Agregar Atleta");
            logger.log(Level.INFO,"2. Mostrar Atletas");
            logger.log(Level.INFO,"3. Guardar Atletas en un Archivo CSV");
            logger.log(Level.INFO,"4. Registrar Resultado de Atleta");
            logger.log(Level.INFO,"5. Agregar Competencia");
            logger.log(Level.INFO,"6. Registrar Ganador de Competencia");
            logger.log(Level.INFO,"7. Mostrar Estadisticas de un Atleta");
            logger.log(Level.INFO,"8. Mostrar Ranking");
            logger.log(Level.INFO,"9. Salir");
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
                    gestionAtletas.guardarAtletasCSV("atletas.csv");
                    break;
                case 4:
                    gestionAtletas.registrarResultadoAtleta(scanner);
                    break;
                case 5:
                    gestionarCompetencia.agregarCompetenciaDesdeConsola(scanner);
                    break;
                case 6:
                    gestionarCompetencia.registrarGanadorDesdeConsola(scanner);
                    break;
                case 7:
                    estadisticasRanking.mostrarEstadisticasDesdeConsola(scanner);
                    break;
                case 8:
                    estadisticasRanking.calcularRanking();
                    break;
                case 9:
                    logger.log(Level.INFO,"Saliendo del sistema");
                    break;
                default:
                    logger.log(Level.INFO,"Opcion no valida, intente nuevamente");
            }
        } while (opcion != 9);

        scanner.close();
    }
}