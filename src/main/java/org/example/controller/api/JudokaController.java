package org.example.controller.api;


import org.example.model.judoka.Judoka;
import org.example.service.JudokaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type Judoka controller.
 */
@RestController
@RequestMapping("/api/judokas")
public class JudokaController {

    private final JudokaService judokaService;

    /**
     * Instantiates a new Judoka controller.
     *
     * @param judokaService the judoka service
     */
    public JudokaController(JudokaService judokaService) {
        this.judokaService = judokaService;
    }

    /**
     * Listar judokas list.
     *
     * @return the list
     */
    @GetMapping
    public List<Judoka> listarJudokas() {
        return judokaService.listarJudokas();
    }

    /**
     * Buscar por id response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @GetMapping("/{id}")
    public ResponseEntity<Judoka> buscarPorId(@PathVariable Long id) {
        return judokaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build()); // response entity con status 204
    }

    /**
     * Crear judoka judoka.
     *
     * @param judoka the judoka
     * @return the judoka
     */
    @PostMapping
    public Judoka crearJudoka(@RequestBody Judoka judoka) {
        return judokaService.guardarjudoka(judoka);
    }

    /**
     * Eliminar judoka response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarJudoka(@PathVariable Long id) {
        judokaService.eliminarJudoka(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Buscar por nombre list.
     *
     * @param nombre the nombre
     * @return the list
     */
    @GetMapping("/buscar")
    public List<Judoka> buscarPorNombre(@RequestParam String nombre) {
        return judokaService.buscarPorNombre(nombre);
    }

}
