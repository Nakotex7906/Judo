package org.example.controller.api;

import org.example.model.competencia.Competencia;
import org.example.service.CompetenciaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/competencias")
class CompetenciaController {

    private final CompetenciaService competenciaService;

    public CompetenciaController(CompetenciaService competenciaService) {
        this.competenciaService = competenciaService;
    }

    @GetMapping
    public List<Competencia> listarCompetencias() {
        return competenciaService.listarCompetencias();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Competencia> buscarPorId(@PathVariable Long id){
        return competenciaService.buscarPorId(id)
                .map(ResponseEntity::ok).orElse(ResponseEntity.noContent().build());
    }

    @PostMapping
    public Competencia crearCompetencia(@RequestBody Competencia competencia) {
        return competenciaService.guardarCompetencia(competencia);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCompetencia(@PathVariable Long id){
        competenciaService.eliminarCompetencia(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public List<Competencia> buscarPorNombre(@RequestParam String nombre){
        return competenciaService.buscarPorNombre(nombre);
    }
}
