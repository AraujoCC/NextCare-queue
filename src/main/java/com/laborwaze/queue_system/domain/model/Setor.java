package com.laborwaze.queue_system.domain.model;

import com.laborwaze.queue_system.domain.exception.BusinessRuleException;
import java.time.LocalDateTime;

public class Setor extends BaseEntity {

    private String nome;
    private String descricao;
    private Boolean ativo;

    public Setor(String id, LocalDateTime createdAt, LocalDateTime updatedAt, String nome, String descricao, Boolean ativo) {
        super(id, createdAt, updatedAt);
        if (nome == null || nome.isBlank()) {
            throw new BusinessRuleException("Nome do setor não pode ser vazio");
        }
        this.nome = nome;
        this.descricao = descricao;
        this.ativo = ativo != null ? ativo : true;
    }

    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public Boolean getAtivo() { return ativo; }
}
