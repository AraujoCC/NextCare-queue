package com.laborwaze.queue_system.api.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank String login,
    @NotBlank String senha
) {}
