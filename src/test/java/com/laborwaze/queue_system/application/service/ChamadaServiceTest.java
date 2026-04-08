package com.laborwaze.queue_system.application.service;

import com.laborwaze.queue_system.application.dto.ChamadaRequest;
import com.laborwaze.queue_system.application.dto.PainelEventoDTO;
import com.laborwaze.queue_system.application.port.PainelPublisherPort;
import com.laborwaze.queue_system.domain.enums.NivelPrioridade;
import com.laborwaze.queue_system.domain.enums.PapelUsuario;
import com.laborwaze.queue_system.domain.enums.StatusChamada;
import com.laborwaze.queue_system.domain.model.Chamada;
import com.laborwaze.queue_system.domain.model.Paciente;
import com.laborwaze.queue_system.domain.model.Sala;
import com.laborwaze.queue_system.domain.model.Servico;
import com.laborwaze.queue_system.domain.model.Usuario;
import com.laborwaze.queue_system.domain.model.Setor;
import com.laborwaze.queue_system.domain.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChamadaServiceTest {

    @Mock private ChamadaRepository chamadaRepository;
    @Mock private PacienteRepository pacienteRepository;
    @Mock private ServicoRepository servicoRepository;
    @Mock private UsuarioRepository usuarioRepository;
    @Mock private SalaRepository salaRepository;
    @Mock private TentativaChamadaRepository tentativaChamadaRepository;
    @Mock private PainelPublisherPort painelPublisher;

    @InjectMocks
    private ChamadaService chamadaService;

    Paciente paciente;
    Servico servico;
    Usuario atendente;
    Chamada chamada;
    Sala sala;

    @BeforeEach
    void setUp() {
        paciente = criarPaciente();
        servico = criarServico();
        atendente = criarAtendente();
        chamada = criarChamada();
        sala = criarSala();
    }

    private Paciente criarPaciente() {
        return new Paciente("paciente-001", null, null, "João", "123.456.789-00", "joao@email.com", null);
    }

    private Servico criarServico() {
        Setor setor = new Setor("setor-001", null, null, "Clínica", "Setor", true);
        return new Servico("servico-001", null, null, "Consulta Geral", "A", "Consulta geral", null, setor, true);
    }

    private Usuario criarAtendente() {
        return new Usuario("atendente-001", null, null, "Maria Silva", "maria.silva", "hashed", "maria@email.com", PapelUsuario.ATENDENTE, null, true);
    }

    private Sala criarSala() {
        return new Sala("sala-001", null, null, "Sala 1", "1", "Consulta", true);
    }

    private Chamada criarChamada() {
        return new Chamada("chamada-001", null, null, "A001", paciente, servico, null, StatusChamada.AGUARDANDO, NivelPrioridade.NORMAL, LocalDateTime.now(), null, null);
    }

    private Chamada criarChamadaEmAtendimento() {
        return new Chamada("chamada-002", null, null, "A002", paciente, servico, criarAtendente(), StatusChamada.EM_ATENDIMENTO, NivelPrioridade.NORMAL, LocalDateTime.now(), LocalDateTime.now(), null);
    }

    @Test
    @DisplayName("Deve criar chamada com sucesso")
    void deveCriarChamadaComSucesso() {
        ChamadaRequest request = new ChamadaRequest("paciente-001", "servico-001", "normal", null);

        when(pacienteRepository.findById("paciente-001")).thenReturn(Optional.of(paciente));
        when(servicoRepository.findById("servico-001")).thenReturn(Optional.of(servico));
        when(chamadaRepository.countByStatus(StatusChamada.AGUARDANDO)).thenReturn(0L);
        when(chamadaRepository.save(any(Chamada.class))).thenAnswer(invocation -> {
            Chamada saved = invocation.getArgument(0);
            return new Chamada("chamada-001", null, null, saved.getSenha(), saved.getPaciente(), saved.getServico(), saved.getAtendente(), saved.getStatus(), saved.getPrioridade(), saved.getDataChamada(), saved.getDataInicioAtendimento(), saved.getDataFimAtendimento());
        });

        Chamada result = chamadaService.criarChamada(request);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(StatusChamada.AGUARDANDO);
        assertThat(result.getSenha()).isEqualTo("A001");
        assertThat(result.getPrioridade()).isEqualTo(NivelPrioridade.NORMAL);
        verify(chamadaRepository, times(1)).save(any(Chamada.class));
        verify(painelPublisher, times(1)).publicarEvento(any(PainelEventoDTO.class));
    }

    @Test
    @DisplayName("Deve criar chamada com prioridade")
    void deveCriarChamadaComPrioridade() {
        ChamadaRequest request = new ChamadaRequest("paciente-001", "servico-001", "urgente", null);

        when(pacienteRepository.findById("paciente-001")).thenReturn(Optional.of(paciente));
        when(servicoRepository.findById("servico-001")).thenReturn(Optional.of(servico));
        when(chamadaRepository.countByStatus(StatusChamada.AGUARDANDO)).thenReturn(0L);
        when(chamadaRepository.save(any(Chamada.class))).thenAnswer(invocation -> {
            Chamada saved = invocation.getArgument(0);
            return new Chamada("chamada-001", null, null, saved.getSenha(), saved.getPaciente(), saved.getServico(), saved.getAtendente(), saved.getStatus(), saved.getPrioridade(), saved.getDataChamada(), saved.getDataInicioAtendimento(), saved.getDataFimAtendimento());
        });

        Chamada result = chamadaService.criarChamada(request);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(StatusChamada.AGUARDANDO);
        assertThat(result.getPrioridade()).isEqualTo(NivelPrioridade.URGENTE);
        verify(chamadaRepository, times(1)).save(any(Chamada.class));
        verify(painelPublisher, times(1)).publicarEvento(any(PainelEventoDTO.class));
    }

    @Test
    @DisplayName("Deve lancar erro ao criar chamada com paciente inexistente")
    void deveLancarErroAoCriarChamadaComPacienteInexistente() {
        ChamadaRequest request = new ChamadaRequest("paciente-inexistente", "servico-001", "normal", null);

        when(pacienteRepository.findById("paciente-inexistente")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> chamadaService.criarChamada(request))
                .isInstanceOf(com.laborwaze.queue_system.domain.exception.ResourceNotFoundException.class)
                .hasMessageContaining("Paciente");

        verify(chamadaRepository, never()).save(any());
        verify(painelPublisher, never()).publicarEvento(any());
    }

    @Test
    @DisplayName("Deve chamar para atendimento")
    void deveChamarParaAtendimento() {
        when(chamadaRepository.findById("chamada-001")).thenReturn(Optional.of(chamada));
        when(usuarioRepository.findById("atendente-001")).thenReturn(Optional.of(atendente));
        when(salaRepository.findById("sala-001")).thenReturn(Optional.of(sala));
        when(chamadaRepository.save(any(Chamada.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<Chamada> result = chamadaService.chamarParaAtendimento("chamada-001", "atendente-001", "sala-001");

        assertThat(result).isPresent();
        assertThat(result.get().getStatus()).isEqualTo(StatusChamada.EM_ATENDIMENTO);
        assertThat(result.get().getAtendente()).isEqualTo(atendente);
        assertThat(result.get().getDataInicioAtendimento()).isNotNull();
        verify(tentativaChamadaRepository, times(1)).save(any());
        verify(painelPublisher, times(1)).publicarEvento(any(PainelEventoDTO.class));
        verify(chamadaRepository, times(1)).save(any(Chamada.class));
    }

    @Test
    @DisplayName("Deve lancar erro ao chamar chamada inexistente")
    void deveLancarErroAoChamarChamadaInexistente() {
        when(chamadaRepository.findById("chamada-inexistente")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> chamadaService.chamarParaAtendimento("chamada-inexistente", "atendente-001", "sala-001"))
                .isInstanceOf(com.laborwaze.queue_system.domain.exception.ResourceNotFoundException.class)
                .hasMessageContaining("Chamada");

        verify(usuarioRepository, never()).findById(any());
        verify(tentativaChamadaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve finalizar chamada")
    void deveFinalizarChamada() {
        Chamada chamadaEmAtendimento = criarChamadaEmAtendimento();
        when(chamadaRepository.findById("chamada-001")).thenReturn(Optional.of(chamadaEmAtendimento));
        when(chamadaRepository.save(any(Chamada.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Chamada result = chamadaService.finalizarChamada("chamada-001");

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(StatusChamada.FINALIZADA);
        assertThat(result.getDataFimAtendimento()).isNotNull();
        verify(chamadaRepository, times(1)).save(chamadaEmAtendimento);
    }

    @Test
    @DisplayName("Deve cancelar chamada")
    void deveCancelarChamada() {
        when(chamadaRepository.findById("chamada-001")).thenReturn(Optional.of(chamada));
        when(chamadaRepository.save(any(Chamada.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Chamada result = chamadaService.cancelarChamada("chamada-001");

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(StatusChamada.CANCELADA);
        verify(chamadaRepository, times(1)).save(chamada);
    }

    @Test
    @DisplayName("Deve buscar chamadas ativas")
    void deveBuscarChamadasAtivas() {
        when(chamadaRepository.findByStatus(StatusChamada.AGUARDANDO)).thenReturn(List.of(chamada));

        List<Chamada> result = chamadaService.buscarAtivas();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getId()).isEqualTo("chamada-001");
        assertThat(result.getFirst().getStatus()).isEqualTo(StatusChamada.AGUARDANDO);
        verify(chamadaRepository, times(1)).findByStatus(StatusChamada.AGUARDANDO);
    }

    @Test
    @DisplayName("Deve buscar chamadas por atendente")
    void deveBuscarPorAtendente() {
        Chamada chamadaEmAtendimento = criarChamadaEmAtendimento();
        when(chamadaRepository.findByAtendenteIdAndStatus("atendente-001", StatusChamada.EM_ATENDIMENTO))
                .thenReturn(List.of(chamadaEmAtendimento));

        List<Chamada> result = chamadaService.buscarPorAtendente("atendente-001");

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getAtendente()).isNotNull();
        verify(chamadaRepository, times(1))
                .findByAtendenteIdAndStatus("atendente-001", StatusChamada.EM_ATENDIMENTO);
    }

    @Test
    @DisplayName("Deve retornar vazio ao buscar chamada inexistente")
    void deveRetornarVazioAoBuscarChamadaInexistente() {
        when(chamadaRepository.findById("chamada-inexistente")).thenReturn(Optional.empty());

        Optional<Chamada> result = chamadaService.buscarPorId("chamada-inexistente");

        assertThat(result).isEmpty();
        verify(chamadaRepository, times(1)).findById("chamada-inexistente");
    }
}
