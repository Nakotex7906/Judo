package org.example;

public class Atleta {
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
        this.victorias = 0;
        this.derrotas = 0;
        this.empates = 0;
    }

    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getCategoria() { return categoria; }
    public int getVictorias() { return victorias; }
    public int getDerrotas() { return derrotas; }
    public int getEmpates() { return empates; }
    public String getFechaNacimiento() { return fechaNacimiento; }

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
                ", % Victorias: " + String.format("%.2f", calcularPorcentajeVictorias());
    }
}
