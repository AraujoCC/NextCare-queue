package com.laborwaze.queue_system.api.response;

import com.laborwaze.queue_system.domain.enums.NivelPrioridade;

import java.time.LocalDateTime;

public record PainelResponse(
    String chamadaId,
    String senha,
    String nomePaciente,
    String salaNumero,
    NivelPrioridade prioridade,
    LocalDateTime timestamp
) {}
