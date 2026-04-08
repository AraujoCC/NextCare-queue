package com.laborwaze.queue_system.infra.persistence.entity;

import com.laborwaze.queue_system.domain.enums.NivelPrioridade;
import com.laborwaze.queue_system.domain.enums.StatusChamada;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chamadas")
@EqualsAndHashCode(callSuper = true)
public class ChamadaJpaEntity extends BaseJpaEntity {

    @Column(nullable = false, unique = true)
    private String senha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private PacienteJpaEntity paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servico_id", nullable = false)
    private ServicoJpaEntity servico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atendente_id")
    private UsuarioJpaEntity atendente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatusChamada status = StatusChamada.AGUARDANDO;

    @Enumerated(EnumType.STRING)
    @Column(name = "prioridade", nullable = false)
    @Builder.Default
    private NivelPrioridade prioridade = NivelPrioridade.NORMAL;

    @Column(name = "data_chamada", nullable = false)
    @Builder.Default
    private LocalDateTime dataChamada = LocalDateTime.now();

    @Column(name = "data_inicio_atendimento")
    private LocalDateTime dataInicioAtendimento;

    @Column(name = "data_fim_atendimento")
    private LocalDateTime dataFimAtendimento;
}
