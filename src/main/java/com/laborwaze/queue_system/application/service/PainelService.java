package com.laborwaze.queue_system.application.service;

import com.laborwaze.queue_system.application.dto.PainelEventoDTO;
import com.laborwaze.queue_system.application.port.PainelPublisherPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PainelService {

    private final PainelPublisherPort painelPublisher;

    public void notificarPainel(PainelEventoDTO evento) {
        painelPublisher.publicarEvento(evento);
    }
}
