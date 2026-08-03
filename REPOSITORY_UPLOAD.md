# Repository Upload Procedure

This repository is prepared for `madebycli/Kiyori`.

## Intended branches

- `main`: Phase-0 backup working tree plus Kiyori recovery context; intended temporary default/product branch.
- `recovery/phase0-backup`: immutable backup at `476ad447217ecae2b7c7ae710f7981ca55d9a003`.
- `develop`: existing remote upstream mirror; fetch from GitHub, do not replace with the offline backup.
- `feature/kiyori-integrated-rebuild`: create from current `origin/develop` after upload.

## Upload

```bash
git remote set-url origin https://github.com/madebycli/Kiyori.git
git fetch origin
git push origin main:main
git push origin recovery/phase0-backup:recovery/phase0-backup
gh repo edit madebycli/Kiyori --default-branch main
```

No force push is required or authorized.
