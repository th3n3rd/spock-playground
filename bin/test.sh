#!/bin/bash

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" &> /dev/null && pwd)"

cd "$SCRIPT_DIR/.."

./mvnw clean test

mkdir docs &> /dev/null || true
rm -f docs/* &> /dev/null || true
cp target/spring-modulith-docs/* docs/
