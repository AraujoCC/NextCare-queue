package com.laborwaze.queue_system.domain.repository;

import com.laborwaze.queue_system.domain.model.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, String> {

    List<Servico> findBySetorIdAndAtivoTrue(String setorId);

    List<Servico> findByAtivoTrue();

    Optional<Servico> findByCodigo(String codigo);
}
