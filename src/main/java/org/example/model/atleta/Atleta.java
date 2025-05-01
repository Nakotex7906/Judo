package org.example.model.atleta;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Locale;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Atleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nombre;
    private String apellido;
    private String categoria;
    private int victorias;
    private int derrotas;
    private int empates;
    private String fechaNacimiento;

    public Atleta(String nombre, String apellido, String categoria, String fechaNacimiento) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.categoria = categoria;
        this.fechaNacimiento = fechaNacimiento;
    }

    public void aumentarVictoria() { victorias++; }
    public void aumentarDerrota() { derrotas++; }
    public void aumentarEmpate() { empates++; }

    public double calcularPorcentajeVictorias() {
        int total = victorias + derrotas + empates;
        return total == 0 ? 0 : (victorias * 100.0) / total;
    }

    public String mostrarInformacion() {
        return "Nombre: " + nombre + " " + apellido + ", Categoria: " + categoria +
                ", Victorias: " + victorias + ", Derrotas: " + derrotas + ", Empates: " + empates +
                ", % Victorias: " + String.format(Locale.US, "%.2f", calcularPorcentajeVictorias());
    }
}
