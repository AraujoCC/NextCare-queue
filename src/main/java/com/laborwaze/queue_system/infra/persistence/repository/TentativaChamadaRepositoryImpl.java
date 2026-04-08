package com.laborwaze.queue_system.infra.persistence.repository;

import com.laborwaze.queue_system.domain.model.TentativaChamada;
import com.laborwaze.queue_system.domain.repository.TentativaChamadaRepository;
import com.laborwaze.queue_system.infra.persistence.entity.TentativaChamadaJpaEntity;
import com.laborwaze.queue_system.infra.persistence.mapper.DomainMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TentativaChamadaRepositoryImpl implements TentativaChamadaRepository {

    private final TentativaChamadaJpaRepository jpaRepository;

    @Override
    public TentativaChamada save(TentativaChamada tentativaChamada) {
        TentativaChamadaJpaEntity entity = DomainMapper.toEntity(tentativaChamada);
        return DomainMapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public List<TentativaChamada> findByChamadaId(String chamadaId) {
        return jpaRepository.findByChamadaId(chamadaId).stream().map(DomainMapper::toDomain).toList();
    }

    @Override
    public List<TentativaChamada> findByChamadaIdOrderByDataTentativaDesc(String chamadaId) {
        return jpaRepository.findByChamadaIdOrderByDataTentativaDesc(chamadaId).stream().map(DomainMapper::toDomain).toList();
    }

    @Override
    public List<TentativaChamada> findBySalaId(String salaId) {
        return jpaRepository.findBySalaId(salaId).stream().map(DomainMapper::toDomain).toList();
    }
}
