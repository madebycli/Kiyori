# Kiyori AI Context Graph

This directory is the authoritative entry point for AI context in Kiyori.

## Why this exists

The old `docs/ai-context/*.md` model accumulated useful history, but prose snapshots can become stale and can contradict the live repository. The graph separates current contracts, volatile observations, policies and historical evidence so an AI does not have to guess which document wins.

## Canonical format

The source of truth is a small JSON property graph:

- `index.json` is the only default entry point.
- `nodes/*.json` contain typed context nodes.
- `schema/node.schema.json` defines the node shape.
- Legacy Markdown remains in `docs/ai-context/` as evidence and recovery history.
- `tools/validate_ai_context_graph.py` validates structure, index coverage and graph references without third-party Python packages.

JSON was chosen over YAML because it is deterministic, universally parseable, directly compatible with JSON Schema and needs no extra parser dependency in CI or agent environments.

## Reading protocol for an AI

1. Read `index.json`.
2. Load the entry nodes relevant to the task.
3. Follow graph relations only as needed, normally no deeper than the configured default depth.
4. Do not recursively load all legacy Markdown.
5. Treat `status=current` as current intent. Historical and deprecated nodes are provenance, not instructions.
6. For volatile facts such as SHAs, branches, CI state, version, package identity and releases, verify the live repository before acting.
7. If two current primary nodes conflict, output `CONFLICT:` with both node IDs and verify the underlying source. Never guess.
8. If required information is absent, output `UNKLAR:` and verify it before destructive operations.

## Authority

The default authority order is:

1. Fresh live repository or CI evidence
2. Current primary graph nodes
3. Current secondary graph nodes
4. Historical or evidence nodes
5. Unlinked legacy Markdown

Fresh evidence may make a graph snapshot stale. In that case, update the smallest affected node in the same work branch.

## Writing protocol

- One concept or contract per node.
- Prefer explicit relations over duplicated prose.
- Current volatile facts require evidence.
- Preserve history by changing status or adding `supersedes` relations instead of rewriting history.
- Never turn a historical node back into current truth without fresh verification.
- Run `python tools/validate_ai_context_graph.py` after graph changes.

## Relation vocabulary

Relations are intentionally extensible. Common relations include:

- `depends_on`
- `protects`
- `protected_by`
- `implemented_by`
- `verified_by`
- `derived_from`
- `informs`
- `conflicts_with`
- `supersedes`
- `superseded_for_current_truth_by`
- `entrypoints`
- `has_history`

A relation target must resolve to a node in `index.json`.
