package com.laborwaze.queue_system.domain.repository;

import com.laborwaze.queue_system.domain.model.Atendente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AtendenteRepository extends JpaRepository<Atendente, String> {

    Optional<Atendente> findByEmail(String email);

    boolean existsByEmail(String email);
}
