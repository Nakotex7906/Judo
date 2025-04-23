package org.example.Competencia;
import org.example.Atleta.Atleta;
import org.example.Atleta.GestionAtletas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class GestionarCompetencia {

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
        System.out.print("Nombre de la Competencia: ");
        String nombre = scanner.nextLine();
        System.out.print("Fecha de la Competencia (YYYY-MM-DD): ");
        String fecha = scanner.nextLine();

        System.out.println("Ingrese los nombres de los participantes (separados por comas): ");
        String participantesInput = scanner.nextLine();
        List<String> nombresParticipantes = Arrays.asList(participantesInput.split(","));
        List<Atleta> participantes = new ArrayList<>();

        for (String nombreParticipante : nombresParticipantes) {
            Atleta atleta = gestionAtletas.obtenerAtleta(nombreParticipante.trim());
            if (atleta != null) {
                participantes.add(atleta);
            } else {
                System.out.println("Atleta no encontrado: " + nombreParticipante.trim());
            }
        }

        if (participantes.isEmpty()) {
            System.out.println("No se agregaron participantes validos, competencia no creada");
            return;
        }

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