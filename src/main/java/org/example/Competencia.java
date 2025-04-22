package org.example;

import java.util.List;

public class Competencia {
    private String nombre;
    private String fecha;
    private List<Atleta> participantes;
    private Atleta ganador;

    public Competencia(String nombre, String fecha, List<Atleta> participantes) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.participantes = participantes;
    }

    public String getNombre() { return nombre; }

    public String getFecha() { return fecha; }

    public List<Atleta> getParticipantes() { return participantes; }

    public Atleta getGanador() { return ganador; }

    public void registrarGanador(String nombreAtleta) {
        for (Atleta atleta : participantes) {
            if (atleta.getNombre().equalsIgnoreCase(nombreAtleta)) {
                this.ganador = atleta;
                return;
            }
        }
        System.out.println("El atleta no esta inscrito en esta competencia");
    }
}