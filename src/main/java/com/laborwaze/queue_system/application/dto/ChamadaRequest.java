package com.laborwaze.queue_system.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChamadaRequest(
    @NotBlank String pacienteId,
    @NotBlank String servicoId,
    boolean prioridade,
    String observacoes
) {}
