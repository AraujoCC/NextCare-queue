package com.laborwaze.queue_system.domain.repository;

import com.laborwaze.queue_system.domain.model.Sala;
import java.util.List;
import java.util.Optional;

public interface SalaRepository {
    Sala save(Sala sala);
    Optional<Sala> findById(String id);
    Optional<Sala> findByNome(String nome);
    List<Sala> findByAtivoTrue();
}
