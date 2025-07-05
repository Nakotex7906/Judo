package org.example.controllerweb;

import lombok.RequiredArgsConstructor;
import org.example.model.user.Judoka;
import org.example.service.JudokaService;
import org.example.service.ReporteService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;
    private final JudokaService judokaService;

    @GetMapping("/reporte/combate")
    public ResponseEntity<byte[]> generarReporte() {
        List<Judoka> judokas = judokaService.listarJudokas();
        byte[] pdf = reporteService.generarReporteCombate(judokas);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
