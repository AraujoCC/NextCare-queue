package com.laborwaze.queue_system.domain.repository;

import com.laborwaze.queue_system.domain.enums.StatusChamada;
import com.laborwaze.queue_system.domain.model.Chamada;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ChamadaRepository {
    Chamada save(Chamada chamada);
    Optional<Chamada> findById(String id);
    List<Chamada> findByStatus(StatusChamada status);
    List<Chamada> findByAtendenteIdAndStatus(String atendenteId, StatusChamada status);
    List<Chamada> findByPacienteId(String pacienteId);
    List<Chamada> findByServicoIdAndStatus(String servicoId, StatusChamada status);
    boolean existsBySenha(String senha);
    long countByStatus(StatusChamada status);
    List<Chamada> findByDataChamadaBetween(LocalDateTime inicio, LocalDateTime fim);
}
