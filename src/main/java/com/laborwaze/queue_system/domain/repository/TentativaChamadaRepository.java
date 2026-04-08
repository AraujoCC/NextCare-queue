package com.laborwaze.queue_system.domain.repository;

import com.laborwaze.queue_system.domain.model.TentativaChamada;
import java.util.List;

public interface TentativaChamadaRepository {
    TentativaChamada save(TentativaChamada tentativaChamada);
    List<TentativaChamada> findByChamadaId(String chamadaId);
    List<TentativaChamada> findByChamadaIdOrderByDataTentativaDesc(String chamadaId);
    List<TentativaChamada> findBySalaId(String salaId);
}
