package com.laborwaze.queue_system.application.port;

import com.laborwaze.queue_system.application.dto.PainelEventoDTO;

public interface PainelPublisherPort {
    void publicarEvento(PainelEventoDTO evento);
}
