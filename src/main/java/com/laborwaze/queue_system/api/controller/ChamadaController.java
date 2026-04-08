package com.laborwaze.queue_system.api.controller;

import com.laborwaze.queue_system.api.response.ChamadaResponse;
import com.laborwaze.queue_system.application.dto.ChamadaRequest;
import com.laborwaze.queue_system.application.service.ChamadaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chamadas")
@RequiredArgsConstructor
@Tag(name = "Chamadas", description = "API para gerenciamento de chamadas da fila")
public class ChamadaController {

    private final ChamadaService chamadaService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ATENDENTE', 'ADMIN', 'SUPERVISOR')")
    @Operation(summary = "Criar chamada", description = "Registra uma nova chamada na fila")
    public ResponseEntity<ChamadaResponse> criarChamada(@Valid @RequestBody ChamadaRequest request) {
        ChamadaResponse response = ChamadaResponse.fromEntity(chamadaService.criarChamada(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Buscar chamadas ativas", description = "Lista todas as chamadas ativas na fila")
    public ResponseEntity<List<ChamadaResponse>> buscarAtivas() {
        List<ChamadaResponse> responses = chamadaService.buscarAtivas().stream()
                .map(ChamadaResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar chamada por ID", description = "Retorna os detalhes de uma chamada específica")
    public ResponseEntity<ChamadaResponse> buscarPorId(@PathVariable String id) {
        return chamadaService.buscarPorId(id)
                .map(ChamadaResponse::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/chamar")
    @PreAuthorize("hasAnyRole('ATENDENTE', 'ADMIN', 'SUPERVISOR')")
    @Operation(summary = "Chamar para atendimento", description = "Inicia o atendimento de uma chamada")
    public ResponseEntity<ChamadaResponse> chamar(
            @PathVariable String id,
            @RequestParam String atendenteId,
            @RequestParam(required = false) String salaId) {
        return chamadaService.chamarParaAtendimento(id, atendenteId, salaId)
                .map(ChamadaResponse::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/finalizar")
    @PreAuthorize("hasAnyRole('ATENDENTE', 'ADMIN', 'SUPERVISOR')")
    @Operation(summary = "Finalizar chamada", description = "Finaliza o atendimento de uma chamada")
    public ResponseEntity<ChamadaResponse> finalizar(@PathVariable String id) {
        ChamadaResponse response = ChamadaResponse.fromEntity(chamadaService.finalizarChamada(id));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyRole('ATENDENTE', 'ADMIN', 'SUPERVISOR')")
    @Operation(summary = "Cancelar chamada", description = "Cancela uma chamada da fila")
    public ResponseEntity<ChamadaResponse> cancelar(@PathVariable String id) {
        ChamadaResponse response = ChamadaResponse.fromEntity(chamadaService.cancelarChamada(id));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/atendente/{atendenteId}")
    @Operation(summary = "Buscar por atendente", description = "Lista chamadas atribuídas a um atendente")
    public ResponseEntity<List<ChamadaResponse>> buscarPorAtendente(@PathVariable String atendenteId) {
        List<ChamadaResponse> responses = chamadaService.buscarPorAtendente(atendenteId).stream()
                .map(ChamadaResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(responses);
    }
}
