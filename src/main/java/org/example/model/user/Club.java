package org.example.model.user;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username; // o email
    private String password;
    private String nombre;
    private String sensei;
    private String anoFundacion;
    private String direccion;
    private String horarios;

    @Column(length = 1000)
    private String descripcion;

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Judoka> judokas = new ArrayList<>();

    /**
     * Agregar un judoka al club.
     *
     * @param judoka el judoka a agregar
     */
    public void agregarJudoka(Judoka judoka) {
        judokas.add(judoka);
        judoka.setClub(this);
    }

    /**
     * Eliminar un judoka del club.
     *
     * @param judoka el judoka a eliminar
     */
    public void eliminarJudoka(Judoka judoka) {
        judokas.remove(judoka);
        judoka.setClub(null);
    }

    /**
     * Mostrar información del club.
     *
     * @return cadena con información resumida del club
     */
    public String mostrarInformacion() {
        return "Club: " + nombre +
                ", Sensei: " + sensei +
                ", Año Fundación: " + anoFundacion +
                ", Dirección: " + direccion +
                ", Horarios: " + horarios +
                ", Descripción: " + descripcion +
                ", Judokas inscritos: " + judokas.size();
    }
}
