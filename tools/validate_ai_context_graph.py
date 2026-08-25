#!/usr/bin/env python3
"""Validate the Kiyori AI context graph using only the Python standard library."""

from __future__ import annotations

import json
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
GRAPH = ROOT / "docs" / "ai-context" / "graph"
INDEX = GRAPH / "index.json"
NODE_ID_RE = re.compile(r"^[a-z0-9][a-z0-9._-]*$")

REQUIRED_NODE_KEYS = {
    "id", "type", "status", "authority", "updated_at",
    "summary", "facts", "constraints", "relations", "evidence",
}
ALLOWED_STATUS = {"current", "planned", "historical", "deprecated"}
ALLOWED_AUTHORITY = {"primary", "secondary", "evidence"}


def fail(message: str) -> None:
    print(f"ERROR: {message}", file=sys.stderr)
    raise SystemExit(1)


def load_json(path: Path):
    try:
        return json.loads(path.read_text(encoding="utf-8"))
    except FileNotFoundError:
        fail(f"missing file: {path.relative_to(ROOT)}")
    except json.JSONDecodeError as exc:
        fail(f"invalid JSON in {path.relative_to(ROOT)}: {exc}")


def main() -> None:
    index = load_json(INDEX)
    if index.get("format") != "kiyori-context-graph/v1":
        fail("unsupported graph format")

    entries = index.get("nodes")
    if not isinstance(entries, list) or not entries:
        fail("index.nodes must be a non-empty array")

    ids = set()
    paths = set()
    nodes = {}

    for entry in entries:
        node_id = entry.get("id")
        rel_path = entry.get("path")
        if not isinstance(node_id, str) or not NODE_ID_RE.fullmatch(node_id):
            fail(f"invalid node id in index: {node_id!r}")
        if node_id in ids:
            fail(f"duplicate node id in index: {node_id}")
        if not isinstance(rel_path, str) or not rel_path.startswith("nodes/"):
            fail(f"invalid path for {node_id}: {rel_path!r}")
        if rel_path in paths:
            fail(f"duplicate node path in index: {rel_path}")
        ids.add(node_id)
        paths.add(rel_path)

        node = load_json(GRAPH / rel_path)
        nodes[node_id] = node
        missing = REQUIRED_NODE_KEYS - node.keys()
        if missing:
            fail(f"{node_id} missing keys: {sorted(missing)}")
        if node["id"] != node_id:
            fail(f"index/node id mismatch: {node_id} != {node['id']}")
        if node["status"] not in ALLOWED_STATUS:
            fail(f"{node_id} has invalid status: {node['status']}")
        if node["authority"] not in ALLOWED_AUTHORITY:
            fail(f"{node_id} has invalid authority: {node['authority']}")
        if entry.get("type") != node["type"] or entry.get("status") != node["status"] or entry.get("authority") != node["authority"]:
            fail(f"index metadata is stale for {node_id}")
        if not isinstance(node["relations"], dict):
            fail(f"{node_id}.relations must be an object")

    entry_nodes = index.get("entry_nodes", [])
    unknown_entries = sorted(set(entry_nodes) - ids)
    if unknown_entries:
        fail(f"unknown entry nodes: {unknown_entries}")

    for node_id, node in nodes.items():
        for relation, targets in node["relations"].items():
            if not isinstance(targets, list):
                fail(f"{node_id}.{relation} must be an array")
            for target in targets:
                if not isinstance(target, str):
                    fail(f"{node_id}.{relation} contains non-string target")
                if target not in ids:
                    fail(f"{node_id}.{relation} points to missing node: {target}")

    disk_nodes = {
        str(path.relative_to(GRAPH)).replace("\\", "/")
        for path in (GRAPH / "nodes").glob("*.json")
    }
    unindexed = sorted(disk_nodes - paths)
    missing_on_disk = sorted(paths - disk_nodes)
    if unindexed:
        fail(f"unindexed node files: {unindexed}")
    if missing_on_disk:
        fail(f"indexed node files missing on disk: {missing_on_disk}")

    relation_count = sum(len(targets) for node in nodes.values() for targets in node["relations"].values())
    print(f"OK: {len(nodes)} nodes, {relation_count} relations")


if __name__ == "__main__":
    main()
