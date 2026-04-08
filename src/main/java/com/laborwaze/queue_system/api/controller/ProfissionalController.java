package com.laborwaze.queue_system.api.controller;

import com.laborwaze.queue_system.api.request.ProfissionalRequest;
import com.laborwaze.queue_system.api.response.ProfissionalResponse;
import com.laborwaze.queue_system.application.service.ProfissionalService;
import com.laborwaze.queue_system.domain.model.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profissionais")
@RequiredArgsConstructor
@Tag(name = "Profissionais", description = "Endpoints para gerenciamento de profissionais")
public class ProfissionalController {

    private final ProfissionalService profissionalService;

    @PostMapping
    @Operation(summary = "Criar profissional", description = "Cria um novo profissional no sistema")
    public ResponseEntity<ProfissionalResponse> criar(@Valid @RequestBody ProfissionalRequest req) {
        Usuario profissional = profissionalService.criar(
                req.nome(), req.login(), req.email(), req.senha(),
                req.papel()
        );
        return ResponseEntity.status(201).body(ProfissionalResponse.fromEntity(profissional));
    }

    @GetMapping
    @Operation(summary = "Listar profissionais ativos", description = "Retorna todos os profissionais ativos")
    public ResponseEntity<List<ProfissionalResponse>> listarAtivos() {
        List<ProfissionalResponse> profissionais = profissionalService.buscarAtivos().stream()
                .map(ProfissionalResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(profissionais);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar profissional por ID", description = "Retorna um profissional pelo seu ID")
    public ResponseEntity<ProfissionalResponse> buscarPorId(@PathVariable String id) {
        Usuario profissional = profissionalService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Profissional não encontrado"));
        return ResponseEntity.ok(ProfissionalResponse.fromEntity(profissional));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desativar profissional", description = "Desativa um profissional pelo seu ID")
    public ResponseEntity<Void> desativar(@PathVariable String id) {
        profissionalService.desativar(id);
        return ResponseEntity.noContent().build();
    }
}
