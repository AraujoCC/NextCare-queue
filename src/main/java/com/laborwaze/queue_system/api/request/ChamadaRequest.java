package com.laborwaze.queue_system.api.request;

import jakarta.validation.constraints.NotBlank;

public record ChamadaRequest(
    @NotBlank String pacienteId,
    @NotBlank String servicoId,
    boolean prioridade,
    String observacoes
) {}
