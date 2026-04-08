package com.laborwaze.queue_system.infra.persistence.repository;

import com.laborwaze.queue_system.infra.persistence.entity.PacienteJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PacienteJpaRepository extends JpaRepository<PacienteJpaEntity, String> {
    Optional<PacienteJpaEntity> findByCpf(String cpf);
    Optional<PacienteJpaEntity> findByEmail(String email);
    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);
}
