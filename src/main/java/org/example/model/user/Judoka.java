package org.example.model.user;

import jakarta.persistence.*;
import lombok.*;

import java.util.Locale;

/**
 * Entidad que representa a un judoka (usuario practicante de judo) dentro del sistema.
 * <p>
 * Contiene datos personales, deportivos y relacionales con un club, incluyendo estadísticas
 * como victorias, derrotas y empates.
 * </p>
 *
 * Relación: muchos judokas pueden pertenecer a un solo {@link Club}.
 *
 * @author Ignacio Essus, Alonso Romero, Benjamin Beroiza
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Judoka {

    /** Identificador único del judoka (clave primaria autogenerada). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /** Nombre del judoka. */
    private String nombre;

    /** Apellido del judoka. */
    private String apellido;

    /** Nombre de usuario o correo electrónico para autenticación. */
    private String username;

    /** Contraseña del judoka. */
    private String password;

    /** Categoría de peso (ej. -60 kg, -81 kg, etc.). */
    private String categoria;

    /** Cantidad de combates ganados. */
    private int victorias;

    /** Cantidad de combates perdidos. */
    private int derrotas;

    /** Cantidad de combates empatados. */
    private int empates;

    /** Fecha de nacimiento del judoka en formato texto (ej. "2001-05-12"). */
    private String fechaNacimiento;

    /** Ocupación o profesión del judoka. */
    private String oficio;

    /** Años de experiencia en entrenamiento. */
    private Integer aniosEntrenamiento;

    /** Descripción libre sobre el judoka (máximo 1000 caracteres). */
    @Column(length = 1000)
    private String descripcion;

    /** Club al que pertenece el judoka (relación muchos a uno). */
    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;

    /**
     * Constructor parcial para crear un judoka con información básica.
     *
     * @param id              identificador del judoka
     * @param nombre          nombre del judoka
     * @param apellido        apellido del judoka
     * @param categoria       categoría de peso
     * @param fechaNacimiento fecha de nacimiento
     */
    public Judoka(long id, String nombre, String apellido, String categoria, String fechaNacimiento) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.categoria = categoria;
        this.fechaNacimiento = fechaNacimiento;
    }

    /**
     * Incrementa en 1 el contador de victorias.
     */
    public void aumentarVictoria() {
        victorias++;
    }

    /**
     * Incrementa en 1 el contador de derrotas.
     */
    public void aumentarDerrota() {
        derrotas++;
    }

    /**
     * Incrementa en 1 el contador de empates.
     */
    public void aumentarEmpate() {
        empates++;
    }

    /**
     * Calcula el porcentaje de victorias del judoka respecto al total de combates.
     *
     * @return porcentaje de victorias (entre 0 y 100)
     */
    public double calcularPorcentajeVictorias() {
        int total = victorias + derrotas + empates;
        return total == 0 ? 0 : (victorias * 100.0) / total;
    }

    /**
     * Muestra una descripción resumen del judoka con estadísticas y categoría.
     *
     * @return cadena con nombre, categoría y estadísticas
     */
    public String mostrarInformacion() {
        return "Nombre: " + nombre + " " + apellido + ", Categoria: " + categoria +
                ", Victorias: " + victorias + ", Derrotas: " + derrotas + ", Empates: " + empates +
                ", % Victorias: " + String.format(Locale.US, "%.2f", calcularPorcentajeVictorias());
    }
}
