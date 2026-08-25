# Kiyori AI Context

> Canonical AI entry point: [`graph/index.json`](graph/index.json)

Kiyori uses a repo-native typed knowledge graph for AI context. **Do not start a session by recursively reading every Markdown file in this directory.**

## Start here

1. Read `graph/index.json`.
2. Read `graph/nodes/contract.context-authority.json`.
3. Read `graph/nodes/contract.repository-safety.json`.
4. Load only the current nodes relevant to the task.
5. Follow explicit relations as needed.
6. Use legacy Markdown only as linked evidence, recovery history or conflict context.

The reusable session instruction is in [`AI_SESSION_PROMPT.md`](AI_SESSION_PROMPT.md).

## Why the old files still exist

The previous Markdown context contains valuable recovery history, decisions, failure reports and upstream handoffs. Those files are intentionally preserved, but they are no longer equal-authority current truth. The graph classifies and links them as evidence so stale SHAs, package targets, branches or old scope decisions cannot silently override fresh repository state.

## Validation

Run:

```bash
python tools/validate_ai_context_graph.py
```

The validator requires only the Python standard library.

## Human documentation

See [`graph/README.md`](graph/README.md) for the graph contract and [`graph/PLAN.md`](graph/PLAN.md) for the migration design.
