package org.example.model.gestor;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.model.logger.LoggerManager;
import org.example.model.user.Club;

import java.io.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GestorClub {

    private List<Club> listaClubs;
    private static final Logger logger = LoggerManager.getLogger(GestorClub.class);


    public void agregarClubDesdeConsola(Scanner scanner) {
        try {
            String nombre = solicitarDato(scanner, "Nombre del Club: ");
            String sensei = solicitarDato(scanner, "Nombre del Sensei: ");
            String ubicacion = solicitarDato(scanner, "Ubicación: ");
            String horarios = solicitarDato(scanner, "Horarios: ");
            String fecha = solicitarDato(scanner, "Fecha de creación (YYYY-MM-DD): ");
            LocalDate fechaCreacion = LocalDate.parse(fecha);

            if (existeClub(nombre)) {
                throw new IllegalArgumentException("El club " + nombre + " ya está registrado");
            }

            Club club = new Club(nombre, sensei, ubicacion, horarios, fechaCreacion);
            listaClubs.add(club);
            guardarClubsCSV("clubs.csv");
            logger.log(Level.INFO, "Club agregado con éxito");
        } catch (IllegalArgumentException e) {
            logger.log(Level.WARNING, e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error inesperado al agregar club: " + e.getMessage(), e);
        }
    }

    public void mostrarClubs() {
        if (listaClubs.isEmpty()) {
            logger.log(Level.INFO, "No hay clubs registrados");
        } else {
            listaClubs.forEach(club -> logger.log(Level.INFO, String.format(
                    "Nombre: %s | Sensei: %s | Ubicación: %s | Horarios: %s | Fecha de creación: %s",
                    club.getNombre(), club.getSensei(), club.getUbicacion(),
                    club.getHorarios(), club.getFechaCreacion())));
        }
    }

    public void cargarClubsDesdeCSV(String rutaArchivo) throws IOException {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            logger.log(Level.INFO, "Archivo {0} no encontrado", rutaArchivo);
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                Club club = parsearClubDesdeLineaCSV(linea);
                if (club != null && !existeClub(club.getNombre())) {
                    listaClubs.add(club);
                }
            }
            logger.log(Level.INFO, "Clubs cargados correctamente desde {0}", rutaArchivo);
        } catch (Exception e) {
            throw new IOException("Error al leer el archivo " + rutaArchivo, e);
        }
    }

    public void guardarClubsCSV(String rutaArchivo) throws IOException {
        try (FileWriter fw = new FileWriter(rutaArchivo)) {
            for (Club club : listaClubs) {
                fw.write(String.format("%s,%s,%s,%s,%s\n",
                        club.getNombre(), club.getSensei(), club.getUbicacion(),
                        club.getHorarios(), club.getFechaCreacion()));
            }
            logger.log(Level.INFO, "Datos de clubs guardados en {0}", rutaArchivo);
        }
    }

    private Club parsearClubDesdeLineaCSV(String linea) {
        String[] datos = linea.split(",");
        if (datos.length != 5) {
            logger.log(Level.WARNING, "Línea inválida en CSV: {0}", linea);
            return null;
        }
        try {
            String nombre = datos[0];
            String sensei = datos[1];
            String ubicacion = datos[2];
            String horarios = datos[3];
            LocalDate fechaCreacion = LocalDate.parse(datos[4]);
            return new Club(nombre, sensei, ubicacion, horarios, fechaCreacion);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error al parsear línea: " + linea, e);
            return null;
        }
    }

    private String solicitarDato(Scanner scanner, String mensaje) {
        logger.log(Level.INFO, mensaje);
        return scanner.nextLine();
    }

    private boolean existeClub(String nombre) {
        return listaClubs.stream().anyMatch(club -> club.getNombre().equalsIgnoreCase(nombre));
    }
}
