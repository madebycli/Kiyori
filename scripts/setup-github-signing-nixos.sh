#!/usr/bin/env bash
set -Eeuo pipefail

command -v nix-shell >/dev/null 2>&1 || {
  echo "nix-shell was not found. Install or enable Nix first." >&2
  exit 2
}

REPOSITORY="${1:-madebycli/Kiyori}"
OUTPUT_DIR="${2:-$HOME/.local/share/kiyori-signing}"
SCRIPT_DIR="$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)"

export KIYORI_SIGNING_REPOSITORY="$REPOSITORY"
export KIYORI_SIGNING_OUTPUT_DIR="$OUTPUT_DIR"
export KIYORI_SIGNING_SCRIPT="$SCRIPT_DIR/setup-github-signing.sh"

exec nix-shell \
  -p bash git gh jdk17_headless coreutils gnugrep \
  --run 'bash "$KIYORI_SIGNING_SCRIPT" "$KIYORI_SIGNING_REPOSITORY" "$KIYORI_SIGNING_OUTPUT_DIR"'
