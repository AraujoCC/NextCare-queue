package com.laborwaze.queue_system.domain.model;

import com.laborwaze.queue_system.domain.enums.PapelUsuario;
import com.laborwaze.queue_system.domain.exception.BusinessRuleException;
import java.time.LocalDateTime;

public class Usuario extends BaseEntity {

    private String nome;
    private String login;
    private String senha;
    private String email;
    private PapelUsuario papel;
    private Setor setor;
    private Boolean ativo;

    public Usuario(String id, LocalDateTime createdAt, LocalDateTime updatedAt, String nome, String login, String senha, String email, PapelUsuario papel, Setor setor, Boolean ativo) {
        super(id, createdAt, updatedAt);
        if (login == null || login.isBlank()) throw new BusinessRuleException("Login é obrigatório");
        if (senha == null || senha.isBlank()) throw new BusinessRuleException("Senha é obrigatória");
        if (email == null || email.isBlank()) throw new BusinessRuleException("E-mail é obrigatório");

        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.email = email;
        this.papel = papel != null ? papel : PapelUsuario.PACIENTE;
        this.setor = setor;
        this.ativo = ativo != null ? ativo : true;
    }

    public String getNome() { return nome; }
    public String getLogin() { return login; }
    public String getSenha() { return senha; }
    public String getEmail() { return email; }
    public PapelUsuario getPapel() { return papel; }
    public Setor getSetor() { return setor; }
    public Boolean getAtivo() { return ativo; }
    
    public void desativar() {
        this.ativo = false;
    }
}
