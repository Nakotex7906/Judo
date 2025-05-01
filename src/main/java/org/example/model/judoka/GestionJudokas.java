package org.example.model.judoka;

import org.example.model.example.LoggerManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GestionJudokas {

    private List<Judoka> listaAtletas;

    public GestionJudokas() {
        listaAtletas = new ArrayList<>();
    }

    private static final Logger logger = LoggerManager.getLogger(GestionJudokas.class);

    public void agregarAtletaDesdeConsola(Scanner scanner) {
        try {
            logger.log(Level.INFO, "Nombre: ");
            String nombre = scanner.nextLine();
            logger.log(Level.INFO, "Apellido: ");
            String apellido = scanner.nextLine();
            logger.log(Level.INFO, "Categoría: ");
            String categoria = scanner.nextLine();
            logger.log(Level.INFO, "Fecha de Nacimiento (DD-MM-YYYY): ");
            String fechaNacimiento = scanner.nextLine();

            Judoka atleta = new Judoka(nombre, apellido, categoria, fechaNacimiento);
            if (existeAtleta(nombre)) {
                throw new IllegalArgumentException("El atleta " + nombre + " ya está registrado");
            }

            listaAtletas.add(atleta);
            guardarAtletasCSV("atletas.csv");
            logger.log(Level.INFO, "Atleta agregado con éxito");

        } catch (IllegalArgumentException e) {
            logger.log(Level.WARNING, e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, String.format("Error inesperado al agregar atleta: %s", e.getMessage()), e);
        }
    }

    public void cargarAtletasDesdeCSV(String rutaArchivo) throws IOException {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            if (logger.isLoggable(Level.INFO)) {
                logger.log(Level.INFO, String.format("Archivo %s no encontrado", rutaArchivo));
            }
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");

                if (datos.length == 7) {
                    String nombre = datos[0];
                    String apellido = datos[1];
                    String categoria = datos[2];
                    int victorias = Integer.parseInt(datos[3]);
                    int derrotas = Integer.parseInt(datos[4]);
                    int empates = Integer.parseInt(datos[5]);
                    String fechaNacimiento = datos[6];

                    Judoka atleta = new Judoka(nombre, apellido, categoria, fechaNacimiento);
                    atleta.setVictorias(victorias);
                    atleta.setDerrotas(derrotas);
                    atleta.setEmpates(empates);

                    if (!existeAtleta(nombre)) {
                        listaAtletas.add(atleta);
                    }
                }
            }
            logger.log(Level.INFO, "Atletas cargados correctamente desde {0}", rutaArchivo);
        } catch (NumberFormatException e) {
            throw new IOException("Error en el formato " + rutaArchivo, e);
        }
    }

    public void registrarResultadoAtleta(Scanner scanner) {
        try {
            logger.log(Level.INFO, "Nombre del Atleta: ");
            String nombre = scanner.nextLine();
            logger.log(Level.INFO, "Resultado (victoria / derrota / empate): ");
            String resultado = scanner.nextLine();

            Judoka atleta = obtenerAtleta(nombre);

            switch (resultado.toLowerCase()) {
                case "victoria":
                    atleta.aumentarVictoria();
                    break;
                case "derrota":
                    atleta.aumentarDerrota();
                    break;
                case "empate":
                    atleta.aumentarEmpate();
                    break;
                default:
                    throw new IllegalArgumentException("Resultado no válido: " + resultado);
            }

            guardarAtletasCSV("atletas.csv");
            logger.log(Level.INFO, "Resultado actualizado");

        } catch (IllegalArgumentException e) {
            logger.log(Level.WARNING, e.getMessage());
        } catch (Exception e) {
            if (logger.isLoggable(Level.SEVERE)) {
                logger.log(Level.SEVERE, String.format("Error inesperado al registrar resultado: %s", e.getMessage()), e);
            }
        }
    }

    public List<Judoka> getListaAtletas() {
        return new ArrayList<>(listaAtletas);
    }

    public Judoka obtenerAtleta(String nombre) throws IllegalArgumentException {
        for (Judoka atleta : listaAtletas) {
            if (atleta.getNombre().equalsIgnoreCase(nombre)) {
                return atleta;
            }
        }
        throw new IllegalArgumentException("Atleta " + nombre + " no encontrado");
    }

    private boolean existeAtleta(String nombre) {
        for (Judoka atleta : listaAtletas) {
            if (atleta.getNombre().equalsIgnoreCase(nombre)) {
                return true;
            }
        }
        return false;
    }

    public void mostrarAtletas() {
        if (listaAtletas.isEmpty()) {
            logger.log(Level.INFO, "No hay atletas registrados");
        } else {
            listaAtletas.forEach(atleta -> logger.log(Level.INFO,atleta.mostrarInformacion()));
        }
    }

    public void guardarAtletasCSV(String rutaArchivo) throws IOException {
        try (FileWriter fw = new FileWriter(rutaArchivo)) {
            for (Judoka atleta : listaAtletas) {
                fw.write(atleta.getNombre() + "," + atleta.getApellido() + "," + atleta.getCategoria() + "," +
                        atleta.getVictorias() + "," + atleta.getDerrotas() + "," + atleta.getEmpates() + "," +
                        atleta.getFechaNacimiento() + "\n");
            }
            logger.log(Level.INFO, "Datos de atletas guardados en {0}", rutaArchivo);
        }
    }
}