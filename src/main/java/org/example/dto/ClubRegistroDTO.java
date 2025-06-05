package org.example.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
public class ClubRegistroDTO {
    @NotBlank(message = "El campo 'username' es obligatorio")
    @Email(message = "Debe ingresar un correo valido")
    private String username;
    @NotBlank(message = "El campo 'password' es obligatorio")
    private String password;
    @NotBlank(message = "El campo 'nombre' es obligatorio")
    private String nombre;
    @NotBlank(message = "El nombre del sensei es obligatorio")
    private String sensei;

    @NotBlank(message = "El año de fundación es obligatorio")
    @Pattern(regexp = "\\d{4}", message = "Debe ingresar un año válido de 4 cifras")
    private String anoFundacion;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @NotBlank(message = "Los horarios son obligatorios")
    private String horarios;


}