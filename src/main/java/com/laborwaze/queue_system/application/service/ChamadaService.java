package com.laborwaze.queue_system.application.service;

import com.laborwaze.queue_system.application.dto.ChamadaRequest;
import com.laborwaze.queue_system.application.dto.PainelEventoDTO;
import com.laborwaze.queue_system.application.port.PainelPublisherPort;
import com.laborwaze.queue_system.domain.enums.NivelPrioridade;
import com.laborwaze.queue_system.domain.enums.StatusChamada;
import com.laborwaze.queue_system.domain.model.*;
import com.laborwaze.queue_system.domain.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChamadaService {

    private final ChamadaRepository chamadaRepository;
    private final PacienteRepository pacienteRepository;
    private final ServicoRepository servicoRepository;
    private final UsuarioRepository usuarioRepository;
    private final SalaRepository salaRepository;
    private final TentativaChamadaRepository tentativaChamadaRepository;
    private final PainelPublisherPort painelPublisher;

    @Transactional
    public Chamada criarChamada(ChamadaRequest request) {
        Paciente paciente = pacienteRepository.findById(request.pacienteId())
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado"));
        Servico servico = servicoRepository.findById(request.servicoId())
                .orElseThrow(() -> new IllegalArgumentException("Serviço não encontrado"));

        String senha = gerarSenha(servico);

        Chamada chamada = Chamada.builder()
                .senha(senha)
                .paciente(paciente)
                .servico(servico)
                .status(StatusChamada.AGUARDANDO)
                .prioridade(parsePrioridade(request.prioridade()))
                .dataChamada(LocalDateTime.now())
                .build();

        chamada = chamadaRepository.save(chamada);

        PainelEventoDTO evento = new PainelEventoDTO(
            "NOVA_CHAMADA",
            chamada.getId(),
            chamada.getSenha(),
            paciente.getNome(),
            null,
            chamada.getPrioridade().name(),
            LocalDateTime.now()
        );
        painelPublisher.publicarEvento(evento);

        return chamada;
    }

    @Transactional
    public Optional<Chamada> chamarParaAtendimento(String chamadaId, String atendenteId, String salaId) {
        Chamada chamada = chamadaRepository.findById(chamadaId)
                .orElseThrow(() -> new IllegalArgumentException("Chamada não encontrada"));
        Usuario atendente = usuarioRepository.findById(atendenteId)
                .orElseThrow(() -> new IllegalArgumentException("Atendente não encontrado"));

        chamada.setStatus(StatusChamada.EM_ATENDIMENTO);
        chamada.setAtendente(atendente);
        chamada.setDataInicioAtendimento(LocalDateTime.now());
        chamada = chamadaRepository.save(chamada);

        Sala sala = salaId != null ? salaRepository.findById(salaId).orElse(null) : null;

        TentativaChamada tentativa = TentativaChamada.builder()
                .chamada(chamada)
                .sala(sala)
                .dataTentativa(LocalDateTime.now())
                .sucesso(true)
                .observacao("Chamado para atendimento")
                .build();
        tentativaChamadaRepository.save(tentativa);

        PainelEventoDTO evento = new PainelEventoDTO(
            "CHAMAR",
            chamada.getId(),
            chamada.getSenha(),
            chamada.getPaciente().getNome(),
            sala != null ? sala.getNumero() : null,
            chamada.getPrioridade().name(),
            LocalDateTime.now()
        );
        painelPublisher.publicarEvento(evento);

        return Optional.of(chamada);
    }

    @Transactional
    public Chamada finalizarChamada(String chamadaId) {
        Chamada chamada = chamadaRepository.findById(chamadaId)
                .orElseThrow(() -> new IllegalArgumentException("Chamada não encontrada"));

        chamada.setStatus(StatusChamada.FINALIZADA);
        chamada.setDataFimAtendimento(LocalDateTime.now());
        return chamadaRepository.save(chamada);
    }

    @Transactional
    public Chamada cancelarChamada(String chamadaId) {
        Chamada chamada = chamadaRepository.findById(chamadaId)
                .orElseThrow(() -> new IllegalArgumentException("Chamada não encontrada"));

        chamada.setStatus(StatusChamada.CANCELADA);
        return chamadaRepository.save(chamada);
    }

    @Transactional(readOnly = true)
    public List<Chamada> buscarAtivas() {
        return chamadaRepository.findByStatus(StatusChamada.AGUARDANDO);
    }

    @Transactional(readOnly = true)
    public Optional<Chamada> buscarPorId(String id) {
        return chamadaRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Chamada> buscarPorAtendente(String atendenteId) {
        return chamadaRepository.findByAtendenteIdAndStatus(atendenteId, StatusChamada.EM_ATENDIMENTO);
    }

    private NivelPrioridade parsePrioridade(String prioridade) {
        if (prioridade == null || prioridade.isBlank()) {
            return NivelPrioridade.NORMAL;
        }
        return switch (prioridade.toLowerCase()) {
            case "urgente" -> NivelPrioridade.URGENTE;
            case "prioridade" -> NivelPrioridade.PRIORIDADE;
            default -> NivelPrioridade.NORMAL;
        };
    }

    private String gerarSenha(Servico servico) {
        long count = chamadaRepository.countByStatus(StatusChamada.AGUARDANDO) + 1;
        String prefixo = servico.getCodigo() != null ? servico.getCodigo().substring(0, Math.min(servico.getCodigo().length(), 1)).toUpperCase() : "A";
        return String.format("%s%03d", prefixo, count);
    }
}
