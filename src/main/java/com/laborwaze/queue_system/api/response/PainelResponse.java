package com.laborwaze.queue_system.api.response;

import java.time.LocalDateTime;

public record PainelResponse(
    String chamadaId,
    String senha,
    String nomePaciente,
    String salaNumero,
    boolean prioridade,
    LocalDateTime timestamp
) {}
