package com.laborwaze.queue_system.domain.model;

import com.laborwaze.queue_system.domain.enums.PapelUsuario;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "atendentes")
@EqualsAndHashCode(callSuper = true)
public class Atendente extends BaseEntity {

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private PapelUsuario papel = PapelUsuario.ATENDENTE;

    @Column(name = "ativo")
    @Builder.Default
    private Boolean ativo = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "setor_id")
    private Setor setor;
}
