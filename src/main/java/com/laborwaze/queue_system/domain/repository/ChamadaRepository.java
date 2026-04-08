package com.laborwaze.queue_system.domain.repository;

import com.laborwaze.queue_system.domain.enums.StatusChamada;
import com.laborwaze.queue_system.domain.model.Chamada;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChamadaRepository extends JpaRepository<Chamada, String> {

    List<Chamada> findByStatus(StatusChamada status);

    List<Chamada> findByAtendenteIdAndStatus(String atendenteId, StatusChamada status);

    List<Chamada> findByPacienteId(String pacienteId);

    List<Chamada> findByServicoIdAndStatus(String servicoId, StatusChamada status);

    boolean existsBySenha(String senha);

    long countByStatus(StatusChamada status);

    @Query("SELECT c FROM Chamada c WHERE c.dataChamada BETWEEN :inicio AND :fim ORDER BY c.dataChamada")
    List<Chamada> findByDataChamadaBetween(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    @Query("SELECT c FROM Chamada c WHERE c.dataChamada BETWEEN :inicio AND :fim ORDER BY c.dataChamada")
    Page<Chamada> findByDataChamadaBetweenPageable(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim, Pageable pageable);
}
