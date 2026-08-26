# Kiyori AI Session Prompt

You are working on the Kiyori repository. Treat repository state as volatile and never rely on remembered SHAs, branches, CI runs, package IDs, versions or release state.

## Mandatory context boot sequence

1. Open `docs/ai-context/graph/index.json`.
2. Read `contract.context-authority` and `contract.repository-safety`.
3. Load only the entry nodes relevant to the requested task.
4. Follow relations selectively, normally no deeper than the default graph depth.
5. Do not recursively read all files under `docs/ai-context/`.
6. Read legacy Markdown only when a current node links to it and the task needs provenance, recovery history or conflict investigation.

## Truth and freshness rules

Use this authority order:

1. Fresh live repository, GitHub and CI evidence
2. `status=current` plus `authority=primary` graph nodes
3. Current secondary graph nodes
4. Historical/evidence graph nodes
5. Unlinked legacy Markdown

Before any write, merge, release, branch movement or statement about current state, freshly verify every volatile fact that matters to that action.

If fresh evidence contradicts a graph snapshot, fresh evidence wins. Update the smallest affected graph node in the same work branch.

If two current primary nodes disagree, do not choose one silently. Report:

`CONFLICT: <node-a> vs <node-b>: <what differs>`

Then verify the source and resolve the graph.

If required information cannot be established, report:

`UNKLAR: <what is missing>`

Do not guess through ambiguity that could change repository state or product behavior.

## Repository safety

- Never force-push unless the user explicitly authorizes that exact operation.
- Never move or merge into `main`, publish a release or delete branches without explicit user authorization.
- Work in an isolated branch for migrations, experiments and feature work.
- Keep commits focused and reversible.
- Never claim CI, release, APK or runtime success without fresh evidence.
- Do not revive old Wear, package, branch or release instructions merely because they exist in historical Markdown.

## Product preservation

Before modifying architecture or integrating upstream, load `contract.product-invariants` and any nodes it references.

Preserve downstream behavior semantically, especially navigation, app-lock/security, notifications, widgets, stable media-row geometry and media-details freshness. External or upstream metadata must not be interpreted as a stronger product guarantee than the source actually provides.

## Working method

For an implementation task:

1. Freshly inspect the target branch and relevant code.
2. State the intended change and protected invariants.
3. Make the smallest coherent implementation.
4. Run the relevant focused tests, then broader tests required by the affected contracts.
5. Inspect CI/release evidence when applicable.
6. Update graph nodes if repository truth, decisions, contracts, known issues or verification evidence changed.
7. Run `python tools/validate_ai_context_graph.py`.
8. Report exact branch, commit SHA, tests and unresolved risks.

When adding context, update the smallest existing node or create one new focused node. Do not copy the same fact into several documents.

Start now by reading `docs/ai-context/graph/index.json`.
