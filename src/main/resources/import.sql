-- Seed inicial (rodar UMA VEZ manualmente, ex.: psql -f import.sql)
-- Não é executado automaticamente pelo Spring Boot.

-- Administrador principal (login: pajeuticket, senha: Ticket202!!)
INSERT INTO tb_usuario (nome, login, senha, role, ultimo_acesso)
VALUES ('Administrador Pajeu', 'pajeuticket', '$2b$10$ccmzPQBPT848PkoruUbJ.OsR7bExBC3BnAVoCuc/xsY5Ue.ehoQGq', 'ADMIN', CURRENT_TIMESTAMP)
ON CONFLICT (login) DO NOTHING;

-- Única sala do cinema (tela 2D)
INSERT INTO tb_sala (nome, capacidade)
SELECT 'Tela 2D', 200
WHERE NOT EXISTS (SELECT 1 FROM tb_sala WHERE nome = 'Tela 2D');

-- Filmes de exemplo
INSERT INTO tb_filme (titulo, genero, classificacao, duracao, sinopse, status)
SELECT 'O Poderoso Chefão', 'Drama/Policial', '16', 175,
       'O patriarca de uma dinastia do crime organizado transfere o controle de seu império clandestino para seu filho relutante.',
       true
WHERE NOT EXISTS (SELECT 1 FROM tb_filme WHERE titulo = 'O Poderoso Chefão');

INSERT INTO tb_filme (titulo, genero, classificacao, duracao, sinopse, status)
SELECT 'Interestelar', 'Ficção Científica', 'Livre', 169,
       'Uma equipe de exploradores viaja através de um buraco de minhoca no espaço na tentativa de garantir a sobrevivência da humanidade.',
       true
WHERE NOT EXISTS (SELECT 1 FROM tb_filme WHERE titulo = 'Interestelar');

-- Produtos bomboniere
INSERT INTO tb_produto (nome, categoria, descricao, preco, quantidade_estoque, estoque_minimo, ativo)
SELECT 'Pipoca Salgada Grande', 'Pipoca', 'Pipoca salgada tamanho grande para compartilhar.', 25.00, 100, 15, true
WHERE NOT EXISTS (SELECT 1 FROM tb_produto WHERE nome = 'Pipoca Salgada Grande' AND categoria = 'Pipoca');

INSERT INTO tb_produto (nome, categoria, descricao, preco, quantidade_estoque, estoque_minimo, ativo)
SELECT 'Pipoca Doce Média', 'Pipoca', 'Pipoca doce tamanho médio.', 22.00, 80, 15, true
WHERE NOT EXISTS (SELECT 1 FROM tb_produto WHERE nome = 'Pipoca Doce Média' AND categoria = 'Pipoca');

INSERT INTO tb_produto (nome, categoria, descricao, preco, quantidade_estoque, estoque_minimo, ativo)
SELECT 'Refrigerante Lata 350ml', 'Refrigerantes', 'Refrigerante em lata 350 ml.', 8.00, 200, 30, true
WHERE NOT EXISTS (SELECT 1 FROM tb_produto WHERE nome = 'Refrigerante Lata 350ml' AND categoria = 'Refrigerantes');

INSERT INTO tb_produto (nome, categoria, descricao, preco, quantidade_estoque, estoque_minimo, ativo)
SELECT 'Chocolate Barra 90g', 'Doces', 'Barra de chocolate 90 g.', 12.00, 50, 10, true
WHERE NOT EXISTS (SELECT 1 FROM tb_produto WHERE nome = 'Chocolate Barra 90g' AND categoria = 'Doces');

-- Sessões de exemplo (vinculadas ao filme pelo título)
INSERT INTO tb_sessao (filme_id, sala_id, data, horario, valor_ingresso, lugares_disponiveis)
SELECT f.id, s.id, CURRENT_DATE, '19:30:00', 25.00, 80
FROM tb_filme f
CROSS JOIN (SELECT id FROM tb_sala ORDER BY id LIMIT 1) s(id)
WHERE f.titulo = 'O Poderoso Chefão'
  AND NOT EXISTS (
    SELECT 1 FROM tb_sessao WHERE filme_id = f.id AND data = CURRENT_DATE AND horario = '19:30:00'
  );

INSERT INTO tb_sessao (filme_id, sala_id, data, horario, valor_ingresso, lugares_disponiveis)
SELECT f.id, s.id, CURRENT_DATE, '21:00:00', 30.00, 50
FROM tb_filme f
CROSS JOIN (SELECT id FROM tb_sala ORDER BY id LIMIT 1) s(id)
WHERE f.titulo = 'Interestelar'
  AND NOT EXISTS (
    SELECT 1 FROM tb_sessao WHERE filme_id = f.id AND data = CURRENT_DATE AND horario = '21:00:00'
  );
