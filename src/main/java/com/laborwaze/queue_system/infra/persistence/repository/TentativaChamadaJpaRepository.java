package com.laborwaze.queue_system.infra.persistence.repository;

import com.laborwaze.queue_system.infra.persistence.entity.TentativaChamadaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TentativaChamadaJpaRepository extends JpaRepository<TentativaChamadaJpaEntity, String> {
    List<TentativaChamadaJpaEntity> findByChamadaId(String chamadaId);
    List<TentativaChamadaJpaEntity> findByChamadaIdOrderByDataTentativaDesc(String chamadaId);
    List<TentativaChamadaJpaEntity> findBySalaId(String salaId);
}
