package com.laborwaze.queue_system.api.request;

import com.laborwaze.queue_system.domain.enums.PapelUsuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProfissionalRequest(
    @NotBlank String nome,
    @NotBlank String email,
    @NotBlank String login,
    @NotBlank String senha,
    @NotNull PapelUsuario papel
) {}
