package com.laborwaze.queue_system.api.response;

import com.laborwaze.queue_system.domain.enums.PapelUsuario;

public record ProfissionalResponse(
    String id,
    String nome,
    String email,
    String login,
    PapelUsuario papel,
    boolean ativo
) {
    public static ProfissionalResponse fromEntity(com.laborwaze.queue_system.domain.model.Usuario u) {
        return new ProfissionalResponse(
            u.getId(),
            u.getLogin(),
            u.getEmail(),
            u.getLogin(),
            u.getPapel(),
            u.getAtivo()
        );
    }
}
