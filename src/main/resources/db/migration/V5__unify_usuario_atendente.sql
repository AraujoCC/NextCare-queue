-- Unify Usuario and Atendente:
-- 1. Add nome and setor_id to usuarios
ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS nome VARCHAR(255);
ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS setor_id VARCHAR(36);

-- 2. Add FK constraint for setor_id
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_usuarios_setor') THEN
        ALTER TABLE usuarios ADD CONSTRAINT fk_usuarios_setor
            FOREIGN KEY (setor_id) REFERENCES setores(id);
    END IF;
END $$;

-- 3. Add ATENDENTE to the papel check constraint
ALTER TABLE usuarios DROP CONSTRAINT IF EXISTS chk_usuarios_papel;
ALTER TABLE usuarios ADD CONSTRAINT chk_usuarios_papel
    CHECK (papel IN ('PACIENTE', 'ADMIN', 'SUPERVISOR', 'ATENDENTE'));

-- 4. Update FK on chamadas to reference usuarios instead of atendentes
ALTER TABLE chamadas DROP CONSTRAINT IF EXISTS fk_chamadas_atendente;
ALTER TABLE chamadas ADD CONSTRAINT fk_chamadas_atendente
    FOREIGN KEY (atendente_id) REFERENCES usuarios(id);

-- 5. Drop table atendentes
DROP TABLE IF EXISTS atendentes;
