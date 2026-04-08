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
        Paciente p = Paciente.builder()
                .nome("Jo\u00e3o")
                .cpf("123.456.789-00")
                .email("joao@email.com")
                .build();
        p.setId("paciente-001");
        return p;
    }

    private Servico criarServico() {
        Servico s = Servico.builder()
                .codigo("A")
                .nome("Consulta Geral")
                .descricao("Consulta geral")
                .build();
        s.setId("servico-001");
        return s;
    }

    private Usuario criarAtendente() {
        Usuario a = Usuario.builder()
                .nome("Maria Silva")
                .login("maria.silva")
                .email("maria@email.com")
                .senha("hashed")
                .papel(PapelUsuario.ATENDENTE)
                .build();
        a.setId("atendente-001");
        return a;
    }

    private Sala criarSala() {
        Sala s = Sala.builder()
                .nome("Sala 1")
                .numero("1")
                .descricao("Consulta")
                .ativo(true)
                .build();
        s.setId("sala-001");
        return s;
    }

    private Chamada criarChamada() {
        Chamada ch = Chamada.builder()
                .senha("A001")
                .paciente(paciente)
                .servico(servico)
                .status(StatusChamada.AGUARDANDO)
                .prioridade(NivelPrioridade.NORMAL)
                .dataChamada(LocalDateTime.now())
                .build();
        ch.setId("chamada-001");
        return ch;
    }

    private Chamada criarChamadaEmAtendimento() {
        Usuario atendente = criarAtendente();
        Chamada ch = Chamada.builder()
                .senha("A002")
                .paciente(paciente)
                .servico(servico)
                .atendente(atendente)
                .status(StatusChamada.EM_ATENDIMENTO)
                .prioridade(NivelPrioridade.NORMAL)
                .dataChamada(LocalDateTime.now())
                .build();
        ch.setId("chamada-002");
        return ch;
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
            saved.setId("chamada-001");
            return saved;
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
            saved.setId("chamada-001");
            return saved;
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
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Paciente n\u00e3o encontrado");

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
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Chamada n\u00e3o encontrada");

        verify(usuarioRepository, never()).findById(any());
        verify(tentativaChamadaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve finalizar chamada")
    void deveFinalizarChamada() {
        when(chamadaRepository.findById("chamada-001")).thenReturn(Optional.of(chamada));
        when(chamadaRepository.save(any(Chamada.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Chamada result = chamadaService.finalizarChamada("chamada-001");

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(StatusChamada.FINALIZADA);
        assertThat(result.getDataFimAtendimento()).isNotNull();
        verify(chamadaRepository, times(1)).save(chamada);
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
