package org.example.Ranking;

import org.example.Atleta.Atleta;
import org.example.Atleta.GestionAtletas;
import org.example.Example.LoggerManager;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EstadisticasRanking {

    private static final Logger logger = LoggerManager.getLogger(EstadisticasRanking.class);

    private GestionAtletas gestorAtletas;

    public EstadisticasRanking(GestionAtletas gestorAtletas) {
        this.gestorAtletas = gestorAtletas;
    }

    public void mostrarEstadisticasDesdeConsola(Scanner scanner) {
        logger.log(Level.INFO,"Nombre del Atleta: ");
        String nombre = scanner.nextLine();

        Atleta atleta = gestorAtletas.obtenerAtleta(nombre);
        if (atleta != null) {
            logger.log(Level.INFO,atleta.mostrarInformacion());
        } else {
            logger.log(Level.INFO,"El atleta no esta registrado.");
        }
    }

    public void calcularRanking() {
        List<Atleta> atletas = gestorAtletas.getListaAtletas();

        atletas.sort(Comparator.comparingInt(Atleta::getVictorias).reversed());

        logger.log(Level.INFO,"Ranking de Atletas (por numero de victorias):");
        for (Atleta a : atletas) {
            logger.log(Level.INFO,a.getNombre() + " " + a.getApellido() + " - Victorias: " + a.getVictorias() +
                    ", Derrotas: " + a.getDerrotas() + ", Empates: " + a.getEmpates());
        }
    }
}
