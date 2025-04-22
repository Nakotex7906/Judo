package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        GestionAtletas gestionAtletas = new GestionAtletas();
        gestionAtletas.cargarAtletasDesdeCSV("atletas.csv");
        GestionarCompetencia gestionarCompetencia = new GestionarCompetencia();
        EstadisticasRanking estadisticasRanking = new EstadisticasRanking(gestionAtletas);

        Scanner scanner = new Scanner(System.in);
        int opcion;


        do {
            System.out.println("\n=== Sistema de Gestión de Judo ===");
            System.out.println("1. Agregar Atleta");
            System.out.println("2. Mostrar Atletas");
            System.out.println("3. Guardar Atletas en un Archivo CSV");
            System.out.println("4. Registrar Resultado de Atleta");
            System.out.println("5. Agregar Competencia");
            System.out.println("6. Registrar Ganador de Competencia");
            System.out.println("7. Mostrar Estadisticas de un Atleta");
            System.out.println("8. Mostrar Ranking");
            System.out.println("9. Salir");
            System.out.print("Seleccione una opcion: ");
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
                    System.out.println("Saliendo del sistema");
                    break;
                default:
                    System.out.println("Opcion no valida, intente nuevamente");
            }
        } while (opcion != 9);

        scanner.close();
    }
}