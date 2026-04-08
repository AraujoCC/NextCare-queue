package com.laborwaze.queue_system.domain.repository;

import com.laborwaze.queue_system.domain.model.Paciente;
import java.util.Optional;

public interface PacienteRepository {
    Paciente save(Paciente paciente);
    Optional<Paciente> findById(String id);
    Optional<Paciente> findByCpf(String cpf);
    Optional<Paciente> findByEmail(String email);
    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);
}
