package com.laborwaze.queue_system.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laborwaze.queue_system.application.dto.ChamadaRequest;
import com.laborwaze.queue_system.application.service.ChamadaService;
import com.laborwaze.queue_system.config.security.JwtAuthFilter;
import com.laborwaze.queue_system.config.security.JwtUtil;
import com.laborwaze.queue_system.domain.enums.PapelUsuario;
import com.laborwaze.queue_system.domain.enums.StatusChamada;
import com.laborwaze.queue_system.domain.enums.NivelPrioridade;
import com.laborwaze.queue_system.domain.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChamadaController.class)
@AutoConfigureMockMvc(addFilters = false)
class ChamadaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ChamadaService chamadaService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    private Chamada criarChamada() {
        Paciente paciente = new Paciente("pac-001", null, null, "João", "111.222.333-44", "joao@email.com", null);
        Setor setor = new Setor("setor-001", null, null, "Clínica", "Setor", true);
        Servico servico = new Servico("srv-001", null, null, "Consulta", "C", "Consulta geral", null, setor, true);
        
        return new Chamada(
            "chamada-001", null, null, "C001", paciente, servico, null, 
            StatusChamada.AGUARDANDO, NivelPrioridade.NORMAL, LocalDateTime.of(2024, 1, 15, 10, 0), null, null
        );
    }

    @Test
    @DisplayName("Deve criar chamada com sucesso")
    void deveCriarChamada() throws Exception {
        Chamada chamada = criarChamada();
        ChamadaRequest request = new ChamadaRequest("pac-001", "srv-001", "normal", null);
        when(chamadaService.criarChamada(any(ChamadaRequest.class))).thenReturn(chamada);

        mockMvc.perform(post("/api/chamadas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.senha").value("C001"))
                .andExpect(jsonPath("$.status").value("AGUARDANDO"));
    }

    @Test
    @DisplayName("Deve buscar chamadas ativas")
    void deveBuscarChamadasAtivas() throws Exception {
        when(chamadaService.buscarAtivas()).thenReturn(List.of(criarChamada()));

        mockMvc.perform(get("/api/chamadas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].senha").value("C001"))
                .andExpect(jsonPath("$[0].status").value("AGUARDANDO"));
    }

    @Test
    @DisplayName("Deve buscar chamadas ativas vazio")
    void deveBuscarChamadasAtivasVazio() throws Exception {
        when(chamadaService.buscarAtivas()).thenReturn(List.of());

        mockMvc.perform(get("/api/chamadas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("Deve buscar chamada por ID")
    void deveBuscarChamadaPorId() throws Exception {
        when(chamadaService.buscarPorId("chamada-001")).thenReturn(Optional.of(criarChamada()));

        mockMvc.perform(get("/api/chamadas/chamada-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("chamada-001"))
                .andExpect(jsonPath("$.senha").value("C001"));
    }

    @Test
    @DisplayName("Deve retornar 404 ao buscar chamada inexistente")
    void deveRetornar404AoBuscarChamadaInexistente() throws Exception {
        when(chamadaService.buscarPorId("inexistente")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/chamadas/inexistente"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve chamar para atendimento")
    void deveChamarParaAtendimento() throws Exception {
        Chamada c = criarChamada();
        Usuario atendente = new Usuario("atendente-001", null, null, "Med", "login", "senha", "email", null, null, true);
        Chamada chamada = new Chamada("chamada-001", null, null, "C001", c.getPaciente(), c.getServico(), atendente, StatusChamada.EM_ATENDIMENTO, NivelPrioridade.NORMAL, c.getDataChamada(), LocalDateTime.now(), null);
        when(chamadaService.chamarParaAtendimento("chamada-001", "atendente-001", "sala-001"))
                .thenReturn(Optional.of(chamada));

        mockMvc.perform(put("/api/chamadas/chamada-001/chamar")
                        .param("atendenteId", "atendente-001")
                        .param("salaId", "sala-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("EM_ATENDIMENTO"));
    }

    @Test
    @DisplayName("Deve finalizar chamada")
    void deveFinalizarChamada() throws Exception {
        Chamada c = criarChamada();
        Chamada chamada = new Chamada("chamada-001", null, null, "C001", c.getPaciente(), c.getServico(), null, StatusChamada.FINALIZADA, NivelPrioridade.NORMAL, c.getDataChamada(), c.getDataInicioAtendimento(), LocalDateTime.now());
        when(chamadaService.finalizarChamada("chamada-001")).thenReturn(chamada);

        mockMvc.perform(put("/api/chamadas/chamada-001/finalizar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("FINALIZADA"));
    }

    @Test
    @DisplayName("Deve cancelar chamada")
    void deveCancelarChamada() throws Exception {
        Chamada c = criarChamada();
        Chamada chamada = new Chamada("chamada-001", null, null, "C001", c.getPaciente(), c.getServico(), null, StatusChamada.CANCELADA, NivelPrioridade.NORMAL, c.getDataChamada(), null, null);
        when(chamadaService.cancelarChamada("chamada-001")).thenReturn(chamada);

        mockMvc.perform(put("/api/chamadas/chamada-001/cancelar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELADA"));
    }

    @Test
    @DisplayName("Deve buscar chamadas por atendente")
    void deveBuscarPorAtendente() throws Exception {
        when(chamadaService.buscarPorAtendente("atendente-001"))
                .thenReturn(List.of(criarChamada()));

        mockMvc.perform(get("/api/chamadas/atendente/atendente-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].senha").value("C001"));
    }
}
