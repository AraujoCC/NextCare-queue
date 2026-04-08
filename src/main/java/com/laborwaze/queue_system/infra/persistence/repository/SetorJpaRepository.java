package com.laborwaze.queue_system.infra.persistence.repository;

import com.laborwaze.queue_system.infra.persistence.entity.SetorJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SetorJpaRepository extends JpaRepository<SetorJpaEntity, String> {
    Optional<SetorJpaEntity> findByNome(String nome);
    List<SetorJpaEntity> findByAtivoTrue();
}
