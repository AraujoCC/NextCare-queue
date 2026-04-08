package com.laborwaze.queue_system.infra.persistence.repository;

import com.laborwaze.queue_system.domain.enums.PapelUsuario;
import com.laborwaze.queue_system.infra.persistence.entity.UsuarioJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioJpaRepository extends JpaRepository<UsuarioJpaEntity, String> {
    Optional<UsuarioJpaEntity> findByLogin(String login);
    Optional<UsuarioJpaEntity> findByEmail(String email);
    boolean existsByLogin(String login);
    boolean existsByEmail(String email);
    List<UsuarioJpaEntity> findByAtivoTrueAndPapelIn(List<PapelUsuario> papies);
    List<UsuarioJpaEntity> findByAtivoTrue();
}
