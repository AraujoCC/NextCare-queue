package com.laborwaze.queue_system.infra.persistence.repository;

import com.laborwaze.queue_system.domain.model.Paciente;
import com.laborwaze.queue_system.domain.repository.PacienteRepository;
import com.laborwaze.queue_system.infra.persistence.entity.PacienteJpaEntity;
import com.laborwaze.queue_system.infra.persistence.mapper.DomainMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PacienteRepositoryImpl implements PacienteRepository {

    private final PacienteJpaRepository jpaRepository;

    @Override
    public Paciente save(Paciente paciente) {
        PacienteJpaEntity entity = DomainMapper.toEntity(paciente);
        return DomainMapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<Paciente> findById(String id) {
        return jpaRepository.findById(id).map(DomainMapper::toDomain);
    }

    @Override
    public Optional<Paciente> findByCpf(String cpf) {
        return jpaRepository.findByCpf(cpf).map(DomainMapper::toDomain);
    }

    @Override
    public Optional<Paciente> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(DomainMapper::toDomain);
    }

    @Override
    public boolean existsByCpf(String cpf) {
        return jpaRepository.existsByCpf(cpf);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }
}
