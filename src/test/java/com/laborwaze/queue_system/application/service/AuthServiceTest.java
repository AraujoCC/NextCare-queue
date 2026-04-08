package com.laborwaze.queue_system.application.service;

import com.laborwaze.queue_system.config.security.JwtUtil;
import com.laborwaze.queue_system.domain.enums.PapelUsuario;
import com.laborwaze.queue_system.domain.model.Usuario;
import com.laborwaze.queue_system.domain.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario("user-001", null, null, "Joao Silva", "joao.silva", "$2a$10$encodedPasswordHash", "joao@email.com", PapelUsuario.ATENDENTE, null, true);
    }

    @Test
    @DisplayName("Deve autenticar com sucesso")
    void deveAutenticarComSucesso() {
        when(usuarioRepository.findByLogin("joao.silva")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("senha123", "$2a$10$encodedPasswordHash")).thenReturn(true);
        when(jwtUtil.gerarToken(usuario)).thenReturn("fake.jwt.token");

        String token = authService.authenticate("joao.silva", "senha123");

        assertThat(token).isEqualTo("fake.jwt.token");
        verify(usuarioRepository, times(1)).findByLogin("joao.silva");
        verify(passwordEncoder, times(1)).matches("senha123", "$2a$10$encodedPasswordHash");
        verify(jwtUtil, times(1)).gerarToken(usuario);
    }

    @Test
    @DisplayName("Deve lancar erro com senha incorreta")
    void deveLancarErroComSenhaIncorreta() {
        when(usuarioRepository.findByLogin("joao.silva")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("senha-errada", "$2a$10$encodedPasswordHash")).thenReturn(false);

        assertThatThrownBy(() -> authService.authenticate("joao.silva", "senha-errada"))
                .isInstanceOf(com.laborwaze.queue_system.domain.exception.BusinessRuleException.class)
                .hasMessageContaining("Credenciais");

        verify(jwtUtil, never()).gerarToken(any());
    }

    @Test
    @DisplayName("Deve lancar erro com usuario inexistente")
    void deveLancarErroComUsuarioInexistente() {
        when(usuarioRepository.findByLogin("usuario-inexistente")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.authenticate("usuario-inexistente", "senha123"))
                .isInstanceOf(com.laborwaze.queue_system.domain.exception.ResourceNotFoundException.class)
                .hasMessageContaining("Credenciais");

        verify(passwordEncoder, never()).matches(any(), any());
        verify(jwtUtil, never()).gerarToken(any());
    }

    @Test
    @DisplayName("Deve retornar token JWT")
    void deveRetornarTokenJWT() {
        when(usuarioRepository.findByLogin("joao.silva")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("senha123", "$2a$10$encodedPasswordHash")).thenReturn(true);
        when(jwtUtil.gerarToken(usuario)).thenReturn("fake.jwt.token");

        String token = authService.authenticate("joao.silva", "senha123");

        assertThat(token).isEqualTo("fake.jwt.token");
        verify(jwtUtil, times(1)).gerarToken(usuario);
    }
}
