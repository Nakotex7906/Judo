package org.example.model.competencia;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.model.atleta.Atleta;
import org.example.model.example.LoggerManager;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Competencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nombre;
    private String fecha;
    @ManyToMany
    @JoinTable(
            name = "competencia_atleta",
            joinColumns = @JoinColumn(name = "competencia_id"),
            inverseJoinColumns = @JoinColumn(name = "atleta_id")
    )
    private List<Atleta> participantes;

    @ManyToOne
    @JoinColumn(name = "ganador_id")
    @JsonBackReference // Sirve para que no sea infinito
    private Atleta ganador;

    private static final Logger logger = LoggerManager.getLogger(Competencia.class);
    public Competencia(String nombre, String fecha, List<Atleta> participantes) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.participantes = participantes;
    }

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