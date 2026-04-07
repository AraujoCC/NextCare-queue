package com.laborwaze.queue_system.api.controller;

import com.laborwaze.queue_system.application.service.RelatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/relatorios")
@RequiredArgsConstructor
@Tag(name = "Relatórios", description = "Endpoints para geração de relatórios")
public class RelatorioController {

    private final RelatorioService relatorioService;

    @GetMapping(value = "/chamadas", produces = MediaType.APPLICATION_PDF_VALUE)
    @Operation(summary = "Gerar relatório de chamadas", description = "Gera relatório em PDF das chamadas em um período")
    public ResponseEntity<byte[]> gerarChamadas(
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fim) throws IOException {
        byte[] pdf = relatorioService.gerarRelatorioChamadas(inicio, fim);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
