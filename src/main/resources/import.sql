-- Administrador principal (login: pajeuticket, senha: Ticket202!!)
INSERT INTO tb_usuario (nome, login, senha, role, ultimo_acesso)
VALUES ('Administrador Pajeu', 'pajeuticket', '$2b$10$ccmzPQBPT848PkoruUbJ.OsR7bExBC3BnAVoCuc/xsY5Ue.ehoQGq', 'ADMIN', CURRENT_TIMESTAMP)
ON CONFLICT (login) DO NOTHING;

-- Única sala do cinema (tela 2D)
INSERT INTO tb_sala (nome, capacidade) VALUES ('Tela 2D', 200) ON CONFLICT DO NOTHING;

-- Inserção de Filmes
INSERT INTO tb_filme (titulo, genero, classificacao, duracao, sinopse, status)
VALUES ('O Poderoso Chefão', 'Drama/Policial', '16', 175, 'O patriarca de uma dinastia do crime organizado transfere o controle de seu império clandestino para seu filho relutante.', true)
ON CONFLICT DO NOTHING;

INSERT INTO tb_filme (titulo, genero, classificacao, duracao, sinopse, status)
VALUES ('Interestelar', 'Ficção Científica', 'Livre', 169, 'Uma equipe de exploradores viaja através de um buraco de minhoca no espaço na tentativa de garantir a sobrevivência da humanidade.', true)
ON CONFLICT DO NOTHING;

-- Inserção de Produtos (Bomboniere)
INSERT INTO tb_produto (nome, categoria, descricao, preco, quantidade_estoque, estoque_minimo, ativo)
VALUES ('Pipoca Salgada Grande', 'Pipoca', 'Pipoca salgada tamanho grande para compartilhar.', 25.00, 100, 15, true)
ON CONFLICT DO NOTHING;

INSERT INTO tb_produto (nome, categoria, descricao, preco, quantidade_estoque, estoque_minimo, ativo)
VALUES ('Pipoca Doce Média', 'Pipoca', 'Pipoca doce tamanho médio.', 22.00, 80, 15, true)
ON CONFLICT DO NOTHING;

INSERT INTO tb_produto (nome, categoria, descricao, preco, quantidade_estoque, estoque_minimo, ativo)
VALUES ('Refrigerante Lata 350ml', 'Refrigerantes', 'Refrigerante em lata 350 ml.', 8.00, 200, 30, true)
ON CONFLICT DO NOTHING;

INSERT INTO tb_produto (nome, categoria, descricao, preco, quantidade_estoque, estoque_minimo, ativo)
VALUES ('Chocolate Barra 90g', 'Doces', 'Barra de chocolate 90 g.', 12.00, 50, 10, true)
ON CONFLICT DO NOTHING;

-- Sessões de exemplo (sala única 2D)
INSERT INTO tb_sessao (filme_id, sala_id, data, horario, valor_ingresso, lugares_disponiveis)
SELECT 1, (SELECT id FROM tb_sala ORDER BY id LIMIT 1), CURRENT_DATE, '19:30:00', 25.00, 80
WHERE EXISTS (SELECT 1 FROM tb_filme WHERE id = 1)
  AND EXISTS (SELECT 1 FROM tb_sala LIMIT 1)
  AND NOT EXISTS (SELECT 1 FROM tb_sessao WHERE filme_id = 1 AND data = CURRENT_DATE AND horario = '19:30:00');

INSERT INTO tb_sessao (filme_id, sala_id, data, horario, valor_ingresso, lugares_disponiveis)
SELECT 2, (SELECT id FROM tb_sala ORDER BY id LIMIT 1), CURRENT_DATE, '21:00:00', 30.00, 50
WHERE EXISTS (SELECT 1 FROM tb_filme WHERE id = 2)
  AND EXISTS (SELECT 1 FROM tb_sala LIMIT 1)
  AND NOT EXISTS (SELECT 1 FROM tb_sessao WHERE filme_id = 2 AND data = CURRENT_DATE AND horario = '21:00:00');
