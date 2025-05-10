package org.example.model.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String sensei;
    private String ubicacion;
    private String horarios;
    private LocalDate fechaCreacion;

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    private List<Judoka> integrantes;


    public Club(String nombre, String sensei, String ubicacion, String horarios, LocalDate fechaCreacion) {
        this.nombre = nombre;
        this.sensei = sensei;
        this.ubicacion = ubicacion;
        this.horarios = horarios;
        this.fechaCreacion = fechaCreacion;
    }
    }