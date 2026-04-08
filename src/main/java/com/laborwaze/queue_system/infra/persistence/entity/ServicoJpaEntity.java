package com.laborwaze.queue_system.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "servicos")
@EqualsAndHashCode(callSuper = true)
public class ServicoJpaEntity extends BaseJpaEntity {

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
    private SetorJpaEntity setor;

    @Column(name = "ativo")
    @Builder.Default
    private Boolean ativo = true;
}
