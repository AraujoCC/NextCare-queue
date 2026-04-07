CREATE TABLE tentativas_chamada (
    id VARCHAR(36) PRIMARY KEY,
    chamada_id VARCHAR(36) NOT NULL,
    sala_id VARCHAR(36),
    data_tentativa TIMESTAMP NOT NULL,
    sucesso BOOLEAN DEFAULT false,
    observacao VARCHAR,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_tentativas_chamada FOREIGN KEY (chamada_id) REFERENCES chamadas(id),
    CONSTRAINT fk_tentativas_sala FOREIGN KEY (sala_id) REFERENCES salas(id)
);

CREATE INDEX idx_tentativas_chamada_id ON tentativas_chamada(chamada_id);
CREATE INDEX idx_tentativas_sala_id ON tentativas_chamada(sala_id);
