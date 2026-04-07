package com.laborwaze.queue_system.domain.model;

import com.laborwaze.queue_system.domain.enums.StatusChamada;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chamadas")
@EqualsAndHashCode(callSuper = true)
public class Chamada extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String senha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servico_id", nullable = false)
    private Servico servico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atendente_id")
    private Atendente atendente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatusChamada status = StatusChamada.AGUARDANDO;

    @Column(name = "prioridade")
    @Builder.Default
    private Boolean prioridade = false;

    @Column(name = "data_chamada", nullable = false)
    @Builder.Default
    private LocalDateTime dataChamada = LocalDateTime.now();

    @Column(name = "data_inicio_atendimento")
    private LocalDateTime dataInicioAtendimento;

    @Column(name = "data_fim_atendimento")
    private LocalDateTime dataFimAtendimento;
}
