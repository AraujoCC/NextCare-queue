package com.laborwaze.queue_system.infra.persistence.repository;

import com.laborwaze.queue_system.infra.persistence.entity.SalaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SalaJpaRepository extends JpaRepository<SalaJpaEntity, String> {
    Optional<SalaJpaEntity> findByNome(String nome);
    List<SalaJpaEntity> findByAtivoTrue();
}
