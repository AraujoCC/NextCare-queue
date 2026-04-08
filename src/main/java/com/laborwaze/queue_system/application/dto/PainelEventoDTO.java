package com.laborwaze.queue_system.application.dto;

import java.time.LocalDateTime;

public record PainelEventoDTO(
    String tipo,
    String chamadaId,
    String senha,
    String nomePaciente,
    String sala,
    String prioridade,
    LocalDateTime timestamp
) {}
