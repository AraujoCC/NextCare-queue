package com.laborwaze.queue_system.domain.model;

import com.laborwaze.queue_system.domain.exception.BusinessRuleException;
import java.time.LocalDateTime;

public class Paciente extends BaseEntity {

    private String nome;
    private String cpf;
    private String email;
    private String telefone;

    public Paciente(String id, LocalDateTime createdAt, LocalDateTime updatedAt, String nome, String cpf, String email, String telefone) {
        super(id, createdAt, updatedAt);
        if (nome == null || nome.isBlank()) throw new BusinessRuleException("Nome do paciente é obrigatório");
        if (cpf == null || cpf.isBlank()) throw new BusinessRuleException("CPF é obrigatório");
        if (email == null || email.isBlank()) throw new BusinessRuleException("E-mail é obrigatório");
        
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
    }

    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public String getEmail() { return email; }
    public String getTelefone() { return telefone; }
}