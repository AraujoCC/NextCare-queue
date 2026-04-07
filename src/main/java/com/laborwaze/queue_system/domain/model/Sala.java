package com.laborwaze.queue_system.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "salas")
@EqualsAndHashCode(callSuper = true)
public class Sala extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(name = "numero")
    private String numero;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "ativo")
    @Builder.Default
    private Boolean ativo = true;
}
