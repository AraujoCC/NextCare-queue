package com.laborwaze.queue_system.api.controller;

import com.laborwaze.queue_system.application.dto.LoginRequest;
import com.laborwaze.queue_system.api.response.LoginResponse;
import com.laborwaze.queue_system.application.service.AuthService;
import com.laborwaze.queue_system.domain.model.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints de autenticação")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuário", description = "Realiza login e retorna token JWT")
    public ResponseEntity<LoginResponse> autenticar(@Valid @RequestBody LoginRequest request) {
        String token = authService.authenticate(request.login(), request.senha());
        Usuario usuario = authService.loadUserByLogin(request.login());
        LoginResponse response = new LoginResponse(
            token,
            "",
            usuario.getId(),
            usuario.getLogin(),
            usuario.getPapel()
        );
        return ResponseEntity.ok(response);
    }
}
