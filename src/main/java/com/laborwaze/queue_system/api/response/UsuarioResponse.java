package com.laborwaze.queue_system.api.response;

import com.laborwaze.queue_system.domain.enums.PapelUsuario;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta do registro de usuário")
public record UsuarioResponse(
    @Schema(description = "ID do usuário", example = "uuid")
    String id,

    @Schema(description = "Nome do usuário", example = "Admin")
    String nome,

    @Schema(description = "Login do usuário", example = "admin")
    String login,

    @Schema(description = "Email do usuário", example = "admin@laborwaze.com")
    String email,

    @Schema(description = "Papel do usuário", example = "ADMIN")
    PapelUsuario papel
) {}