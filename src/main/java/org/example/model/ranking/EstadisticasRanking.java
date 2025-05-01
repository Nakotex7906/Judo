package org.example.model.ranking;

import org.example.model.judoka.Judoka;
import org.example.model.judoka.GestionJudokas;
import org.example.model.example.LoggerManager;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EstadisticasRanking {

    private static final Logger logger = LoggerManager.getLogger(EstadisticasRanking.class);

    private GestionJudokas gestorAtletas;

    public EstadisticasRanking(GestionJudokas gestorAtletas) {
        this.gestorAtletas = gestorAtletas;
    }

    public void mostrarEstadisticasDesdeConsola(Scanner scanner) {
        logger.log(Level.INFO,"Nombre del Atleta: ");
        String nombre = scanner.nextLine();

        Judoka atleta = gestorAtletas.obtenerAtleta(nombre);
        if (atleta != null) {
            if (logger.isLoggable(Level.INFO)) {
                logger.log(Level.INFO, atleta.mostrarInformacion());
            }
        } else {
            logger.log(Level.INFO,"El atleta no esta registrado.");
        }
    }

    public void calcularRanking() {
        List<Judoka> atletas = gestorAtletas.getListaAtletas();

        atletas.sort(Comparator.comparingInt(Judoka::getVictorias).reversed());

        logger.log(Level.INFO,"Ranking de Atletas (por numero de victorias):");
        for (Judoka a : atletas) {
            logger.log(Level.INFO, "{0} {1} - Victorias: {2}, Derrotas: {3}, Empates: {4}",
         new Object[]{a.getNombre(), a.getApellido(), a.getVictorias(), a.getDerrotas(), a.getEmpates()});
        }
    }
}