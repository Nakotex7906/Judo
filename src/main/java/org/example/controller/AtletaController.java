package org.example.controller;


import org.example.model.atleta.Atleta;
import org.example.service.AtletaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/atletas")
public class AtletaController {

    private final AtletaService atletaService;

    public AtletaController(AtletaService atletaService) {
        this.atletaService = atletaService;
    }

    @GetMapping
    public List<Atleta> listarAtletas() {
        return atletaService.listarAtletas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Atleta> buscarPorId(@PathVariable Long id) {
        return atletaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build()); // response entity con status 204
    }

    @PostMapping
    public Atleta crearAtleta(@RequestBody Atleta atleta) {
        return atletaService.guardarAtleta(atleta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAtleta(@PathVariable Long id) {
        atletaService.eliminarAtleta(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public List<Atleta> buscarPorNombre(@RequestParam String nombre) {
        return atletaService.buscarPorNombre(nombre);
    }

}
