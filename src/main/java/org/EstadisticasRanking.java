package org;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EstadisticasRanking {

    private Map<String, AtletaEstadisticas> estadisticasAtletas;

    public EstadisticasRanking() {
        estadisticasAtletas = new HashMap<>();
    }

    public void agregarAtleta(String nombreAtleta) {
        if (!estadisticasAtletas.containsKey(nombreAtleta)) {
            estadisticasAtletas.put(nombreAtleta, new AtletaEstadisticas(nombreAtleta));
        }
    }

    public void registrarVictoria(String nombreAtleta) {
        if (estadisticasAtletas.containsKey(nombreAtleta)) {
            estadisticasAtletas.get(nombreAtleta).incrementarVictorias();
        } else {
            System.out.println("El atleta no está registrado.");
        }
    }

    public void registrarDerrota(String nombreAtleta) {
        if (estadisticasAtletas.containsKey(nombreAtleta)) {
            estadisticasAtletas.get(nombreAtleta).incrementarDerrotas();
        } else {
            System.out.println("El atleta no está registrado.");
        }
    }

    public void registrarEmpate(String nombreAtleta) {
        if (estadisticasAtletas.containsKey(nombreAtleta)) {
            estadisticasAtletas.get(nombreAtleta).incrementarEmpates();
        } else {
            System.out.println("El atleta no está registrado.");
        }
    }

    // Calcular el ranking de los atletas basado en victorias (de mayor a menor)
    public void calcularRanking() {
        List<Map.Entry<String, AtletaEstadisticas>> listaAtletas = new ArrayList<>(estadisticasAtletas.entrySet());

        // Ordenar a los atletas por victorias en orden descendente
        listaAtletas.sort((entry1, entry2)
                -> Integer.compare(entry2.getValue().getVictorias(), entry1.getValue().getVictorias()));

        System.out.println("Ranking de Atletas (por número de victorias):");
        for (Map.Entry<String, AtletaEstadisticas> entry : listaAtletas) {
            String nombreAtleta = entry.getKey();
            AtletaEstadisticas stats = entry.getValue();
            System.out.println(nombreAtleta + " - Victorias: " + stats.getVictorias() + ", Derrotas: "
                    + stats.getDerrotas() + ", Empates: " + stats.getEmpates());
        }
    }

    public void mostrarEstadisticas(String nombreAtleta) {
        if (estadisticasAtletas.containsKey(nombreAtleta)) {
            AtletaEstadisticas stats = estadisticasAtletas.get(nombreAtleta);
            System.out.println(stats.mostrarInformacion());
        } else {
            System.out.println("El atleta no está registrado.");
        }
    }

}
