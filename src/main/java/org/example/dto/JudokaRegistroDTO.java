package org.example.dto;

import lombok.Data;

@Data
public class JudokaRegistroDTO {
    private String username;
    private String password;
    private String nombre;
    private String apellido;
    private String categoria;
    private String fechaNacimiento;
}

