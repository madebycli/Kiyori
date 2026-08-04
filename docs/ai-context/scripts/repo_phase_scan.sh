#!/usr/bin/env bash
set -Eeuo pipefail

echo "== Git state =="
git rev-parse --show-toplevel
git status --porcelain=v2 --branch
git remote -v
git branch -avv
git tag -n || true

echo "== Version =="
cat version.properties 2>/dev/null || true

echo "== Phase markers =="
markers=(
  "MainNavigationConfig"
  "MainNavigationShortcut"
  "CalendarDateRange"
  "CalendarListFilter"
  "CalendarMain"
  "SeasonMain"
  "MainNavigationSettingsView"
  "navori_splash_mark"
  "Phase4NavigationMigrationTest"
  "applicationContext.packageName"
)
for marker in "${markers[@]}"; do
  printf '\n-- %s --\n' "$marker"
  rg -n --hidden --glob '!.git/**' "$marker" . || true
done
