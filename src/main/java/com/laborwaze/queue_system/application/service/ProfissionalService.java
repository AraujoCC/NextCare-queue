package com.laborwaze.queue_system.application.service;

import com.laborwaze.queue_system.domain.enums.PapelUsuario;
import com.laborwaze.queue_system.domain.model.Usuario;
import com.laborwaze.queue_system.domain.repository.ProfissionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfissionalService {

    private final ProfissionalRepository profissionalRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Usuario criar(String nome, String email, String login, String senha, PapelUsuario papel) {
        if (profissionalRepository.existsByLogin(login)) {
            throw new IllegalArgumentException("Login já existe");
        }

        Usuario profissional = Usuario.builder()
                .login(login)
                .email(email)
                .senha(passwordEncoder.encode(senha))
                .papel(papel)
                .ativo(true)
                .build();
        return profissionalRepository.save(profissional);
    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarAtivos() {
        return profissionalRepository.findByAtivoTrue();
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorId(String id) {
        return profissionalRepository.findById(id);
    }

    @Transactional
    public void desativar(String id) {
        Usuario profissional = profissionalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Profissional não encontrado"));
        profissional.setAtivo(false);
        profissionalRepository.save(profissional);
    }
}
