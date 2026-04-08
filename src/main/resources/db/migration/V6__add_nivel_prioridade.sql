-- Convert prioridade from BOOLEAN to VARCHAR enum
ALTER TABLE chamadas ADD COLUMN prioridade_new VARCHAR(20);

-- Migrate existing data
UPDATE chamadas SET prioridade_new = CASE
    WHEN prioridade = true THEN 'PRIORIDADE'
    ELSE 'NORMAL'
END;

-- Drop old column and rename new one
ALTER TABLE chamadas DROP COLUMN prioridade;
ALTER TABLE chamadas RENAME COLUMN prioridade_new TO prioridade;
