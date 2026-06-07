-- Pedidos bomboniere: colunas novas em bases já existentes (idempotente)
ALTER TABLE tb_venda_produto ADD COLUMN IF NOT EXISTS status_pedido VARCHAR(20);
ALTER TABLE tb_venda_produto ADD COLUMN IF NOT EXISTS codigo_pedido VARCHAR(24);
ALTER TABLE tb_venda_produto ADD COLUMN IF NOT EXISTS cliente_id BIGINT;

UPDATE tb_venda_produto SET status_pedido = 'PENDENTE' WHERE status_pedido IS NULL;

UPDATE tb_venda_produto v
SET codigo_pedido = 'BOM-LEG-' || LPAD(v.id::text, 6, '0')
WHERE codigo_pedido IS NULL;

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_constraint WHERE conname = 'fk_venda_produto_cliente'
  ) THEN
    ALTER TABLE tb_venda_produto
      ADD CONSTRAINT fk_venda_produto_cliente
      FOREIGN KEY (cliente_id) REFERENCES tb_usuario(id);
  END IF;
END $$;

CREATE UNIQUE INDEX IF NOT EXISTS uk_venda_produto_codigo_pedido ON tb_venda_produto (codigo_pedido);

-- Reserva de assentos por sessão
CREATE TABLE IF NOT EXISTS tb_assento_reservado (
    id BIGSERIAL PRIMARY KEY,
    sessao_id BIGINT NOT NULL REFERENCES tb_sessao(id) ON DELETE CASCADE,
    venda_id BIGINT NOT NULL REFERENCES tb_venda_ingresso(id) ON DELETE CASCADE,
    codigo_assento VARCHAR(8) NOT NULL,
    CONSTRAINT uk_assento_sessao_codigo UNIQUE (sessao_id, codigo_assento)
);

-- OAuth e recuperação de senha
ALTER TABLE tb_usuario ADD COLUMN IF NOT EXISTS oauth_provider VARCHAR(20);
ALTER TABLE tb_usuario ADD COLUMN IF NOT EXISTS oauth_subject VARCHAR(255);

CREATE UNIQUE INDEX IF NOT EXISTS uk_usuario_oauth
  ON tb_usuario (oauth_provider, oauth_subject)
  WHERE oauth_provider IS NOT NULL AND oauth_subject IS NOT NULL;

CREATE TABLE IF NOT EXISTS tb_token_recuperacao_senha (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(64) NOT NULL UNIQUE,
    usuario_id BIGINT NOT NULL REFERENCES tb_usuario(id) ON DELETE CASCADE,
    expires_at TIMESTAMP NOT NULL,
    used_at TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_token_recuperacao_usuario
  ON tb_token_recuperacao_senha (usuario_id);

-- Pedidos de ingresso: código, status e cliente
ALTER TABLE tb_venda_ingresso ADD COLUMN IF NOT EXISTS codigo_pedido VARCHAR(24);
ALTER TABLE tb_venda_ingresso ADD COLUMN IF NOT EXISTS status_pedido VARCHAR(20);
ALTER TABLE tb_venda_ingresso ADD COLUMN IF NOT EXISTS cliente_id BIGINT;

UPDATE tb_venda_ingresso SET status_pedido = 'ENTREGUE' WHERE status_pedido IS NULL;

UPDATE tb_venda_ingresso v
SET codigo_pedido = 'ING-LEG-' || LPAD(v.id::text, 6, '0')
WHERE codigo_pedido IS NULL;

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_constraint WHERE conname = 'fk_venda_ingresso_cliente'
  ) THEN
    ALTER TABLE tb_venda_ingresso
      ADD CONSTRAINT fk_venda_ingresso_cliente
      FOREIGN KEY (cliente_id) REFERENCES tb_usuario(id);
  END IF;
END $$;

CREATE UNIQUE INDEX IF NOT EXISTS uk_venda_ingresso_codigo_pedido ON tb_venda_ingresso (codigo_pedido);
