package com.laborwaze.queue_system.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laborwaze.queue_system.api.request.ProfissionalRequest;
import com.laborwaze.queue_system.application.service.ProfissionalService;
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

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({ProfissionalController.class, com.laborwaze.queue_system.config.exception.ApiExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
class ProfissionalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProfissionalService profissionalService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    private Usuario criarProfissional() {
        return new Usuario("prof-001", null, null, "Maria Silva", "maria.silva", "encoded", "maria@email.com", PapelUsuario.ATENDENTE, null, true);
    }

    @Test
    @DisplayName("Deve criar profissional com sucesso")
    void deveCriarProfissional() throws Exception {
        Usuario profissional = criarProfissional();
        ProfissionalRequest request = new ProfissionalRequest(
                "Maria Silva", "maria@email.com", "maria.silva", "senha123", PapelUsuario.ATENDENTE);
        when(profissionalService.criar(anyString(), anyString(), anyString(), anyString(), any(PapelUsuario.class)))
                .thenReturn(profissional);

        mockMvc.perform(post("/api/profissionais")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.login").value("maria.silva"))
                .andExpect(jsonPath("$.papel").value("ATENDENTE"))
                .andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    @DisplayName("Deve listar profissionais ativos")
    void deveListarProfissionaisAtivos() throws Exception {
        when(profissionalService.buscarAtivos()).thenReturn(List.of(criarProfissional()));

        mockMvc.perform(get("/api/profissionais"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].login").value("maria.silva"))
                .andExpect(jsonPath("$[0].ativo").value(true));
    }

    @Test
    @DisplayName("Deve buscar profissional por ID")
    void deveBuscarProfissionalPorId() throws Exception {
        when(profissionalService.buscarPorId("prof-001")).thenReturn(Optional.of(criarProfissional()));

        mockMvc.perform(get("/api/profissionais/prof-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("prof-001"))
                .andExpect(jsonPath("$.login").value("maria.silva"));
    }

    @Test
    @DisplayName("Deve retornar 404 ao buscar profissional inexistente")
    void deveRetornar404AoBuscarProfissionalInexistente() throws Exception {
        when(profissionalService.buscarPorId("inexistente")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/profissionais/inexistente"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve desativar profissional")
    void deveDesativarProfissional() throws Exception {
        mockMvc.perform(delete("/api/profissionais/prof-001"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar erro ao desativar profissional inexistente")
    void deveLancarErroAoDesativarProfissionalInexistente() throws Exception {
        doThrow(new com.laborwaze.queue_system.domain.exception.ResourceNotFoundException("Profissional não encontrado"))
                .when(profissionalService).desativar("inexistente");

        mockMvc.perform(delete("/api/profissionais/inexistente"))
                .andExpect(status().isNotFound());
    }
}
