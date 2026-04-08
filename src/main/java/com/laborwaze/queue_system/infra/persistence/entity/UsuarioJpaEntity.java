package com.laborwaze.queue_system.infra.persistence.entity;

import com.laborwaze.queue_system.domain.enums.PapelUsuario;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios")
@EqualsAndHashCode(callSuper = true)
public class UsuarioJpaEntity extends BaseJpaEntity {

    @Column
    private String nome;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private PapelUsuario papel = PapelUsuario.PACIENTE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "setor_id")
    private SetorJpaEntity setor;

    @Column(name = "ativo")
    @Builder.Default
    private Boolean ativo = true;
}
