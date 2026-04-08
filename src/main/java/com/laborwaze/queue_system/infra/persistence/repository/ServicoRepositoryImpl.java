package com.laborwaze.queue_system.infra.persistence.repository;

import com.laborwaze.queue_system.domain.model.Servico;
import com.laborwaze.queue_system.domain.repository.ServicoRepository;
import com.laborwaze.queue_system.infra.persistence.entity.ServicoJpaEntity;
import com.laborwaze.queue_system.infra.persistence.mapper.DomainMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ServicoRepositoryImpl implements ServicoRepository {

    private final ServicoJpaRepository jpaRepository;

    @Override
    public Servico save(Servico servico) {
        ServicoJpaEntity entity = DomainMapper.toEntity(servico);
        return DomainMapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<Servico> findById(String id) {
        return jpaRepository.findById(id).map(DomainMapper::toDomain);
    }

    @Override
    public List<Servico> findBySetorIdAndAtivoTrue(String setorId) {
        return jpaRepository.findBySetorIdAndAtivoTrue(setorId).stream().map(DomainMapper::toDomain).toList();
    }

    @Override
    public List<Servico> findByAtivoTrue() {
        return jpaRepository.findByAtivoTrue().stream().map(DomainMapper::toDomain).toList();
    }

    @Override
    public Optional<Servico> findByCodigo(String codigo) {
        return jpaRepository.findByCodigo(codigo).map(DomainMapper::toDomain);
    }
}
