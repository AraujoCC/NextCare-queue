package com.laborwaze.queue_system.api.controller;

import com.laborwaze.queue_system.api.request.SalaRequest;
import com.laborwaze.queue_system.api.response.SalaResponse;
import com.laborwaze.queue_system.application.service.SalaService;
import com.laborwaze.queue_system.domain.model.Sala;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salas")
@RequiredArgsConstructor
@Tag(name = "Salas", description = "Endpoints para gerenciamento de salas")
public class SalaController {

    private final SalaService salaService;

    @PostMapping
    @Operation(summary = "Criar sala", description = "Cria uma nova sala no sistema")
    public ResponseEntity<SalaResponse> criar(@Valid @RequestBody SalaRequest req) {
        Sala sala = salaService.criar(req.nome(), req.numero(), req.descricao());
        return ResponseEntity.status(201).body(SalaResponse.fromEntity(sala));
    }

    @GetMapping
    @Operation(summary = "Listar salas ativas", description = "Retorna todas as salas ativas")
    public ResponseEntity<List<SalaResponse>> listarAtivas() {
        List<SalaResponse> salas = salaService.buscarAtivas().stream()
                .map(SalaResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(salas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar sala por ID", description = "Retorna uma sala pelo seu ID")
    public ResponseEntity<SalaResponse> buscarPorId(@PathVariable String id) {
        Sala sala = salaService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Sala não encontrada"));
        return ResponseEntity.ok(SalaResponse.fromEntity(sala));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desativar sala", description = "Desativa uma sala pelo seu ID")
    public ResponseEntity<Void> desativar(@PathVariable String id) {
        salaService.desativar(id);
        return ResponseEntity.noContent().build();
    }
}
