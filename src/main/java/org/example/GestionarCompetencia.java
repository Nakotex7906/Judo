package org.example;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class GestionarCompetencia {

    private List<Competencia> competencias;

    public GestionarCompetencia() {
        competencias = new ArrayList<>();
    }

    public List<Competencia> getCompetencias() {
        return competencias;
    }

    public void agregarCompetenciaDesdeConsola(Scanner scanner) {
        System.out.print("Nombre de la Competencia: ");
        String nombre = scanner.nextLine();
        System.out.print("Fecha de la Competencia (YYYY-MM-DD): ");
        String fecha = scanner.nextLine();

        System.out.println("Ingrese los nombres de los participantes (separados por comas): ");
        String participantesInput = scanner.nextLine();
        List<String> participantes = Arrays.asList(participantesInput.split(","));

        Competencia competencia = new Competencia(nombre, fecha, participantes);
        competencias.add(competencia);
        System.out.println("Competencia agregada con exito.");
    }

    public void registrarGanadorDesdeConsola(Scanner scanner) {
        System.out.print("Nombre de la Competencia: ");
        String nombreCompetencia = scanner.nextLine();
        System.out.print("Nombre del Ganador: ");
        String nombreGanador = scanner.nextLine();

        for (Competencia competencia : competencias) {
            if (competencia.getNombre().equalsIgnoreCase(nombreCompetencia)) {
                competencia.registrarGanador(nombreGanador);
                System.out.println("Ganador registrado");
                return;
            }
        }
        System.out.println("Competencia no encontrada");
    }

}