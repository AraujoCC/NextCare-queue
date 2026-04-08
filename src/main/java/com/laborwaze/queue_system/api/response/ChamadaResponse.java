package com.laborwaze.queue_system.api.response;

import com.laborwaze.queue_system.domain.enums.NivelPrioridade;
import com.laborwaze.queue_system.domain.enums.StatusChamada;
import com.laborwaze.queue_system.domain.model.Chamada;

import java.time.LocalDateTime;

public record ChamadaResponse(
    String id,
    String senha,
    String pacienteNome,
    String servicoNome,
    String atendenteNome,
    StatusChamada status,
    NivelPrioridade prioridade,
    LocalDateTime dataChamada,
    LocalDateTime dataInicioAtendimento,
    LocalDateTime dataFimAtendimento
) {
    public static ChamadaResponse fromEntity(Chamada c) {
        return new ChamadaResponse(
            c.getId(),
            c.getSenha(),
            c.getPaciente()  != null ? c.getPaciente().getNome()  : null,
            c.getServico()   != null ? c.getServico().getNome()   : null,
            c.getAtendente() != null ? c.getAtendente().getNome() : null,
            c.getStatus(),
            c.getPrioridade() != null ? c.getPrioridade() : NivelPrioridade.NORMAL,
            c.getDataChamada(),
            c.getDataInicioAtendimento(),
            c.getDataFimAtendimento()
        );
    }
}