-- Ensure additional papel values and add any papel-related constraints.
-- The papel column already exists on usuarios with a default of 'PACIENTE'.
-- Add a check constraint to enforce valid papel values.
ALTER TABLE usuarios
    ADD CONSTRAINT chk_usuarios_papel
    CHECK (papel IN ('PACIENTE', 'ADMIN', 'SUPERVISOR'));

-- setores table
CREATE TABLE setores (
    id VARCHAR(36) PRIMARY KEY,
    nome VARCHAR UNIQUE NOT NULL,
    descricao VARCHAR,
    ativo BOOLEAN DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- atendentes table
CREATE TABLE atendentes (
    id VARCHAR(36) PRIMARY KEY,
    nome VARCHAR NOT NULL,
    email VARCHAR UNIQUE NOT NULL,
    senha VARCHAR NOT NULL,
    papel VARCHAR NOT NULL DEFAULT 'ATENDENTE',
    ativo BOOLEAN DEFAULT true,
    setor_id VARCHAR(36),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_atendentes_setor FOREIGN KEY (setor_id) REFERENCES setores(id)
);

CREATE INDEX idx_atendentes_email ON atendentes(email);
CREATE INDEX idx_atendentes_setor_id ON atendentes(setor_id);
CREATE INDEX idx_atendentes_papel ON atendentes(papel);
CREATE INDEX idx_setores_nome ON setores(nome);
