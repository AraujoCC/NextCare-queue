package com.laborwaze.queue_system.application.service;

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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfissionalServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ProfissionalService profissionalService;

    private Usuario profissional;

    @BeforeEach
    void setUp() {
        profissional = new Usuario("prof-001", null, null, "Maria Silva", "maria.silva", "encoded-hash", "maria@email.com", PapelUsuario.ATENDENTE, null, true);
    }

    @Test
    @DisplayName("Deve criar profissional com sucesso")
    void deveCriarProfissionalComSucesso() {
        when(usuarioRepository.existsByLogin("maria.silva")).thenReturn(false);
        when(passwordEncoder.encode("senha123")).thenReturn("encoded-hash");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario saved = invocation.getArgument(0);
            return new Usuario("prof-001", null, null, saved.getNome(), saved.getLogin(), saved.getSenha(), saved.getEmail(), saved.getPapel(), saved.getSetor(), saved.getAtivo());
        });

            Usuario result = profissionalService.criar("Maria Silva", "maria.silva",
                "maria@email.com", "senha123", PapelUsuario.ATENDENTE);

        assertThat(result).isNotNull();
        assertThat(result.getLogin()).isEqualTo("maria.silva");
        assertThat(result.getEmail()).isEqualTo("maria@email.com");
        assertThat(result.getSenha()).isEqualTo("encoded-hash");
        assertThat(result.getPapel()).isEqualTo(PapelUsuario.ATENDENTE);
        assertThat(result.getAtivo()).isTrue();
        verify(passwordEncoder, times(1)).encode("senha123");
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lancar erro se login ja existe")
    void deveLancarErroSeLoginJaExiste() {
        when(usuarioRepository.existsByLogin("maria.silva")).thenReturn(true);

        assertThatThrownBy(() -> profissionalService.criar("Maria Silva", "maria.silva",
                "maria@email.com", "senha123", PapelUsuario.ATENDENTE))
                .isInstanceOf(com.laborwaze.queue_system.domain.exception.BusinessRuleException.class)
                .hasMessageContaining("Login");

        verify(passwordEncoder, never()).encode(any());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve buscar profissionais ativos")
    void deveBuscarProfissionaisAtivos() {
        when(usuarioRepository.findByAtivoTrue()).thenReturn(List.of(profissional));

        var result = profissionalService.buscarAtivos();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getLogin()).isEqualTo("maria.silva");
        assertThat(result.getFirst().getAtivo()).isTrue();
        verify(usuarioRepository, times(1)).findByAtivoTrue();
    }

    @Test
    @DisplayName("Deve desativar profissional")
    void deveDesativarProfissional() {
        when(usuarioRepository.findById("prof-001")).thenReturn(Optional.of(profissional));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(profissional);

        profissionalService.desativar("prof-001");

        assertThat(profissional.getAtivo()).isFalse();
        verify(usuarioRepository, times(1)).findById("prof-001");
        verify(usuarioRepository, times(1)).save(profissional);
    }
}
