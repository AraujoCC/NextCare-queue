package com.laborwaze.queue_system.api.request;

import jakarta.validation.constraints.NotBlank;

public record SalaRequest(
    @NotBlank String nome,
    String numero,
    String descricao
) {}
