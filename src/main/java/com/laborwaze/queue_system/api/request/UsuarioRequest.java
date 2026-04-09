package com.laborwaze.queue_system.api.request;

import com.laborwaze.queue_system.domain.enums.PapelUsuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Requisição para registro de novo usuário")
public record UsuarioRequest(
    @Schema(description = "Nome do usuário", example = "Admin")
    @NotBlank(message = "Nome é obrigatório")
    String nome,

    @Schema(description = "Login do usuário", example = "admin")
    @NotBlank(message = "Login é obrigatório")
    String login,

    @Schema(description = "Email do usuário", example = "admin@laborwaze.com")
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    String email,

    @Schema(description = "Senha do usuário", example = "admin123")
    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
    String senha,

    @Schema(description = "Papel do usuário", example = "ADMIN")
    @NotNull(message = "Papel é obrigatório")
    PapelUsuario papel
) {}