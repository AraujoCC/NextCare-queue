package com.laborwaze.queue_system.application.dto;

import com.laborwaze.queue_system.domain.enums.StatusChamada;

import java.time.LocalDateTime;

public record ChamadaResponse(
    String id,
    String senha,
    String pacienteNome,
    String servicoNome,
    String atendenteNome,
    StatusChamada status,
    Boolean prioridade,
    LocalDateTime dataChamada,
    LocalDateTime dataInicioAtendimento,
    LocalDateTime dataFimAtendimento
) {}
