package org.example.model.club;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username; // o email
    private String password;
    private String nombre;
}
