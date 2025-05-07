package org.example.controller.api;

import org.example.model.competencia.Competencia;
import org.example.service.CompetenciaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type Competencia controller.
 */
@RestController
@RequestMapping("/api/competencias")
class CompetenciaController {

    private final CompetenciaService competenciaService;

    /**
     * Instantiates a new Competencia controller.
     *
     * @param competenciaService the competencia service
     */
    public CompetenciaController(CompetenciaService competenciaService) {
        this.competenciaService = competenciaService;
    }

    /**
     * Listar competencias list.
     *
     * @return the list
     */
    @GetMapping
    public List<Competencia> listarCompetencias() {
        return competenciaService.listarCompetencias();
    }

    /**
     * Buscar por id response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @GetMapping("/{id}")
    public ResponseEntity<Competencia> buscarPorId(@PathVariable Long id){
        return competenciaService.buscarPorId(id)
                .map(ResponseEntity::ok).orElse(ResponseEntity.noContent().build());
    }

    /**
     * Crear competencia competencia.
     *
     * @param competencia the competencia
     * @return the competencia
     */
    @PostMapping
    public Competencia crearCompetencia(@RequestBody Competencia competencia) {
        return competenciaService.guardarCompetencia(competencia);
    }

    /**
     * Eliminar competencia response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCompetencia(@PathVariable Long id){
        competenciaService.eliminarCompetencia(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Buscar por nombre list.
     *
     * @param nombre the nombre
     * @return the list
     */
    @GetMapping("/buscar")
    public List<Competencia> buscarPorNombre(@RequestParam String nombre){
        return competenciaService.buscarPorNombre(nombre);
    }
}
