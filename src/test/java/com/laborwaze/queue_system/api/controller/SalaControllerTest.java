package com.laborwaze.queue_system.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laborwaze.queue_system.api.request.SalaRequest;
import com.laborwaze.queue_system.application.service.SalaService;
import com.laborwaze.queue_system.config.security.JwtAuthFilter;
import com.laborwaze.queue_system.config.security.JwtUtil;
import com.laborwaze.queue_system.domain.model.Sala;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SalaController.class)
@AutoConfigureMockMvc(addFilters = false)
class SalaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SalaService salaService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    private Sala criarSala() {
        Sala sala = Sala.builder()
                .nome("Sala 1")
                .numero("1")
                .descricao("Consulta")
                .ativo(true)
                .build();
        sala.setId("sala-001");
        return sala;
    }

    @Test
    @DisplayName("Deve criar sala com sucesso")
    void deveCriarSala() throws Exception {
        Sala sala = criarSala();
        SalaRequest request = new SalaRequest("Sala 1", "1", "Consulta");
        when(salaService.criar(anyString(), anyString(), anyString())).thenReturn(sala);

        mockMvc.perform(post("/api/salas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Sala 1"))
                .andExpect(jsonPath("$.numero").value("1"))
                .andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    @DisplayName("Deve listar salas ativas")
    void deveListarSalasAtivas() throws Exception {
        when(salaService.buscarAtivas()).thenReturn(List.of(criarSala()));

        mockMvc.perform(get("/api/salas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Sala 1"))
                .andExpect(jsonPath("$[0].numero").value("1"));
    }

    @Test
    @DisplayName("Deve buscar sala por ID")
    void deveBuscarSalaPorId() throws Exception {
        when(salaService.buscarPorId("sala-001")).thenReturn(Optional.of(criarSala()));

        mockMvc.perform(get("/api/salas/sala-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("sala-001"))
                .andExpect(jsonPath("$.nome").value("Sala 1"));
    }

    @Test
    @DisplayName("Deve retornar erro ao buscar sala inexistente")
    void deveRetornarErroAoBuscarSalaInexistente() throws Exception {
        when(salaService.buscarPorId("inexistente")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/salas/inexistente"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve desativar sala")
    void deveDesativarSala() throws Exception {
        mockMvc.perform(delete("/api/salas/sala-001"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar erro ao desativar sala inexistente")
    void deveLancarErroAoDesativarSalaInexistente() throws Exception {
        doThrow(new IllegalArgumentException("Sala não encontrada"))
                .when(salaService).desativar("inexistente");

        mockMvc.perform(delete("/api/salas/inexistente"))
                .andExpect(status().isBadRequest());
    }
}
