package org.example.Ranking;

import org.example.Atleta.Atleta;
import org.example.Atleta.GestionAtletas;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class EstadisticasRanking {

    private GestionAtletas gestorAtletas;

    public EstadisticasRanking(GestionAtletas gestorAtletas) {
        this.gestorAtletas = gestorAtletas;
    }

    public void mostrarEstadisticasDesdeConsola(Scanner scanner) {
        System.out.print("Nombre del Atleta: ");
        String nombre = scanner.nextLine();

        Atleta atleta = gestorAtletas.obtenerAtleta(nombre);
        if (atleta != null) {
            System.out.println(atleta.mostrarInformacion());
        } else {
            System.out.println("El atleta no esta registrado.");
        }
    }

    public void calcularRanking() {
        List<Atleta> atletas = gestorAtletas.getListaAtletas();

        atletas.sort(Comparator.comparingInt(Atleta::getVictorias).reversed());

        System.out.println("Ranking de Atletas (por numero de victorias):");
        for (Atleta a : atletas) {
            System.out.println(a.getNombre() + " " + a.getApellido() + " - Victorias: " + a.getVictorias() +
                    ", Derrotas: " + a.getDerrotas() + ", Empates: " + a.getEmpates());
        }
    }
}
