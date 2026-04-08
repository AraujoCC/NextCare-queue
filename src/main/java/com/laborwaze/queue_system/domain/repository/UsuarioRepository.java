package com.laborwaze.queue_system.domain.repository;

import com.laborwaze.queue_system.domain.enums.PapelUsuario;
import com.laborwaze.queue_system.domain.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {
    Usuario save(Usuario usuario);
    Optional<Usuario> findById(String id);
    Optional<Usuario> findByLogin(String login);
    Optional<Usuario> findByEmail(String email);
    boolean existsByLogin(String login);
    boolean existsByEmail(String email);
    List<Usuario> findByAtivoTrueAndPapelIn(List<PapelUsuario> papies);
    List<Usuario> findByAtivoTrue();
}
