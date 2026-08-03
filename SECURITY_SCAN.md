# Packaging and Secret Scan

This is a conservative tracked-file scan performed before creating the transfer ZIP.

- Tracked files scanned: 905
- Sensitive filename hits: 0
- Concrete private-key/token/password marker hits: 3

## Content hits requiring review

- `app/src/main/java/com/axiel7/anihyou/ui/screens/main/MainViewModel.kt`
- `feature/widget/src/main/java/com/axiel7/anihyou/widget/AiringWidget.kt`
- `feature/worker/src/main/java/com/axiel7/anihyou/feature/worker/NotificationWorker.kt`

Workflow references such as `${{ secrets.KEYSTORE_PASSWORD }}` and environment-variable names are expected and do not contain secret values.

APK binaries are intentionally excluded from the repository ZIP.
