package org.example;

import java.util.List;

public class Competencia {
    private String nombre;
    private String fecha;
    private List<String> participantes;
    private String ganador;

    public Competencia(String nombre, String fecha, List<String> participantes) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.participantes = participantes;
    }

    public String getNombre() {return nombre;}
    public String getFecha() {return fecha;}
    public List<String> getParticipantes() {return participantes;}
    public String getGanador() {return ganador;}

    public void registrarGanador(String nombreAtleta) {
        if (participantes.contains(nombreAtleta)) {
            this.ganador = nombreAtleta;
        } else {
            System.out.println("El atleta no esta inscrito en esta competencia.");
        }
    }

    public void mostrarCompetencia() {
        System.out.println("Competencia: " + nombre + " - Fecha: " + fecha);
        System.out.println("Participantes:");
        for (String participante : participantes) {
            System.out.println("- " + participante);
        }
        if (ganador != null) {
            System.out.println("Ganador: " + ganador);
        }
    }
}