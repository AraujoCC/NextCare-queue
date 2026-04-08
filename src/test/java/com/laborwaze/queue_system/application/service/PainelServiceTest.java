package com.laborwaze.queue_system.application.service;

import com.laborwaze.queue_system.application.dto.PainelEventoDTO;
import com.laborwaze.queue_system.application.port.PainelPublisherPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PainelServiceTest {

    @Mock
    private PainelPublisherPort painelPublisher;

    @InjectMocks
    private PainelService painelService;

    @Test
    @DisplayName("Deve broadcastar evento ao notificar painel")
    void deveBroadcastarEventoAoNotificarPainel() {
        PainelEventoDTO evento = new PainelEventoDTO(
                "NOVA_CHAMADA",
                "chamada-001",
                "A001",
                "João Silva",
                "Sala 1",
                "NORMAL",
                LocalDateTime.now()
        );

        painelService.notificarPainel(evento);

        verify(painelPublisher, times(1)).publicarEvento(evento);
    }

    @Test
    @DisplayName("Deve broadcastar evento de prioridade")
    void deveBroadcastarEventoPrioridade() {
        PainelEventoDTO evento = new PainelEventoDTO(
                "NOVA_CHAMADA",
                "chamada-002",
                "X001",
                "Maria Santos",
                null,
                "URGENTE",
                LocalDateTime.now()
        );

        painelService.notificarPainel(evento);

        verify(painelPublisher).publicarEvento(argThat(e ->
                e.prioridade().equals("URGENTE") && e.tipo().equals("NOVA_CHAMADA")
        ));
    }

    @Test
    @DisplayName("Deve delegar para o publisher corretamente")
    void deveDelegarParaPublisher() {
        PainelEventoDTO evento = new PainelEventoDTO(
                "CHAMAR",
                "chamada-003",
                "B001",
                "Carlos Lima",
                "2",
                "NORMAL",
                LocalDateTime.now()
        );

        painelService.notificarPainel(evento);

        verify(painelPublisher).publicarEvento(argThat(e ->
                e.chamadaId().equals("chamada-003")
                        && e.senha().equals("B001")
                        && e.sala().equals("2")
                        && e.tipo().equals("CHAMAR")
        ));
    }
}
