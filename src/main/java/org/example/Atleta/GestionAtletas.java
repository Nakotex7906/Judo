package org.example.Atleta;

import org.example.Competencia.GestionarCompetencia;
import org.example.Example.LoggerManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GestionAtletas {

    private List<Atleta> listaAtletas;

    public GestionAtletas() {
        listaAtletas = new ArrayList<>();
    }

    private static final Logger logger = LoggerManager.getLogger(GestionAtletas.class);

    public void agregarAtletaDesdeConsola(Scanner scanner) {
        logger.log(Level.INFO,"Nombre: ");
        String nombre = scanner.nextLine();
        logger.log(Level.INFO,"Apellido: ");
        String apellido = scanner.nextLine();
        logger.log(Level.INFO,"Categoría: ");
        String categoria = scanner.nextLine();
        logger.log(Level.INFO,"Fecha de Nacimiento (DD-MM-YYYY): ");
        String fechaNacimiento = scanner.nextLine();

        Atleta atleta = new Atleta(nombre, apellido, categoria, fechaNacimiento);
        if (obtenerAtleta(nombre) == null) {
            listaAtletas.add(atleta);
            guardarAtletasCSV("atletas.csv");
            logger.log(Level.INFO,"Atleta agregado con exito");
        } else {
            logger.log(Level.WARNING,"Error, El atleta ya esta registrado");
        }
    }

    public void cargarAtletasDesdeCSV(String rutaArchivo) {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            logger.log(Level.INFO,rutaArchivo + " archivo encontrado");
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

                    Atleta atleta = new Atleta(nombre, apellido, categoria, fechaNacimiento);
                    atleta.setVictorias(victorias);
                    atleta.setDerrotas(derrotas);
                    atleta.setEmpates(empates);

                    if (obtenerAtleta(nombre) == null) {
                        listaAtletas.add(atleta);
                    }
                }
            }
            logger.log(Level.INFO,"Atletas cargados correctamente desde " + rutaArchivo);
        } catch (IOException e) {
            logger.log(Level.WARNING,"Error al cargar atletas: " + e.getMessage());
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING,"Error al interpretar datos numéricos: " + e.getMessage());
        }
    }

    public void registrarResultadoAtleta(Scanner scanner) {
        logger.log(Level.INFO,"Nombre del Atleta: ");
        String nombre = scanner.nextLine();
        logger.log(Level.INFO,"Resultado (victoria / derrota / empate): ");
        String resultado = scanner.nextLine();
        Atleta atleta = obtenerAtleta(nombre);

        if (atleta != null) {
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
                    logger.log(Level.WARNING,"Resultado no valido");
            }
            guardarAtletasCSV("atletas.csv");
            logger.log(Level.INFO,"Resultado actualizado");
        } else {
            logger.log(Level.WARNING,"Atleta no encontrado");
        }

    }

    public List<Atleta> getListaAtletas() {
        return listaAtletas;
    }

    // Leer información de un atleta (por nombre)
    public Atleta obtenerAtleta(String nombre) {
        for (Atleta atleta : listaAtletas) {
            if (atleta.getNombre().equalsIgnoreCase(nombre)) {
                return atleta;
            }
        }
        return null;  // Retorna null si no encuentra al atleta
    }

    public void mostrarAtletas() {
        if (listaAtletas.isEmpty()) {
            logger.log(Level.INFO,"No hay atletas registrados");
        } else {
            listaAtletas.forEach(atleta -> System.out.println(atleta.mostrarInformacion()));
        }

    }

    // Guardar lista de atletas en un archivo CSV
    public void guardarAtletasCSV(String rutaArchivo) {
        try (FileWriter fw = new FileWriter(rutaArchivo)) {
            for (Atleta atleta : listaAtletas) {
                fw.write(atleta.getNombre() + "," + atleta.getApellido() + "," + atleta.getCategoria() + "," +
                        atleta.getVictorias() + "," + atleta.getDerrotas() + "," + atleta.getEmpates() + "," +
                        atleta.getFechaNacimiento() + "\n");
            }
            logger.log(Level.INFO,"Datos de atletas guardados en " + rutaArchivo);
        } catch (IOException e) {
            logger.log(Level.WARNING,"Error al guardar atletas: " + e.getMessage());
        }
    }


}