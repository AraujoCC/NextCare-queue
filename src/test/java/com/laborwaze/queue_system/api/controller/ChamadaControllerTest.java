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
        Paciente paciente = Paciente.builder().nome("João").cpf("111.222.333-44").email("joao@email.com").build();
        paciente.setId("pac-001");

        Setor setor = Setor.builder().nome("Clínica").descricao("Setor").ativo(true).build();
        setor.setId("setor-001");

        Servico servico = Servico.builder().codigo("C").nome("Consulta").descricao("Consulta geral").setor(setor).ativo(true).build();
        servico.setId("srv-001");

        Chamada chamada = Chamada.builder()
                .senha("C001").paciente(paciente).servico(servico)
                .status(StatusChamada.AGUARDANDO).prioridade(NivelPrioridade.NORMAL)
                .prioridade(NivelPrioridade.NORMAL)
                .dataChamada(LocalDateTime.of(2024, 1, 15, 10, 0)).build();
        chamada.setId("chamada-001");
        return chamada;
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
        Chamada chamada = criarChamada();
        chamada.setStatus(StatusChamada.EM_ATENDIMENTO);
        chamada.setDataInicioAtendimento(LocalDateTime.now());
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
        Chamada chamada = criarChamada();
        chamada.setStatus(StatusChamada.FINALIZADA);
        chamada.setDataFimAtendimento(LocalDateTime.now());
        when(chamadaService.finalizarChamada("chamada-001")).thenReturn(chamada);

        mockMvc.perform(put("/api/chamadas/chamada-001/finalizar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("FINALIZADA"));
    }

    @Test
    @DisplayName("Deve cancelar chamada")
    void deveCancelarChamada() throws Exception {
        Chamada chamada = criarChamada();
        chamada.setStatus(StatusChamada.CANCELADA);
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
