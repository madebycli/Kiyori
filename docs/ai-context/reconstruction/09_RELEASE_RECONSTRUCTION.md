> **Reconstruction status:** implementation guidance adapted for Kiyori from the surviving project record. It is not a byte-identical copy of the deleted source.

# Release Reconstruction

Release work is last.

## Intended release

```text
name = 1.6.0.1
code = 113
tag = v1.6.0.1
title = Kiyori 1.6.0.1
asset = Kiyori-1.6.0.1-foss-universal.apk
```

## Do not rush version bump

The product rebuild matters more than version text. Keep development versioning unchanged until final FOSS debug is approved.

## Signing identity

Before generating a new permanent key, inspect saved APKs:

```bash
apksigner verify --print-certs APK_FILE
```

If a signed `app.kiyori` production APK was distributed, preserve its signing identity.

If only debug APKs and unsigned candidates exist, create one new permanent release key and back it up.

## Public documentation

English:

- README;
- changelog;
- GitHub release title/notes.

Application UI may remain localized.

## Final workflow

- main only;
- full CI first;
- signing secrets;
- build FOSS release;
- `apksigner verify`;
- upload universal APK;
- create tag;
- create GitHub Release;
- record APK/certificate SHA-256;
- remove temporary keystore.
