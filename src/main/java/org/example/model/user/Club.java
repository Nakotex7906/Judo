package org.example.model.user;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa un club de judo en el sistema.
 * <p>
 * Cada club puede tener múltiples judokas asociados.
 * La clase almacena información básica como nombre, sensei, dirección,
 * horarios, año de fundación y una breve descripción.
 * </p>
 *
 * Esta clase se relaciona con la entidad {@link Judoka} mediante una relación
 * {@code OneToMany} bidireccional.
 *
 * @author Alonso Romero, Ignacio Essus
 */
@Entity
@Data
public class Club {

    /** Identificador único del club (clave primaria autogenerada). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre de usuario del club, generalmente un correo electrónico. */
    private String username;

    /** Contraseña del club. */
    private String password;

    /** Nombre oficial del club. */
    private String nombre;

    /** Nombre del sensei a cargo del club. */
    private String sensei;

    /** Año de fundación del club (en formato de texto, ej. "2005"). */
    private String anoFundacion;

    /** Dirección física del club. */
    private String direccion;

    /** Horarios de funcionamiento o entrenamiento. */
    private String horarios;

    /** Descripción general del club (hasta 1000 caracteres). */
    @Column(length = 1000)
    private String descripcion;

    /**
     * Lista de judokas asociados al club.
     * <p>
     * Relación uno a muchos. Los judokas son eliminados si el club se elimina.
     * </p>
     */
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Judoka> judokas = new ArrayList<>();

    /**
     * Agrega un judoka al club y establece la relación inversa.
     *
     * @param judoka el judoka a agregar
     */
    public void agregarJudoka(Judoka judoka) {
        judokas.add(judoka);
        judoka.setClub(this);
    }

    /**
     * Elimina un judoka del club y rompe la relación inversa.
     *
     * @param judoka el judoka a eliminar
     */
    public void eliminarJudoka(Judoka judoka) {
        judokas.remove(judoka);
        judoka.setClub(null);
    }

    /**
     * Retorna un resumen textual con la información clave del club.
     *
     * @return cadena con nombre, sensei, año, dirección, horarios y cantidad de judokas
     */
    public String mostrarInformacion() {
        return "Club: " + nombre +
                ", Sensei: " + sensei +
                ", Año Fundación: " + anoFundacion +
                ", Dirección: " + direccion +
                ", Horarios: " + horarios +
                ", Judokas inscritos: " + judokas.size();
    }
}
