package org.example;
import java.util.ArrayList;
import java.util.List;

public class GestionarCompetencia {
    private List<Competencia> competencias;

    public GestionarCompetencia() {
        competencias = new ArrayList<>();
    }

    public void agregarCompetencia(Competencia competencia) {
        competencias.add(competencia);
    }

    public void registrarGanador(String nombreCompetencia, Atleta atleta) {
        for (Competencia competencia : competencias) {
            if (competencia.getNombre().equalsIgnoreCase(nombreCompetencia)) {
                competencia.registrarGanador(atleta);
                break;
            }
        }
    }

    public void mostrarCompetencias() {
        for (Competencia competencia : competencias) {
            competencia.mostrarCompetencia();
        }
    }
}