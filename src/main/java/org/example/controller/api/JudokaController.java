package org.example.controller.api;


import org.example.model.judoka.Judoka;
import org.example.service.JudokaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/judokas")
public class JudokaController {

    private final JudokaService judokaService;

    public JudokaController(JudokaService judokaService) {
        this.judokaService = judokaService;
    }

    @GetMapping
    public List<Judoka> listarJudokas() {
        return judokaService.listarJudokas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Judoka> buscarPorId(@PathVariable Long id) {
        return judokaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build()); // response entity con status 204
    }

    @PostMapping
    public Judoka crearJudoka(@RequestBody Judoka judoka) {
        return judokaService.guardarjudoka(judoka);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarJudoka(@PathVariable Long id) {
        judokaService.eliminarJudoka(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public List<Judoka> buscarPorNombre(@RequestParam String nombre) {
        return judokaService.buscarPorNombre(nombre);
    }

}
