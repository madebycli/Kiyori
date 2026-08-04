# Packaging and Secret Scan

A conservative tracked-file scan was performed before creating the transfer ZIP.

- Sensitive filename hits: **0**
- Tracked `.jks`, `.keystore`, `.p12`, `local.properties`, `.env`, `.secrets/`, private-key files or APK binaries: **0**
- PEM/OpenSSH private-key blocks: **0**
- Concrete signing-password assignments: **0**
- Concrete embedded GitHub/API access-token values: **0**

Three initial content matches were manually reviewed:

- `MainViewModel.kt`
- `AiringWidget.kt`
- `NotificationWorker.kt`

They only reference the runtime variable/repository property named `accessToken`; no token value is present in source.

Workflow expressions such as `${{ secrets.KEYSTORE_PASSWORD }}` and environment-variable names are expected references and do not contain secret values.

APK binaries are intentionally excluded from this repository ZIP. Their filenames and SHA-256 values are recorded in `docs/ai-context/evidence/APK_ARTIFACTS.md`.
