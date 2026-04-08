package com.laborwaze.queue_system.domain.repository;

import com.laborwaze.queue_system.domain.model.Servico;

import java.util.List;
import java.util.Optional;

public interface ServicoRepository {
    Servico save(Servico servico);
    Optional<Servico> findById(String id);
    List<Servico> findBySetorIdAndAtivoTrue(String setorId);
    List<Servico> findByAtivoTrue();
    Optional<Servico> findByCodigo(String codigo);
}
