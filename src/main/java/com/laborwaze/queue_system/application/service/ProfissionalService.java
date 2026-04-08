package com.laborwaze.queue_system.application.service;

import com.laborwaze.queue_system.domain.enums.PapelUsuario;
import com.laborwaze.queue_system.domain.model.Usuario;
import com.laborwaze.queue_system.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfissionalService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Usuario criar(String nome, String login, String email, String senha, PapelUsuario papel) {
        if (usuarioRepository.existsByLogin(login)) {
            throw new IllegalArgumentException("Login já existe");
        }
        if (usuarioRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email já existe");
        }

        Usuario profissional = Usuario.builder()
                .nome(nome)
                .login(login)
                .email(email)
                .senha(passwordEncoder.encode(senha))
                .papel(papel)
                .ativo(true)
                .build();
        return usuarioRepository.save(profissional);
    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarAtivos() {
        return usuarioRepository.findByAtivoTrue();
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorId(String id) {
        return usuarioRepository.findById(id);
    }

    @Transactional
    public void desativar(String id) {
        Usuario profissional = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Profissional não encontrado"));
        profissional.setAtivo(false);
        usuarioRepository.save(profissional);
    }
}
