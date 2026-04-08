package com.laborwaze.queue_system.infra.persistence.repository;

import com.laborwaze.queue_system.infra.persistence.entity.ServicoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServicoJpaRepository extends JpaRepository<ServicoJpaEntity, String> {
    List<ServicoJpaEntity> findBySetorIdAndAtivoTrue(String setorId);
    List<ServicoJpaEntity> findByAtivoTrue();
    Optional<ServicoJpaEntity> findByCodigo(String codigo);
}
