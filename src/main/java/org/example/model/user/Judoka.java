package org.example.model.user;

import jakarta.persistence.*;
import lombok.*;

import java.util.Locale;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Judoka {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nombre;
    private String apellido;
    private String username;
    private String password;
    private String categoria;
    private int victorias;
    private int derrotas;
    private int empates;
    private String fechaNacimiento;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;



    /**
     * Instantiates a new Judoka.
     *
     * @param nombre          the nombre
     * @param apellido        the apellido
     * @param categoria       the categoria
     * @param fechaNacimiento the fecha nacimiento
     */
    public Judoka(String nombre, String apellido, String categoria, String fechaNacimiento) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.categoria = categoria;
        this.fechaNacimiento = fechaNacimiento;
    }

    /**
     * Aumentar victoria.
     */
    public void aumentarVictoria() { victorias++; }

    /**
     * Aumentar derrota.
     */
    public void aumentarDerrota() { derrotas++; }

    /**
     * Aumentar empate.
     */
    public void aumentarEmpate() { empates++; }

    /**
     * Calcular porcentaje victorias double.
     *
     * @return the double
     */
    public double calcularPorcentajeVictorias() {
        int total = victorias + derrotas + empates;
        return total == 0 ? 0 : (victorias * 100.0) / total;
    }

    /**
     * Mostrar informacion string.
     *
     * @return the string
     */
    public String mostrarInformacion() {
        return "Nombre: " + nombre + " " + apellido + ", Categoria: " + categoria +
                ", Victorias: " + victorias + ", Derrotas: " + derrotas + ", Empates: " + empates +
                ", % Victorias: " + String.format(Locale.US, "%.2f", calcularPorcentajeVictorias());
    }

}
