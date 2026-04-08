package com.laborwaze.queue_system.domain.repository;

import com.laborwaze.queue_system.domain.model.Setor;
import java.util.List;
import java.util.Optional;

public interface SetorRepository {
    Setor save(Setor setor);
    Optional<Setor> findById(String id);
    Optional<Setor> findByNome(String nome);
    List<Setor> findByAtivoTrue();
}
