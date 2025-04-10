package org;

public class AtletaEstadisticas {
    private String nombreAtleta;
    private int victorias;
    private int derrotas;
    private int empates;

    public AtletaEstadisticas(String nombreAtleta) {
        this.nombreAtleta = nombreAtleta;
        this.victorias = 0;
        this.derrotas = 0;
        this.empates = 0;
    }

    public void incrementarVictorias() {
        this.victorias++;
    }

    public void incrementarDerrotas() {
        this.derrotas++;
    }

    public void incrementarEmpates() {
        this.empates++;
    }

    public int getVictorias() {
        return victorias;
    }

    public int getDerrotas() {
        return derrotas;
    }

    public int getEmpates() {
        return empates;
    }

    public String mostrarInformacion() {
        return "Atleta: " + nombreAtleta + " | Victorias: " + victorias + " | Derrotas: " + derrotas + " | Empates: " + empates;
    }
}

