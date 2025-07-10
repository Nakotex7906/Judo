package org.example.dto;

import lombok.Data;

/**
 * DTO (Data Transfer Object) utilizado para registrar un nuevo judoka en el sistema.
 * <p>
 * Esta clase encapsula los datos que se reciben desde el formulario de registro de judokas,
 * antes de mapearlos a una entidad del sistema.
 * </p>
 *
 * @see org.example.controllerweb.JudokaWebController
 * @author Ignacio Essus
 */
@Data
public class JudokaRegistroDTO {

    /**
     * Correo electrónico del judoka. Se utiliza como nombre de usuario.
     */
    private String username;

    /**
     * Contraseña del judoka.
     */
    private String password;

    /**
     * Nombre del judoka.
     */
    private String nombre;

    /**
     * Apellido del judoka.
     */
    private String apellido;

    /**
     * Categoría de peso del judoka (ej. -60 kg, -73 kg, etc.).
     */
    private String categoria;

    /**
     * Fecha de nacimiento del judoka en formato AAAA-MM-DD.
     */
    private String fechaNacimiento;
}
