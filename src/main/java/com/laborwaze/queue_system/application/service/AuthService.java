package com.laborwaze.queue_system.application.service;

import com.laborwaze.queue_system.config.security.JwtUtil;
import com.laborwaze.queue_system.domain.model.Usuario;
import com.laborwaze.queue_system.domain.exception.BusinessRuleException;
import com.laborwaze.queue_system.domain.exception.ResourceNotFoundException;
import com.laborwaze.queue_system.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional(readOnly = true)
    public String authenticate(String login, String senha) {
        Usuario usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("Credenciais inválidas"));

        if (!passwordEncoder.matches(senha, usuario.getSenha())) {
            throw new BusinessRuleException("Credenciais inválidas");
        }

        if (!usuario.getAtivo()) {
            throw new BusinessRuleException("Usuário desativado");
        }

        return jwtUtil.gerarToken(usuario);
    }

    @Transactional(readOnly = true)
    public Usuario loadUserByLogin(String login) {
        return usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }

    @Transactional
    public Usuario register(String nome, String login, String email, String senha, com.laborwaze.queue_system.domain.enums.PapelUsuario papel) {
        if (usuarioRepository.existsByLogin(login)) {
            throw new BusinessRuleException("Login já está em uso");
        }
        if (usuarioRepository.existsByEmail(email)) {
            throw new BusinessRuleException("Email já está em uso");
        }

        String senhaCriptografada = passwordEncoder.encode(senha);
        Usuario usuario = new Usuario(
            UUID.randomUUID().toString(),
            java.time.LocalDateTime.now(),
            java.time.LocalDateTime.now(),
            nome,
            login,
            senhaCriptografada,
            email,
            papel,
            null,
            true
        );

        return usuarioRepository.save(usuario);
    }
}
