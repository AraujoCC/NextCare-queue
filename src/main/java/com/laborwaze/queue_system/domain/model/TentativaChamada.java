package com.laborwaze.queue_system.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tentativas_chamada")
@EqualsAndHashCode(callSuper = true)
public class TentativaChamada extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chamada_id", nullable = false)
    private Chamada chamada;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sala_id")
    private Sala sala;

    @Column(nullable = false)
    private LocalDateTime dataTentativa;

    @Column(nullable = false)
    @Builder.Default
    private Boolean sucesso = false;

    @Column(name = "observacao")
    private String observacao;
}
