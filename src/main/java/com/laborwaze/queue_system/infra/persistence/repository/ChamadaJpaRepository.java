package com.laborwaze.queue_system.infra.persistence.repository;

import com.laborwaze.queue_system.domain.enums.StatusChamada;
import com.laborwaze.queue_system.infra.persistence.entity.ChamadaJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ChamadaJpaRepository extends JpaRepository<ChamadaJpaEntity, String> {

    List<ChamadaJpaEntity> findByStatus(StatusChamada status);

    List<ChamadaJpaEntity> findByAtendenteIdAndStatus(String atendenteId, StatusChamada status);

    List<ChamadaJpaEntity> findByPacienteId(String pacienteId);

    List<ChamadaJpaEntity> findByServicoIdAndStatus(String servicoId, StatusChamada status);

    boolean existsBySenha(String senha);

    long countByStatus(StatusChamada status);

    @Query("SELECT c FROM ChamadaJpaEntity c WHERE c.dataChamada BETWEEN :inicio AND :fim ORDER BY c.dataChamada")
    List<ChamadaJpaEntity> findByDataChamadaBetween(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    @Query("SELECT c FROM ChamadaJpaEntity c WHERE c.dataChamada BETWEEN :inicio AND :fim ORDER BY c.dataChamada")
    Page<ChamadaJpaEntity> findByDataChamadaBetweenPageable(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim, Pageable pageable);
}
