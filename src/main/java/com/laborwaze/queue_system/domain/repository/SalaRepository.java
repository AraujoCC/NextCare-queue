package com.laborwaze.queue_system.domain.repository;

import com.laborwaze.queue_system.domain.model.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalaRepository extends JpaRepository<Sala, String> {

    Optional<Sala> findByNome(String nome);

    List<Sala> findByAtivoTrue();
}
