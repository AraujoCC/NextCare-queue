package com.laborwaze.queue_system.domain.model;

import com.laborwaze.queue_system.domain.exception.BusinessRuleException;
import java.time.LocalDateTime;

public class TentativaChamada extends BaseEntity {

    private Chamada chamada;
    private Sala sala;
    private LocalDateTime dataTentativa;
    private Boolean sucesso;
    private String observacao;

    public TentativaChamada(String id, LocalDateTime createdAt, LocalDateTime updatedAt, Chamada chamada, Sala sala, LocalDateTime dataTentativa, Boolean sucesso, String observacao) {
        super(id, createdAt, updatedAt);
        if (chamada == null) throw new BusinessRuleException("Chamada é obrigatória na tentativa");
        if (dataTentativa == null) throw new BusinessRuleException("Data da tentativa é obrigatória");
        
        this.chamada = chamada;
        this.sala = sala;
        this.dataTentativa = dataTentativa;
        this.sucesso = sucesso != null ? sucesso : false;
        this.observacao = observacao;
    }

    public Chamada getChamada() { return chamada; }
    public Sala getSala() { return sala; }
    public LocalDateTime getDataTentativa() { return dataTentativa; }
    public Boolean getSucesso() { return sucesso; }
    public String getObservacao() { return observacao; }
    
    public void marcarSucesso() {
        this.sucesso = true;
    }
}
