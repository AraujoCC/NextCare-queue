package com.laborwaze.queue_system.domain.model;

import com.laborwaze.queue_system.domain.exception.BusinessRuleException;
import java.time.LocalDateTime;

public class Servico extends BaseEntity {

    private String nome;
    private String codigo;
    private String descricao;
    private Integer tempoMedioAtendimento;
    private Setor setor;
    private Boolean ativo;

    public Servico(String id, LocalDateTime createdAt, LocalDateTime updatedAt, String nome, String codigo, String descricao, Integer tempoMedioAtendimento, Setor setor, Boolean ativo) {
        super(id, createdAt, updatedAt);
        if (nome == null || nome.isBlank()) throw new BusinessRuleException("Nome do serviço é obrigatório");
        if (codigo == null || codigo.isBlank()) throw new BusinessRuleException("Código do serviço é obrigatório");
        if (setor == null) throw new BusinessRuleException("Setor é obrigatório para o serviço");

        this.nome = nome;
        this.codigo = codigo;
        this.descricao = descricao;
        this.tempoMedioAtendimento = tempoMedioAtendimento;
        this.setor = setor;
        this.ativo = ativo != null ? ativo : true;
    }

    public String getNome() { return nome; }
    public String getCodigo() { return codigo; }
    public String getDescricao() { return descricao; }
    public Integer getTempoMedioAtendimento() { return tempoMedioAtendimento; }
    public Setor getSetor() { return setor; }
    public Boolean getAtivo() { return ativo; }
}
