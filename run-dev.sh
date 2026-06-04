#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"

if [[ -f .env ]]; then
  set -a
  # shellcheck disable=SC1091
  source .env
  set +a
fi

export DB_HOST="${DB_HOST:-localhost}"
export DB_PORT="${DB_PORT:-5432}"
export DB_NAME="${DB_NAME:-conectapajeu}"
export DB_USERNAME="${DB_USERNAME:-bruno}"
export DB_PASSWORD="${DB_PASSWORD:-123456}"

echo "API → jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME} (user: ${DB_USERNAME})"

if [[ -x ./mvnw ]]; then
  exec ./mvnw spring-boot:run
else
  exec mvn spring-boot:run
fi
