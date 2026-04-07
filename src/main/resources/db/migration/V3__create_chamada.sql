-- servicos table (needed as a FK target for chamadas)
CREATE TABLE servicos (
    id VARCHAR(36) PRIMARY KEY,
    nome VARCHAR NOT NULL,
    codigo VARCHAR UNIQUE NOT NULL,
    descricao TEXT,
    tempo_medio_atendimento INTEGER,
    ativo BOOLEAN DEFAULT true,
    setor_id VARCHAR(36),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_servicos_setor FOREIGN KEY (setor_id) REFERENCES setores(id)
);

CREATE INDEX idx_servicos_codigo ON servicos(codigo);
CREATE INDEX idx_servicos_setor_id ON servicos(setor_id);

-- chamadas table
CREATE TABLE chamadas (
    id VARCHAR(36) PRIMARY KEY,
    senha VARCHAR UNIQUE NOT NULL,
    paciente_id VARCHAR(36) NOT NULL,
    servico_id VARCHAR(36) NOT NULL,
    atendente_id VARCHAR(36),
    status VARCHAR NOT NULL DEFAULT 'AGUARDANDO',
    prioridade BOOLEAN DEFAULT false,
    data_chamada TIMESTAMP NOT NULL,
    data_inicio_atendimento TIMESTAMP,
    data_fim_atendimento TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_chamadas_paciente FOREIGN KEY (paciente_id) REFERENCES pacientes(id),
    CONSTRAINT fk_chamadas_servico FOREIGN KEY (servico_id) REFERENCES servicos(id),
    CONSTRAINT fk_chamadas_atendente FOREIGN KEY (atendente_id) REFERENCES atendentes(id)
);

CREATE INDEX idx_chamadas_senha ON chamadas(senha);
CREATE INDEX idx_chamadas_paciente_id ON chamadas(paciente_id);
CREATE INDEX idx_chamadas_servico_id ON chamadas(servico_id);
CREATE INDEX idx_chamadas_atendente_id ON chamadas(atendente_id);
CREATE INDEX idx_chamadas_status ON chamadas(status);
