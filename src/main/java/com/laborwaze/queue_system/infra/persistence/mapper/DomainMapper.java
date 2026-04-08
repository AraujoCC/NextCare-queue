package com.laborwaze.queue_system.infra.persistence.mapper;

import com.laborwaze.queue_system.domain.model.*;
import com.laborwaze.queue_system.infra.persistence.entity.*;

public class DomainMapper {

    public static Paciente toDomain(PacienteJpaEntity entity) {
        if (entity == null) return null;
        return new Paciente(entity.getId(), entity.getCreatedAt(), entity.getUpdatedAt(),
                entity.getNome(), entity.getCpf(), entity.getEmail(), entity.getTelefone());
    }

    public static PacienteJpaEntity toEntity(Paciente domain) {
        if (domain == null) return null;
        return PacienteJpaEntity.builder().id(domain.getId()).createdAt(domain.getCreatedAt()).updatedAt(domain.getUpdatedAt())
                .nome(domain.getNome()).cpf(domain.getCpf()).email(domain.getEmail()).telefone(domain.getTelefone()).build();
    }

    public static Sala toDomain(SalaJpaEntity entity) {
        if (entity == null) return null;
        return new Sala(entity.getId(), entity.getCreatedAt(), entity.getUpdatedAt(),
                entity.getNome(), entity.getNumero(), entity.getDescricao(), entity.getAtivo());
    }

    public static SalaJpaEntity toEntity(Sala domain) {
        if (domain == null) return null;
        return SalaJpaEntity.builder().id(domain.getId()).createdAt(domain.getCreatedAt()).updatedAt(domain.getUpdatedAt())
                .nome(domain.getNome()).numero(domain.getNumero()).descricao(domain.getDescricao()).ativo(domain.getAtivo()).build();
    }

    public static Setor toDomain(SetorJpaEntity entity) {
        if (entity == null) return null;
        return new Setor(entity.getId(), entity.getCreatedAt(), entity.getUpdatedAt(),
                entity.getNome(), entity.getDescricao(), entity.getAtivo());
    }

    public static SetorJpaEntity toEntity(Setor domain) {
        if (domain == null) return null;
        return SetorJpaEntity.builder().id(domain.getId()).createdAt(domain.getCreatedAt()).updatedAt(domain.getUpdatedAt())
                .nome(domain.getNome()).descricao(domain.getDescricao()).ativo(domain.getAtivo()).build();
    }

    public static Usuario toDomain(UsuarioJpaEntity entity) {
        if (entity == null) return null;
        return new Usuario(entity.getId(), entity.getCreatedAt(), entity.getUpdatedAt(),
                entity.getNome(), entity.getLogin(), entity.getSenha(), entity.getEmail(),
                entity.getPapel(), toDomain(entity.getSetor()), entity.getAtivo());
    }

    public static UsuarioJpaEntity toEntity(Usuario domain) {
        if (domain == null) return null;
        return UsuarioJpaEntity.builder().id(domain.getId()).createdAt(domain.getCreatedAt()).updatedAt(domain.getUpdatedAt())
                .nome(domain.getNome()).login(domain.getLogin()).senha(domain.getSenha()).email(domain.getEmail())
                .papel(domain.getPapel()).setor(toEntity(domain.getSetor())).ativo(domain.getAtivo()).build();
    }

    public static Servico toDomain(ServicoJpaEntity entity) {
        if (entity == null) return null;
        return new Servico(entity.getId(), entity.getCreatedAt(), entity.getUpdatedAt(),
                entity.getNome(), entity.getCodigo(), entity.getDescricao(), entity.getTempoMedioAtendimento(),
                toDomain(entity.getSetor()), entity.getAtivo());
    }

    public static ServicoJpaEntity toEntity(Servico domain) {
        if (domain == null) return null;
        return ServicoJpaEntity.builder().id(domain.getId()).createdAt(domain.getCreatedAt()).updatedAt(domain.getUpdatedAt())
                .nome(domain.getNome()).codigo(domain.getCodigo()).descricao(domain.getDescricao())
                .tempoMedioAtendimento(domain.getTempoMedioAtendimento()).setor(toEntity(domain.getSetor())).ativo(domain.getAtivo()).build();
    }

    public static Chamada toDomain(ChamadaJpaEntity entity) {
        if (entity == null) return null;
        return new Chamada(entity.getId(), entity.getCreatedAt(), entity.getUpdatedAt(),
                entity.getSenha(), toDomain(entity.getPaciente()), toDomain(entity.getServico()),
                toDomain(entity.getAtendente()), entity.getStatus(), entity.getPrioridade(),
                entity.getDataChamada(), entity.getDataInicioAtendimento(), entity.getDataFimAtendimento());
    }

    public static ChamadaJpaEntity toEntity(Chamada domain) {
        if (domain == null) return null;
        return ChamadaJpaEntity.builder().id(domain.getId()).createdAt(domain.getCreatedAt()).updatedAt(domain.getUpdatedAt())
                .senha(domain.getSenha())
                .paciente(toEntity(domain.getPaciente()))
                .servico(toEntity(domain.getServico()))
                .atendente(toEntity(domain.getAtendente()))
                .status(domain.getStatus())
                .prioridade(domain.getPrioridade())
                .dataChamada(domain.getDataChamada())
                .dataInicioAtendimento(domain.getDataInicioAtendimento())
                .dataFimAtendimento(domain.getDataFimAtendimento()).build();
    }

    public static TentativaChamada toDomain(TentativaChamadaJpaEntity entity) {
        if (entity == null) return null;
        return new TentativaChamada(entity.getId(), entity.getCreatedAt(), entity.getUpdatedAt(),
                toDomain(entity.getChamada()), toDomain(entity.getSala()), entity.getDataTentativa(),
                entity.getSucesso(), entity.getObservacao());
    }

    public static TentativaChamadaJpaEntity toEntity(TentativaChamada domain) {
        if (domain == null) return null;
        return TentativaChamadaJpaEntity.builder().id(domain.getId()).createdAt(domain.getCreatedAt()).updatedAt(domain.getUpdatedAt())
                .chamada(toEntity(domain.getChamada()))
                .sala(toEntity(domain.getSala()))
                .dataTentativa(domain.getDataTentativa())
                .sucesso(domain.getSucesso())
                .observacao(domain.getObservacao()).build();
    }
}
