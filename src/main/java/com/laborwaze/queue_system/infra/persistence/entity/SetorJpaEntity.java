package com.laborwaze.queue_system.infra.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "setores")
@EqualsAndHashCode(callSuper = true)
public class SetorJpaEntity extends BaseJpaEntity {

    @Column(nullable = false, unique = true)
    private String nome;

    @Column
    private String descricao;

    @Column(name = "ativo")
    @Builder.Default
    private Boolean ativo = true;
}
