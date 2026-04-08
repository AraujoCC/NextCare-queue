package com.laborwaze.queue_system.infra.persistence.repository;

import com.laborwaze.queue_system.domain.enums.PapelUsuario;
import com.laborwaze.queue_system.domain.model.Usuario;
import com.laborwaze.queue_system.domain.repository.UsuarioRepository;
import com.laborwaze.queue_system.infra.persistence.entity.UsuarioJpaEntity;
import com.laborwaze.queue_system.infra.persistence.mapper.DomainMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UsuarioRepositoryImpl implements UsuarioRepository {

    private final UsuarioJpaRepository jpaRepository;

    @Override
    public Usuario save(Usuario usuario) {
        UsuarioJpaEntity entity = DomainMapper.toEntity(usuario);
        return DomainMapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<Usuario> findById(String id) {
        return jpaRepository.findById(id).map(DomainMapper::toDomain);
    }

    @Override
    public Optional<Usuario> findByLogin(String login) {
        return jpaRepository.findByLogin(login).map(DomainMapper::toDomain);
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(DomainMapper::toDomain);
    }

    @Override
    public boolean existsByLogin(String login) {
        return jpaRepository.existsByLogin(login);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public List<Usuario> findByAtivoTrueAndPapelIn(List<PapelUsuario> papies) {
        return jpaRepository.findByAtivoTrueAndPapelIn(papies).stream().map(DomainMapper::toDomain).toList();
    }

    @Override
    public List<Usuario> findByAtivoTrue() {
        return jpaRepository.findByAtivoTrue().stream().map(DomainMapper::toDomain).toList();
    }
}
