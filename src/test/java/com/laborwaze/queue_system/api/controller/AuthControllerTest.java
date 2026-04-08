package com.laborwaze.queue_system.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laborwaze.queue_system.application.dto.LoginRequest;
import com.laborwaze.queue_system.application.service.AuthService;
import com.laborwaze.queue_system.config.security.JwtAuthFilter;
import com.laborwaze.queue_system.config.security.JwtUtil;
import com.laborwaze.queue_system.domain.enums.PapelUsuario;
import com.laborwaze.queue_system.domain.model.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({AuthController.class, com.laborwaze.queue_system.config.exception.ApiExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    private Usuario criarUsuario() {
        return new Usuario("user-001", null, null, "Joao Silva", "joao.silva", "encoded", "joao@email.com", PapelUsuario.ATENDENTE, null, true);
    }

    @Test
    @DisplayName("Deve autenticar e retornar token")
    void deveAutenticarERetornarToken() throws Exception {
        Usuario usuario = criarUsuario();
        when(authService.authenticate("joao.silva", "senha123")).thenReturn("fake.jwt.token");
        when(authService.loadUserByLogin("joao.silva")).thenReturn(usuario);

        LoginRequest request = new LoginRequest("joao.silva", "senha123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake.jwt.token"))
                .andExpect(jsonPath("$.nome").value("joao.silva"))
                .andExpect(jsonPath("$.papel").value("ATENDENTE"));
    }

    @Test
    @DisplayName("Deve lancar erro com credenciais invalidas")
    void deveLancarErroComCredenciaisInvalidas() throws Exception {
        when(authService.authenticate(anyString(), anyString()))
                .thenThrow(new com.laborwaze.queue_system.domain.exception.BusinessRuleException("Credenciais inválidas"));

        LoginRequest request = new LoginRequest("joao.silva", "senha-errada");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar erro para request sem campo obrigatorio")
    void deveRetornarErroParaSemCampos() throws Exception {
        String body = "{}";

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }
}
