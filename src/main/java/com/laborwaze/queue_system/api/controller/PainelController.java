package com.laborwaze.queue_system.api.controller;

import com.laborwaze.queue_system.api.response.PainelResponse;
import com.laborwaze.queue_system.application.dto.PainelEventoDTO;
import com.laborwaze.queue_system.application.service.ChamadaService;
import com.laborwaze.queue_system.application.service.PainelService;
import com.laborwaze.queue_system.domain.enums.NivelPrioridade;
import com.laborwaze.queue_system.domain.model.Chamada;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/painel")
@RequiredArgsConstructor
@Tag(name = "Painel", description = "API para consulta do painel de chamadas")
public class PainelController {

    private final PainelService painelService;
    private final ChamadaService chamadaService;

    @GetMapping
    @Operation(summary = "Status do painel", description = "Retorna o estado atual do painel com chamadas ativas")
    public ResponseEntity<List<PainelResponse>> getStatus() {
        List<PainelResponse> responses = chamadaService.buscarAtivas().stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    private PainelResponse toResponse(Chamada chamada) {
        return new PainelResponse(
                chamada.getId(),
                chamada.getSenha(),
                chamada.getPaciente() != null ? chamada.getPaciente().getNome() : null,
                null,
                chamada.getPrioridade() != null ? chamada.getPrioridade() : NivelPrioridade.NORMAL,
                chamada.getDataChamada()
        );
    }
}
