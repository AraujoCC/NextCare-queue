package com.laborwaze.queue_system.domain.repository;

import com.laborwaze.queue_system.domain.model.TentativaChamada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TentativaChamadaRepository extends JpaRepository<TentativaChamada, String> {

    List<TentativaChamada> findByChamadaId(String chamadaId);

    List<TentativaChamada> findByChamadaIdOrderByDataTentativaDesc(String chamadaId);

    List<TentativaChamada> findBySalaId(String salaId);
}
