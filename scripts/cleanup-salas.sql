-- Consolida em uma única sala "Tela 2D" e remove salas antigas/duplicadas.
-- Uso: PGPASSWORD=123456 psql -h localhost -U bruno -d cinepajeu -f scripts/cleanup-salas.sql

BEGIN;

INSERT INTO tb_sala (nome, capacidade)
SELECT 'Tela 2D', 200
WHERE NOT EXISTS (SELECT 1 FROM tb_sala WHERE nome = 'Tela 2D');

UPDATE tb_sessao
SET sala_id = (SELECT id FROM tb_sala WHERE nome = 'Tela 2D' ORDER BY id LIMIT 1)
WHERE sala_id IS NOT NULL
  AND sala_id NOT IN (SELECT id FROM tb_sala WHERE nome = 'Tela 2D');

DELETE FROM tb_sala
WHERE nome <> 'Tela 2D';

-- Mantém apenas uma linha Tela 2D (a de menor id)
DELETE FROM tb_sala s
WHERE s.nome = 'Tela 2D'
  AND s.id <> (SELECT MIN(id) FROM tb_sala WHERE nome = 'Tela 2D');

COMMIT;
