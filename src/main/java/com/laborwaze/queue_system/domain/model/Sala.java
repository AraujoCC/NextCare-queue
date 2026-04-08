package com.laborwaze.queue_system.domain.model;

import com.laborwaze.queue_system.domain.exception.BusinessRuleException;
import java.time.LocalDateTime;

public class Sala extends BaseEntity {

    private String nome;
    private String numero;
    private String descricao;
    private Boolean ativo;

    public Sala(String id, LocalDateTime createdAt, LocalDateTime updatedAt, String nome, String numero, String descricao, Boolean ativo) {
        super(id, createdAt, updatedAt);
        if (nome == null || nome.isBlank()) throw new BusinessRuleException("Nome da sala é obrigatório");
        this.nome = nome;
        this.numero = numero;
        this.descricao = descricao;
        this.ativo = ativo != null ? ativo : true;
    }

    public String getNome() { return nome; }
    public String getNumero() { return numero; }
    public String getDescricao() { return descricao; }
    public Boolean getAtivo() { return ativo; }
    
    public void desativar() {
        this.ativo = false;
    }
}
