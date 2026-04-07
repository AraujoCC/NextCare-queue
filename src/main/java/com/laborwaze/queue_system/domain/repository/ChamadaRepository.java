package com.laborwaze.queue_system.domain.repository;

import com.laborwaze.queue_system.domain.enums.StatusChamada;
import com.laborwaze.queue_system.domain.model.Chamada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChamadaRepository extends JpaRepository<Chamada, String> {

    List<Chamada> findByStatus(StatusChamada status);

    List<Chamada> findByAtendenteIdAndStatus(String atendenteId, StatusChamada status);

    List<Chamada> findByPacienteId(String pacienteId);

    List<Chamada> findByServicoIdAndStatus(String servicoId, StatusChamada status);

    boolean existsBySenha(String senha);

    long countByStatus(StatusChamada status);
}
