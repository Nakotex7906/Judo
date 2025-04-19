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

    public void registrarGanador(String nombreAtleta) {
        if (participantes.contains(nombreAtleta)) {
            this.ganador = nombreAtleta;
        } else {
            System.out.println("El atleta no esta inscrito en esta competencia.");
        }
    }

}