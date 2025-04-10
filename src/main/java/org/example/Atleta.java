package org.example;
public class Atleta {
    private String nombre;
    private String apellido;
    private String categoria; // Ejemplo: peso ligero, peso medio, etc.
    private int victorias;
    private int derrotas;
    private int empates;
    private String fechaNacimiento;

    // Constructor
    public Atleta(String nombre,String apellido, String categoria, String fechaNacimiento) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.fechaNacimiento = fechaNacimiento;
        this.victorias = 0;
        this.derrotas = 0;
        this.empates = 0;
    }

    public String getNombre() { return nombre; }
    public String getApellido() {return apellido;}
    public String getCategoria() { return categoria; }
    public int getVictorias() { return victorias; }
    public int getDerrotas() { return derrotas; }
    public int getEmpates() { return empates; }
    public String getFechaNacimiento() { return fechaNacimiento; }

    public void registrarVictoria() { victorias++; }
    public void registrarDerrota() { derrotas++; }
    public void registrarEmpate() { empates++; }

    public String mostrarInformacion() {
        return "Nombre: " + nombre + ", Categoria: " + categoria + ", Victorias: " + victorias + ", Derrotas: " + derrotas + ", Empates: " + empates;
    }
}
