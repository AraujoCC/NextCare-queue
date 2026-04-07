package com.laborwaze.queue_system.domain.repository;

import com.laborwaze.queue_system.domain.model.Setor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SetorRepository extends JpaRepository<Setor, String> {

    Optional<Setor> findByNome(String nome);

    List<Setor> findByAtivoTrue();
}
