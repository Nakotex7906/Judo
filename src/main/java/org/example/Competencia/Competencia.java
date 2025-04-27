package org.example.Competencia;

import org.example.Atleta.Atleta;
import org.example.Atleta.GestionAtletas;
import org.example.Example.LoggerManager;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Competencia {
    private String nombre;
    private String fecha;
    private List<Atleta> participantes;
    private Atleta ganador;

    private static final Logger logger = LoggerManager.getLogger(Competencia.class);
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
        logger.log(Level.INFO,"El atleta no esta inscrito en esta competencia");
    }
}