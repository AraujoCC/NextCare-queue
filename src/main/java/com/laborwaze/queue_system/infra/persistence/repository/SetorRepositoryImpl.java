package com.laborwaze.queue_system.infra.persistence.repository;

import com.laborwaze.queue_system.domain.model.Setor;
import com.laborwaze.queue_system.domain.repository.SetorRepository;
import com.laborwaze.queue_system.infra.persistence.entity.SetorJpaEntity;
import com.laborwaze.queue_system.infra.persistence.mapper.DomainMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SetorRepositoryImpl implements SetorRepository {

    private final SetorJpaRepository jpaRepository;

    @Override
    public Setor save(Setor setor) {
        SetorJpaEntity entity = DomainMapper.toEntity(setor);
        return DomainMapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<Setor> findById(String id) {
        return jpaRepository.findById(id).map(DomainMapper::toDomain);
    }

    @Override
    public Optional<Setor> findByNome(String nome) {
        return jpaRepository.findByNome(nome).map(DomainMapper::toDomain);
    }

    @Override
    public List<Setor> findByAtivoTrue() {
        return jpaRepository.findByAtivoTrue().stream().map(DomainMapper::toDomain).toList();
    }
}
