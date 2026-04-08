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
        setor = setorRepository.save(Setor.builder().nome("Clínica").descricao("Setor clínico").ativo(true).build());
        servico = servicoRepository.save(Servico.builder()
                .codigo("C").nome("Consulta").descricao("Consulta geral").setor(setor).ativo(true).build());
        paciente = pacienteRepository.save(Paciente.builder()
                .nome("João").cpf("111.222.333-44").email("joao@email.com").build());
        atendente = usuarioRepository.save(Usuario.builder()
                .nome("Maria").login("maria").email("maria@email.com").senha("123")
                .papel(PapelUsuario.ATENDENTE).build());

        chamada = chamadaRepository.save(Chamada.builder()
                .senha("C001").paciente(paciente).servico(servico)
                .status(StatusChamada.AGUARDANDO).prioridade(NivelPrioridade.NORMAL).build());
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
        Chamada emAtendimento = Chamada.builder()
                .senha("C002").paciente(paciente).servico(servico).atendente(atendente)
                .status(StatusChamada.EM_ATENDIMENTO).prioridade(NivelPrioridade.NORMAL).build();
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
        chamada.setStatus(StatusChamada.FINALIZADA);
        chamadaRepository.save(chamada);

        Optional<Chamada> found = chamadaRepository.findById(chamada.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getStatus()).isEqualTo(StatusChamada.FINALIZADA);
    }

    @Test
    @DisplayName("Deve salvar e buscar tentativas por sala")
    void deveBuscarTentativasPorSala() {
        Sala sala = salaRepository.save(Sala.builder().nome("Sala 1").numero("1").descricao("Consulta").build());

        TentativaChamada tentativa = TentativaChamada.builder()
                .chamada(chamada).sala(sala).dataTentativa(java.time.LocalDateTime.now())
                .sucesso(true).observacao("Atendimento realizado").build();
        tentativaChamadaRepository.save(tentativa);

        List<TentativaChamada> result = tentativaChamadaRepository.findBySalaId(sala.getId());
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getChamada().getSenha()).isEqualTo("C001");
    }

    @Test
    @DisplayName("Deve buscar tentativas ordenadas por data")
    void deveBuscarTentativasOrdenadasPorData() {
        TentativaChamada t1 = TentativaChamada.builder()
                .chamada(chamada).dataTentativa(java.time.LocalDateTime.now().minusMinutes(5))
                .sucesso(false).observacao("Sem resposta").build();
        TentativaChamada t2 = TentativaChamada.builder()
                .chamada(chamada).dataTentativa(java.time.LocalDateTime.now())
                .sucesso(true).observacao("Atendido").build();
        tentativaChamadaRepository.save(t1);
        tentativaChamadaRepository.save(t2);

        List<TentativaChamada> result = tentativaChamadaRepository.findByChamadaIdOrderByDataTentativaDesc(chamada.getId());

        assertThat(result).hasSize(2);
        assertThat(result.getFirst().getObservacao()).isEqualTo("Atendido");
    }
}
