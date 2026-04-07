package com.laborwaze.queue_system.domain.model;

import com.laborwaze.queue_system.domain.enums.PapelUsuario;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios")
@EqualsAndHashCode(callSuper = true)
public class Usuario extends BaseEntity {

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

    @Column(name = "ativo")
    @Builder.Default
    private Boolean ativo = true;
}
