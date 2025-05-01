package org.example.controller.api;


import org.example.model.judoka.Judoka;
import org.example.service.JudokaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/atletas")
public class JudokaController {

    private final JudokaService atletaService;

    public JudokaController(JudokaService atletaService) {
        this.atletaService = atletaService;
    }

    @GetMapping
    public List<Judoka> listarAtletas() {
        return atletaService.listarAtletas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Judoka> buscarPorId(@PathVariable Long id) {
        return atletaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build()); // response entity con status 204
    }

    @PostMapping
    public Judoka crearAtleta(@RequestBody Judoka atleta) {
        return atletaService.guardarAtleta(atleta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAtleta(@PathVariable Long id) {
        atletaService.eliminarAtleta(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public List<Judoka> buscarPorNombre(@RequestParam String nombre) {
        return atletaService.buscarPorNombre(nombre);
    }

}
