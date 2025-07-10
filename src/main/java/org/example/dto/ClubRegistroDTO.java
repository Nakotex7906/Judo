package org.example.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO (Data Transfer Object) utilizado para registrar un nuevo club en el sistema.
 *
 * <p>Incluye validaciones de campos requeridos y formato correcto para el email y año de fundación.</p>
 *
 * Se utiliza principalmente en el formulario de registro de clubes (controlador: {@code ClubWebController}).
 *
 * @author Ignacio Essus
 */
@Data
public class ClubRegistroDTO {

    /**
     * Correo electrónico del club. Se usa como nombre de usuario.
     * Debe estar presente y ser un email válido.
     */
    @NotBlank(message = "El campo 'username' es obligatorio")
    @Email(message = "Debe ingresar un correo valido")
    private String username;

    /**
     * Contraseña del club.
     * Debe estar presente.
     */
    @NotBlank(message = "El campo 'password' es obligatorio")
    private String password;

    /**
     * Nombre oficial del club.
     */
    @NotBlank(message = "El campo 'nombre' es obligatorio")
    private String nombre;

    /**
     * Nombre del sensei responsable del club.
     */
    @NotBlank(message = "El nombre del sensei es obligatorio")
    private String sensei;

    /**
     * Año de fundación del club.
     * Debe tener exactamente 4 dígitos.
     */
    @NotBlank(message = "El año de fundación es obligatorio")
    @Pattern(regexp = "\\d{4}", message = "Debe ingresar un año válido de 4 cifras")
    private String anoFundacion;

    /**
     * Dirección física donde se encuentra el club.
     */
    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    /**
     * Horarios en los que el club ofrece entrenamiento o clases.
     */
    @NotBlank(message = "Los horarios son obligatorios")
    private String horarios;
}
