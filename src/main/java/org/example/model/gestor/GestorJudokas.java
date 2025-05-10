package org.example.model.gestor;

import lombok.Getter;
import org.example.model.logger.LoggerManager;
import org.example.model.user.Judoka;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * The type Gestion judokas.
 */
@Getter
public class GestorJudokas {

    private List<Judoka> listaJudokas;

    /**
     * Instantiates a new Gestion judokas.
     */
    public GestorJudokas() {
        listaJudokas = new ArrayList<>();
    }

    private static final Logger logger = LoggerManager.getLogger(GestorJudokas.class);

    /**
     * Agregar judoka desde consola.
     *
     * @param scanner the scanner
     */
    public void agregarJudokaDesdeConsola(Scanner scanner) {
        try {
            String nombre = solicitarDato(scanner, "Nombre: ");
            String apellido = solicitarDato(scanner, "Apellido: ");
            String categoria = solicitarDato(scanner, "Categoría: ");
            String fechaNacimiento = solicitarDato(scanner, "Fecha de Nacimiento (DD-MM-YYYY): ");

            if (existeJudoka(nombre)) {
                throw new IllegalArgumentException("El judoka " + nombre + " ya está registrado");
            }

            Judoka judoka = new Judoka(nombre, apellido, categoria, fechaNacimiento);
            listaJudokas.add(judoka);
            guardarJudokasCSV("judokas.csv");
            logger.log(Level.INFO, "Judoka agregado con éxito");
        } catch (IllegalArgumentException e) {
            logger.log(Level.WARNING, e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, String.format("Error inesperado al agregar judoka: %s", e.getMessage()), e);
        }
    }

    private String solicitarDato(Scanner scanner, String mensaje) {
        logger.log(Level.INFO, mensaje);
        return scanner.nextLine();
    }

    /**
     * Cargar judokass desde csv.
     *
     * @param rutaArchivo the ruta archivo
     * @throws IOException the io exception
     */
    public void cargarJudokassDesdeCSV(String rutaArchivo) throws IOException {
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
                Judoka judoka = parsearJudokaDesdeLineaCSV(linea);
                if (judoka != null && !existeJudoka(judoka.getNombre())) {
                    listaJudokas.add(judoka);
                }
            }
            logger.log(Level.INFO, "Judokas cargados correctamente desde {0}", rutaArchivo);
        } catch (NumberFormatException e) {
            throw new IOException("Error en el formato " + rutaArchivo, e);
        }
    }

    private Judoka parsearJudokaDesdeLineaCSV(String linea) {
        String[] datos = linea.split(",");
        if (datos.length != 7) {
            logger.log(Level.WARNING, "Línea {0} inválida en CSV: ",linea);
            return null;
        }
        try {
            String nombre = datos[0];
            String apellido = datos[1];
            String categoria = datos[2];
            int victorias = Integer.parseInt(datos[3]);
            int derrotas = Integer.parseInt(datos[4]);
            int empates = Integer.parseInt(datos[5]);
            String fechaNacimiento = datos[6];

            Judoka judoka = new Judoka(nombre, apellido, categoria, fechaNacimiento);
            judoka.setVictorias(victorias);
            judoka.setDerrotas(derrotas);
            judoka.setEmpates(empates);
            return judoka;
        } catch (Exception e) {
            logger.log(Level.WARNING, String.format("Error al parsear línea: %s", linea), e);
            return null;
        }
    }

    /**
     * Registrar resultado judoka.
     *
     * @param scanner the scanner
     */
    public void registrarResultadoJudoka(Scanner scanner) {
        try {
            logger.log(Level.INFO, "Nombre del Judoka: ");
            String nombre = scanner.nextLine();
            logger.log(Level.INFO, "Resultado (victoria / derrota / empate): ");
            String resultado = scanner.nextLine();

            Judoka judoka = obtenerJudoka(nombre);

            switch (resultado.toLowerCase()) {
                case "victoria":
                    judoka.aumentarVictoria();
                    break;
                case "derrota":
                    judoka.aumentarDerrota();
                    break;
                case "empate":
                    judoka.aumentarEmpate();
                    break;
                default:
                    throw new IllegalArgumentException("Resultado no válido: " + resultado);
            }
            guardarJudokasCSV("judokas.csv");
            logger.log(Level.INFO, "Resultado actualizado");

        } catch (IllegalArgumentException e) {
            logger.log(Level.WARNING, e.getMessage());
        } catch (Exception e) {
            if (logger.isLoggable(Level.SEVERE)) {
                logger.log(Level.SEVERE, String.format("Error inesperado al registrar resultado: %s", e.getMessage()), e);
            }
        }
    }

    /**
     * Obtener judoka judoka.
     *
     * @param nombre the nombre
     * @return the judoka
     * @throws IllegalArgumentException the illegal argument exception
     */
    public Judoka obtenerJudoka(String nombre) throws IllegalArgumentException {
        for (Judoka judoka : listaJudokas) {
            if (judoka.getNombre().equalsIgnoreCase(nombre)) {
                return judoka;
            }
        }
        throw new IllegalArgumentException("Judoka " + nombre + " no encontrado");
    }

    private boolean existeJudoka(String nombre) {
        for (Judoka judoka : listaJudokas) {
            if (judoka.getNombre().equalsIgnoreCase(nombre)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Mostrar judokas.
     */
    public void mostrarJudokas() {
        if (listaJudokas.isEmpty()) {
            logger.log(Level.INFO, "No hay judokass registrados");
        } else {
            listaJudokas.forEach(judoka -> logger.log(Level.INFO, judoka.mostrarInformacion()));
        }
    }

    /**
     * Guardar judokas csv.
     *
     * @param rutaArchivo the ruta archivo
     * @throws IOException the io exception
     */
    public void guardarJudokasCSV(String rutaArchivo) throws IOException {
        try (FileWriter fw = new FileWriter(rutaArchivo)) {
            for (Judoka judoka : listaJudokas) {
                fw.write(judoka.getNombre() + "," + judoka.getApellido() + "," + judoka.getCategoria() + "," +
                        judoka.getVictorias() + "," + judoka.getDerrotas() + "," + judoka.getEmpates() + "," +
                        judoka.getFechaNacimiento() + "\n");
            }
            logger.log(Level.INFO, "Datos de judokas guardados en {0}", rutaArchivo);
        }
    }
}