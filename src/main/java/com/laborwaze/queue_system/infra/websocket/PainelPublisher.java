package com.laborwaze.queue_system.infra.websocket;

import com.laborwaze.queue_system.application.dto.PainelEventoDTO;
import com.laborwaze.queue_system.application.port.PainelPublisherPort;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PainelPublisher implements PainelPublisherPort {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void publicarEvento(PainelEventoDTO evento) {
        messagingTemplate.convertAndSend("/topic/painel", evento);
    }
}
