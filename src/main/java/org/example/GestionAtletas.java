package org.example;

import java.util.ArrayList;
import java.util.List;

public class GestionAtletas {

    private List<Atleta> listaAtletas;

    public GestionAtletas() {
        listaAtletas = new ArrayList<>();
    }

    public void agregarAtleta(Atleta atleta) {
        listaAtletas.add(atleta);
    }

    // Leer información de un atleta (por nombre)
    public Atleta obtenerAtleta(String nombre) {
        for (Atleta atleta : listaAtletas) {
            if (atleta.getNombre().equalsIgnoreCase(nombre)) {
                return atleta;
            }
        }
        return null;  // Retorna null si no encuentra al atleta
    }

    // Actualizar estadísticas de un atleta
    public void actualizarAtleta(String nombre, String resultado) {
        Atleta atleta = obtenerAtleta(nombre);
        if (atleta != null) {
            switch (resultado.toLowerCase()) {
                case "victoria":
                    atleta.registrarVictoria();
                    break;
                case "derrota":
                    atleta.registrarDerrota();
                    break;
                case "empate":
                    atleta.registrarEmpate();
                    break;
                default:
                    System.out.println("Resultado no válido.");
            }
        }
    }

    public boolean eliminarAtleta(String nombre) {
        Atleta atleta = obtenerAtleta(nombre);
        if (atleta != null) {
            listaAtletas.remove(atleta);
            return true;
        }
        return false;  // Si no se encuentra al atleta, no se elimina
    }

    public void mostrarAtletas() {
        for (Atleta atleta : listaAtletas) {
            System.out.println(atleta.mostrarInformacion());
        }
    }

}
