package org.example.model;

import org.example.model.gestor.GestorJudokas;
import org.example.model.gestor.GestionarTorneo;
import org.example.model.logger.LoggerManager;
import org.example.model.ranking.EstadisticasRanking;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = LoggerManager.getLogger(Main.class);
    public static void main(String[] args) {
        GestorJudokas gestionJudokas = new GestorJudokas();

        try {
            gestionJudokas.cargarJudokassDesdeCSV("judokas.csv");
        } catch (IOException e) {
            logger.log(Level.SEVERE, String.format("Error al cargar judokas desde CSV: %s", e.getMessage()), e);
        }

        GestionarTorneo gestionarTorneo = new GestionarTorneo(gestionJudokas);
        EstadisticasRanking estadisticasRanking = new EstadisticasRanking(gestionJudokas);

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            logger.log(Level.INFO,"\n=== Sistema de Gestión de Judo ===");
            logger.log(Level.INFO,"1. Agregar Judoka");
            logger.log(Level.INFO,"2. Mostrar Judokass");
            logger.log(Level.INFO,"3. Registrar Resultado de Judoka");
            logger.log(Level.INFO,"4. Agregar Competencia");
            logger.log(Level.INFO,"5. Registrar Ganador de Competencia");
            logger.log(Level.INFO,"6. Mostrar Estadisticas de un Judoka");
            logger.log(Level.INFO,"7. Mostrar Ranking");
            logger.log(Level.INFO,"8. Salir");
            logger.log(Level.INFO,"Seleccione una opcion: ");
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    gestionJudokas.agregarJudokaDesdeConsola(scanner);
                    break;
                case 2:
                    gestionJudokas.mostrarJudokas();
                    break;
                case 3:
                    gestionJudokas.registrarResultadoJudoka(scanner);
                    break;
                case 4:
                    gestionarTorneo.agregarTorneoDesdeConsola(scanner);
                    break;
                case 5:
                    gestionarTorneo.registrarGanadorDesdeConsola(scanner);
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
