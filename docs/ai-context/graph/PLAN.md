# PLAN.md

## Ziel

Das bisher prose-lastige `docs/ai-context` in einen repo-nativen, typisierten Knowledge Graph überführen. Eine neue KI soll aktuellen Zustand, Verträge, Entscheidungen und Historie gezielt traversieren können, ohne veraltete Recovery- oder SHA-Angaben mit aktuellem Repository-Zustand zu vermischen.

## Anforderungen

- Ein einziger maschinenlesbarer Einstiegspunkt.
- Aktuelle Fakten klar von Historie und Evidence trennen.
- Explizite Beziehungen statt impliziter Verweise in langen Markdown-Dateien.
- Volatile Fakten wie SHAs, Branches, CI, Version und Package vor Aktionen frisch verifizieren.
- Bestehende Markdown-Historie erhalten.
- Keine externe Datenbank oder Cloud-Abhängigkeit.
- Offline und in CI validierbar.
- Git-diffbar und für beliebige KI-Agenten lesbar.
- Skalierbar auf weitere Feature-, CI-, Release-, Issue- und Architektur-Nodes.
- Keine sensiblen Tokens, Secrets oder personenbezogenen Daten im Graph speichern.

## Architektur

Gewählt: repo-nativer JSON Property Graph.

Komponenten und Datenfluss:

`AI -> graph/index.json -> relevante current Nodes -> Relations -> optional Evidence/Legacy -> Live-Repo-Verifikation -> Änderung -> Validator -> Commit`

JSON-Nodes sind die kanonische maschinenlesbare Form. JSON Schema dokumentiert das Format. Ein Python-Validator prüft Index, IDs, Referenzen und Metadaten ohne zusätzliche Pakete. Markdown bleibt als menschlich lesbare Erklärung und historische Evidence erhalten.

Alternative 1 war ein YAML-Graph. Er ist etwas angenehmer handschriftlich zu bearbeiten, benötigt aber einen YAML-Parser und besitzt mehr Parser-Sonderfälle.

Alternative 2 war Neo4j plus Vector-RAG. Das skaliert für sehr große Wissensbasen stärker, verursacht hier aber zusätzliche Infrastruktur, Kosten, Synchronisationsprobleme und eine zweite externe Source of Truth. Für ein einzelnes Repository wäre das Overengineering.

## Dateistruktur

```text
docs/ai-context/
├── README.md
├── AI_SESSION_PROMPT.md
└── graph/
    ├── README.md
    ├── PLAN.md
    ├── index.json
    ├── schema/
    │   └── node.schema.json
    └── nodes/
        ├── project.kiyori.json
        ├── repo.main-state.json
        ├── contract.context-authority.json
        ├── contract.repository-safety.json
        ├── contract.product-invariants.json
        ├── policy.upstream-sync.json
        └── history.legacy-context.json

tools/
└── validate_ai_context_graph.py
```

## Umsetzungsschritte

1. Aktuellen `main` frisch verifizieren und einen isolierten Migrationsbranch erstellen.
2. Graphformat, Authority-Regeln und Traversal-Policy definieren.
3. Aktuelle Repo-Fakten ausschließlich aus frisch geprüften Code- und GitHub-Quellen übernehmen.
4. Bestehende Architektur-, Decision-, Failure- und Upstream-Dokumente als historische Evidence verknüpfen.
5. `index.json` als einzigen Standard-Einstiegspunkt erzeugen.
6. JSON Schema und Zero-Dependency-Validator hinzufügen.
7. `docs/ai-context/README.md` auf graph-first umstellen.
8. Einen wiederverwendbaren AI-Session-Prompt hinzufügen.
9. Validator ausführen und Graphreferenzen prüfen.
10. Änderungen nur im Migrationsbranch committen. `main` bleibt unangetastet, bis ein Merge ausdrücklich freigegeben wird.

## Offene Fragen / Unklarheiten

- UNKLAR: Welche zusätzlichen Feature-Nodes aus den langen historischen Dateien sollen nach dem Bootstrap als eigene current Nodes extrahiert werden. Das kann inkrementell erfolgen, wenn die jeweiligen Features wieder bearbeitet werden.
- UNKLAR: Ob der Graph später automatisch aus CI-/Release-Metadaten aktualisiert werden soll. Für den Bootstrap bleibt Live-Verifikation bewusst eine Agentenpflicht, damit kein Bot ungeprüft Repository-Zustand umschreibt.
