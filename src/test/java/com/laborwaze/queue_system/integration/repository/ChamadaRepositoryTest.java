package com.laborwaze.queue_system.integration.repository;

import com.laborwaze.queue_system.domain.enums.NivelPrioridade;
import com.laborwaze.queue_system.domain.enums.PapelUsuario;
import com.laborwaze.queue_system.domain.enums.StatusChamada;
import com.laborwaze.queue_system.domain.model.*;
import com.laborwaze.queue_system.domain.repository.*;
import com.laborwaze.queue_system.integration.testcontainers.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class ChamadaRepositoryTest extends BaseIntegrationTest {

    @Autowired private ChamadaRepository chamadaRepository;
    @Autowired private PacienteRepository pacienteRepository;
    @Autowired private ServicoRepository servicoRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private SalaRepository salaRepository;
    @Autowired private TentativaChamadaRepository tentativaChamadaRepository;
    @Autowired private SetorRepository setorRepository;

    Paciente paciente;
    Servico servico;
    Usuario atendente;
    Setor setor;
    Chamada chamada;

    @BeforeEach
    void setUp() {
        setor = setorRepository.save(new Setor(null, null, null, "Clínica", "Setor clínico", true));
        servico = servicoRepository.save(new Servico(null, null, null, "Consulta", "C", "Consulta geral", null, setor, true));
        paciente = pacienteRepository.save(new Paciente(null, null, null, "João", "111.222.333-44", "joao@email.com", null));
        atendente = usuarioRepository.save(new Usuario(null, null, null, "Maria", "maria", "123", "maria@email.com", PapelUsuario.ATENDENTE, null, true));

        chamada = chamadaRepository.save(new Chamada(null, null, null, "C001", paciente, servico, null, StatusChamada.AGUARDANDO, NivelPrioridade.NORMAL, java.time.LocalDateTime.now(), null, null));
    }

    @Test
    @DisplayName("Deve buscar chamadas por status")
    void findByStatus() {
        List<Chamada> aguardando = chamadaRepository.findByStatus(StatusChamada.AGUARDANDO);

        assertThat(aguardando).hasSize(1);
        assertThat(aguardando.getFirst().getSenha()).isEqualTo("C001");
        assertThat(aguardando.getFirst().getStatus()).isEqualTo(StatusChamada.AGUARDANDO);
    }

    @Test
    @DisplayName("Deve retornar vazio ao buscar por status sem resultados")
    void findByStatusVazio() {
        List<Chamada> emAtendimento = chamadaRepository.findByStatus(StatusChamada.EM_ATENDIMENTO);
        assertThat(emAtendimento).isEmpty();
    }

    @Test
    @DisplayName("Deve buscar chamadas por atendente e status")
    void findByAtendenteIdAndStatus() {
        Chamada emAtendimento = new Chamada(null, null, null, "C002", paciente, servico, atendente, StatusChamada.EM_ATENDIMENTO, NivelPrioridade.NORMAL, java.time.LocalDateTime.now(), null, null);
        chamadaRepository.save(emAtendimento);

        List<Chamada> result = chamadaRepository.findByAtendenteIdAndStatus(atendente.getId(), StatusChamada.EM_ATENDIMENTO);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getAtendente().getId()).isEqualTo(atendente.getId());
    }

    @Test
    @DisplayName("Deve buscar chamadas por paciente")
    void findByPacienteId() {
        List<Chamada> result = chamadaRepository.findByPacienteId(paciente.getId());

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getPaciente().getNome()).isEqualTo("João");
    }

    @Test
    @DisplayName("Deve buscar chamadas por serviço e status")
    void findByServicoIdAndStatus() {
        List<Chamada> result = chamadaRepository.findByServicoIdAndStatus(servico.getId(), StatusChamada.AGUARDANDO);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getServico().getCodigo()).isEqualTo("C");
    }

    @Test
    @DisplayName("Deve verificar se senha existe")
    void existsBySenha() {
        boolean exists = chamadaRepository.existsBySenha("C001");
        assertThat(exists).isTrue();

        boolean notExists = chamadaRepository.existsBySenha("C999");
        assertThat(notExists).isFalse();
    }

    @Test
    @DisplayName("Deve contar chamadas por status")
    void countByStatus() {
        long count = chamadaRepository.countByStatus(StatusChamada.AGUARDANDO);
        assertThat(count).isEqualTo(1);

        long countVazia = chamadaRepository.countByStatus(StatusChamada.FINALIZADA);
        assertThat(countVazia).isZero();
    }

    @Test
    @DisplayName("Deve salvar e recuperar chamada por ID")
    void saveAndFindById() {
        Optional<Chamada> found = chamadaRepository.findById(chamada.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getSenha()).isEqualTo("C001");
        assertThat(found.get().getPaciente().getCpf()).isEqualTo("111.222.333-44");
        assertThat(found.get().getServico().getCodigo()).isEqualTo("C");
    }

    @Test
    @DisplayName("Deve atualizar status de chamada")
    void deveAtualizarStatus() {
        Chamada cFinalizada = new Chamada(chamada.getId(), chamada.getCreatedAt(), chamada.getUpdatedAt(), chamada.getSenha(), chamada.getPaciente(), chamada.getServico(), chamada.getAtendente(), StatusChamada.FINALIZADA, chamada.getPrioridade(), chamada.getDataChamada(), chamada.getDataInicioAtendimento(), java.time.LocalDateTime.now());
        chamadaRepository.save(cFinalizada);

        Optional<Chamada> found = chamadaRepository.findById(chamada.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getStatus()).isEqualTo(StatusChamada.FINALIZADA);
    }

    @Test
    @DisplayName("Deve salvar e buscar tentativas por sala")
    void deveBuscarTentativasPorSala() {
        Sala sala = salaRepository.save(new Sala(null, null, null, "Sala 1", "1", "Consulta", true));

        TentativaChamada tentativa = new TentativaChamada(null, null, null, chamada, sala, java.time.LocalDateTime.now(), true, "Atendimento realizado");
        tentativaChamadaRepository.save(tentativa);

        List<TentativaChamada> result = tentativaChamadaRepository.findBySalaId(sala.getId());
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getChamada().getSenha()).isEqualTo("C001");
    }

    @Test
    @DisplayName("Deve buscar tentativas ordenadas por data")
    void deveBuscarTentativasOrdenadasPorData() {
        TentativaChamada t1 = new TentativaChamada(null, null, null, chamada, null, java.time.LocalDateTime.now().minusMinutes(5), false, "Sem resposta");
        TentativaChamada t2 = new TentativaChamada(null, null, null, chamada, null, java.time.LocalDateTime.now(), true, "Atendido");
        tentativaChamadaRepository.save(t1);
        tentativaChamadaRepository.save(t2);

        List<TentativaChamada> result = tentativaChamadaRepository.findByChamadaIdOrderByDataTentativaDesc(chamada.getId());

        assertThat(result).hasSize(2);
        assertThat(result.getFirst().getObservacao()).isEqualTo("Atendido");
    }
}
