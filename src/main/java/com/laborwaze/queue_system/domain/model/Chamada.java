package com.laborwaze.queue_system.domain.model;

import com.laborwaze.queue_system.domain.enums.NivelPrioridade;
import com.laborwaze.queue_system.domain.enums.StatusChamada;
import com.laborwaze.queue_system.domain.exception.BusinessRuleException;

import java.time.LocalDateTime;

public class Chamada extends BaseEntity {

    private String senha;
    private Paciente paciente;
    private Servico servico;
    private Usuario atendente;
    private StatusChamada status;
    private NivelPrioridade prioridade;
    private LocalDateTime dataChamada;
    private LocalDateTime dataInicioAtendimento;
    private LocalDateTime dataFimAtendimento;

    // Construtor completo usado na reconstituição via db mapper
    public Chamada(String id, LocalDateTime createdAt, LocalDateTime updatedAt, String senha, Paciente paciente, 
                   Servico servico, Usuario atendente, StatusChamada status, NivelPrioridade prioridade, 
                   LocalDateTime dataChamada, LocalDateTime dataInicioAtendimento, LocalDateTime dataFimAtendimento) {
        super(id, createdAt, updatedAt);
        if (senha == null || senha.isBlank()) throw new BusinessRuleException("Senha é obrigatória");
        if (paciente == null) throw new BusinessRuleException("Paciente é obrigatório");
        if (servico == null) throw new BusinessRuleException("Serviço é obrigatório");
        
        this.senha = senha;
        this.paciente = paciente;
        this.servico = servico;
        this.atendente = atendente;
        this.status = status != null ? status : StatusChamada.AGUARDANDO;
        this.prioridade = prioridade != null ? prioridade : NivelPrioridade.NORMAL;
        this.dataChamada = dataChamada != null ? dataChamada : LocalDateTime.now();
        this.dataInicioAtendimento = dataInicioAtendimento;
        this.dataFimAtendimento = dataFimAtendimento;
    }

    // Factory method internalizando regra de negócio da geração de senha
    public static Chamada criarGeraSenha(Paciente paciente, Servico servico, NivelPrioridade prioridade, long totalAguardando) {
        String prefixo = (servico.getCodigo() != null && !servico.getCodigo().isEmpty()) 
            ? servico.getCodigo().substring(0, 1).toUpperCase() 
            : "A";
        String senhaGerada = String.format("%s%03d", prefixo, totalAguardando + 1);
        
        return new Chamada(null, null, null, senhaGerada, paciente, servico, null, 
                           StatusChamada.AGUARDANDO, prioridade, LocalDateTime.now(), null, null);
    }

    public void chamarParaAtendimento(Usuario atendente) {
        if (this.status != StatusChamada.AGUARDANDO) {
            throw new BusinessRuleException("Apenas chamadas aguardando podem ser iniciadas.");
        }
        if (atendente == null) {
            throw new BusinessRuleException("Atendente é obrigatório para iniciar atendimento.");
        }
        this.status = StatusChamada.EM_ATENDIMENTO;
        this.atendente = atendente;
        this.dataInicioAtendimento = LocalDateTime.now();
    }

    public void finalizar() {
        if (this.status != StatusChamada.EM_ATENDIMENTO) {
            throw new BusinessRuleException("Apenas chamadas em atendimento podem ser finalizadas.");
        }
        this.status = StatusChamada.FINALIZADA;
        this.dataFimAtendimento = LocalDateTime.now();
    }

    public void cancelar() {
        if (this.status == StatusChamada.FINALIZADA) {
            throw new BusinessRuleException("Chamadas finalizadas não podem ser canceladas.");
        }
        this.status = StatusChamada.CANCELADA;
    }

    public String getSenha() { return senha; }
    public Paciente getPaciente() { return paciente; }
    public Servico getServico() { return servico; }
    public Usuario getAtendente() { return atendente; }
    public StatusChamada getStatus() { return status; }
    public NivelPrioridade getPrioridade() { return prioridade; }
    public LocalDateTime getDataChamada() { return dataChamada; }
    public LocalDateTime getDataInicioAtendimento() { return dataInicioAtendimento; }
    public LocalDateTime getDataFimAtendimento() { return dataFimAtendimento; }
}
