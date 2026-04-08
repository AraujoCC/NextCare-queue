package com.laborwaze.queue_system.infra.persistence.repository;

import com.laborwaze.queue_system.domain.enums.StatusChamada;
import com.laborwaze.queue_system.domain.model.Chamada;
import com.laborwaze.queue_system.domain.repository.ChamadaRepository;
import com.laborwaze.queue_system.infra.persistence.entity.ChamadaJpaEntity;
import com.laborwaze.queue_system.infra.persistence.mapper.DomainMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChamadaRepositoryImpl implements ChamadaRepository {

    private final ChamadaJpaRepository jpaRepository;

    @Override
    public Chamada save(Chamada chamada) {
        ChamadaJpaEntity entity = DomainMapper.toEntity(chamada);
        ChamadaJpaEntity saved = jpaRepository.save(entity);
        return DomainMapper.toDomain(saved);
    }

    @Override
    public Optional<Chamada> findById(String id) {
        return jpaRepository.findById(id).map(DomainMapper::toDomain);
    }

    @Override
    public List<Chamada> findByStatus(StatusChamada status) {
        return jpaRepository.findByStatus(status).stream().map(DomainMapper::toDomain).toList();
    }

    @Override
    public List<Chamada> findByAtendenteIdAndStatus(String atendenteId, StatusChamada status) {
        return jpaRepository.findByAtendenteIdAndStatus(atendenteId, status).stream().map(DomainMapper::toDomain).toList();
    }

    @Override
    public List<Chamada> findByPacienteId(String pacienteId) {
        return jpaRepository.findByPacienteId(pacienteId).stream().map(DomainMapper::toDomain).toList();
    }

    @Override
    public List<Chamada> findByServicoIdAndStatus(String servicoId, StatusChamada status) {
        return jpaRepository.findByServicoIdAndStatus(servicoId, status).stream().map(DomainMapper::toDomain).toList();
    }

    @Override
    public boolean existsBySenha(String senha) {
        return jpaRepository.existsBySenha(senha);
    }

    @Override
    public long countByStatus(StatusChamada status) {
        return jpaRepository.countByStatus(status);
    }

    @Override
    public List<Chamada> findByDataChamadaBetween(LocalDateTime inicio, LocalDateTime fim) {
        return jpaRepository.findByDataChamadaBetween(inicio, fim).stream().map(DomainMapper::toDomain).toList();
    }
}
