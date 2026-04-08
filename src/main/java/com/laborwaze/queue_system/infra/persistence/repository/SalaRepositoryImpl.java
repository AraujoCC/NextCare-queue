package com.laborwaze.queue_system.infra.persistence.repository;

import com.laborwaze.queue_system.domain.model.Sala;
import com.laborwaze.queue_system.domain.repository.SalaRepository;
import com.laborwaze.queue_system.infra.persistence.entity.SalaJpaEntity;
import com.laborwaze.queue_system.infra.persistence.mapper.DomainMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SalaRepositoryImpl implements SalaRepository {

    private final SalaJpaRepository jpaRepository;

    @Override
    public Sala save(Sala sala) {
        SalaJpaEntity entity = DomainMapper.toEntity(sala);
        return DomainMapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<Sala> findById(String id) {
        return jpaRepository.findById(id).map(DomainMapper::toDomain);
    }

    @Override
    public Optional<Sala> findByNome(String nome) {
        return jpaRepository.findByNome(nome).map(DomainMapper::toDomain);
    }

    @Override
    public List<Sala> findByAtivoTrue() {
        return jpaRepository.findByAtivoTrue().stream().map(DomainMapper::toDomain).toList();
    }
}
