package com.laborwaze.queue_system.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "servicos")
@EqualsAndHashCode(callSuper = true)
public class Servico extends BaseEntity {

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String codigo;

    @Column
    private String descricao;

    @Column(name = "tempo_medio_atendimento")
    private Integer tempoMedioAtendimento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "setor_id", nullable = false)
    private Setor setor;

    @Column(name = "ativo")
    @Builder.Default
    private Boolean ativo = true;
}
