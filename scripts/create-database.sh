#!/usr/bin/env bash
set -euo pipefail

DB_NAME="${DB_NAME:-cinepajeu}"
DB_USER="${DB_USERNAME:-bruno}"
DB_PASSWORD="${DB_PASSWORD:-123456}"
DB_HOST="${DB_HOST:-localhost}"

export PGPASSWORD="$DB_PASSWORD"

echo "→ Verificando se o banco '${DB_NAME}' já existe..."
if psql -h "$DB_HOST" -U "$DB_USER" -d "$DB_NAME" -c "SELECT 1" &>/dev/null; then
  echo "✓ Banco '${DB_NAME}' já existe e está acessível para ${DB_USER}."
  exit 0
fi

echo "→ Tentando criar com usuário ${DB_USER}..."
if createdb -h "$DB_HOST" -U "$DB_USER" -O "$DB_USER" "$DB_NAME" 2>/dev/null; then
  echo "✓ Banco '${DB_NAME}' criado por ${DB_USER}."
  exit 0
fi

echo "→ ${DB_USER} não tem permissão CREATEDB. Tentando via superusuário postgres (sudo)..."
if sudo -n -u postgres psql -v ON_ERROR_STOP=1 <<SQL 2>/dev/null; then
ALTER ROLE ${DB_USER} CREATEDB;
DO \$\$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_database WHERE datname = '${DB_NAME}') THEN
    EXECUTE format('CREATE DATABASE %I OWNER %I ENCODING ''UTF8'' TEMPLATE template0', '${DB_NAME}', '${DB_USER}');
  END IF;
END
\$\$;
GRANT ALL PRIVILEGES ON DATABASE ${DB_NAME} TO ${DB_USER};
SQL
  echo "✓ Banco '${DB_NAME}' criado via postgres (sudo)."
  exit 0
fi

echo ""
echo "Não foi possível criar automaticamente (sudo pede senha)."
echo "Execute manualmente no terminal:"
echo ""
echo "  sudo -u postgres psql -f scripts/create-database.sql"
echo ""
echo "Depois atualize .env: DB_NAME=cinepajeu"
exit 1
