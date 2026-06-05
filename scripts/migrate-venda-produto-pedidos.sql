-- Execute se a API falhar ao subir por status_pedido em tb_venda_produto:
-- psql -U bruno -d cinepajeu -f scripts/migrate-venda-produto-pedidos.sql

\i src/main/resources/schema-patch.sql
