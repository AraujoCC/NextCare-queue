package com.laborwaze.queue_system.application.service;

import com.laborwaze.queue_system.domain.model.Sala;
import com.laborwaze.queue_system.domain.repository.SalaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SalaServiceTest {

    @Mock
    private SalaRepository salaRepository;

    @InjectMocks
    private SalaService salaService;

    private Sala sala;

    @BeforeEach
    void setUp() {
        sala = Sala.builder()
                .nome("Sala 1")
                .numero("1")
                .descricao("Sala de consulta")
                .ativo(true)
                .build();
        sala.setId("sala-001");
    }

    @Test
    @DisplayName("Deve criar sala com sucesso")
    void deveCriarSalaComSucesso() {
        when(salaRepository.save(any(Sala.class))).thenAnswer(invocation -> {
            Sala saved = invocation.getArgument(0);
            saved.setId("sala-001");
            return saved;
        });

        Sala result = salaService.criar("Sala 1", "1", "Sala de consulta");

        assertThat(result).isNotNull();
        assertThat(result.getNome()).isEqualTo("Sala 1");
        assertThat(result.getNumero()).isEqualTo("1");
        assertThat(result.getDescricao()).isEqualTo("Sala de consulta");
        assertThat(result.getAtivo()).isTrue();
        verify(salaRepository, times(1)).save(any(Sala.class));
    }

    @Test
    @DisplayName("Deve buscar salas ativas")
    void deveBuscarSalasAtivas() {
        when(salaRepository.findByAtivoTrue()).thenReturn(List.of(sala));

        var result = salaService.buscarAtivas();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getNome()).isEqualTo("Sala 1");
        assertThat(result.getFirst().getAtivo()).isTrue();
        verify(salaRepository, times(1)).findByAtivoTrue();
    }

    @Test
    @DisplayName("Deve desativar sala")
    void deveDesativarSala() {
        when(salaRepository.findById("sala-001")).thenReturn(Optional.of(sala));
        when(salaRepository.save(any(Sala.class))).thenReturn(sala);

        salaService.desativar("sala-001");

        assertThat(sala.getAtivo()).isFalse();
        verify(salaRepository, times(1)).findById("sala-001");
        verify(salaRepository, times(1)).save(sala);
    }

    @Test
    @DisplayName("Deve lancar erro ao buscar sala inexistente")
    void deveLancarErroAoBuscarSalaInexistente() {
        when(salaRepository.findById("sala-inexistente")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> salaService.desativar("sala-inexistente"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Sala n\u00e3o encontrada");

        verify(salaRepository, never()).save(any());
    }
}
