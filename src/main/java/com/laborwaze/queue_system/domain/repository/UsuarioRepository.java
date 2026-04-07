package com.laborwaze.queue_system.domain.repository;

import com.laborwaze.queue_system.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    Optional<Usuario> findByLogin(String login);

    boolean existsByLogin(String login);
}
