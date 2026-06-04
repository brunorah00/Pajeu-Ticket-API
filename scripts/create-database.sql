-- Rodar como superusuário postgres:
--   sudo -u postgres psql -f scripts/create-database.sql

ALTER ROLE bruno CREATEDB;

DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_database WHERE datname = 'cinepajeu') THEN
    CREATE DATABASE cinepajeu OWNER bruno ENCODING 'UTF8' TEMPLATE template0;
  END IF;
END
$$;

GRANT ALL PRIVILEGES ON DATABASE cinepajeu TO bruno;
