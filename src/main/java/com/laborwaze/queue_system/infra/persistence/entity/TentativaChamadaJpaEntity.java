package com.laborwaze.queue_system.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tentativas_chamada")
@EqualsAndHashCode(callSuper = true)
public class TentativaChamadaJpaEntity extends BaseJpaEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chamada_id", nullable = false)
    private ChamadaJpaEntity chamada;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sala_id")
    private SalaJpaEntity sala;

    @Column(nullable = false)
    private LocalDateTime dataTentativa;

    @Column(nullable = false)
    @Builder.Default
    private Boolean sucesso = false;

    @Column(name = "observacao")
    private String observacao;
}
