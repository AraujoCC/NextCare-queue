package com.laborwaze.queue_system.api.response;

import com.laborwaze.queue_system.domain.enums.PapelUsuario;

public record LoginResponse(
    String token,
    String refreshToken,
    String id,
    String nome,
    PapelUsuario papel
) {}
