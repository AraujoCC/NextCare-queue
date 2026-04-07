package com.laborwaze.queue_system.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "setores")
@EqualsAndHashCode(callSuper = true)
public class Setor extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String nome;

    @Column
    private String descricao;

    @Column(name = "ativo")
    @Builder.Default
    private Boolean ativo = true;
}
