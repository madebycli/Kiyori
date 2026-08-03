> **Reconstructed blueprint:** adapt to the current upstream APIs and compile; do not claim this is the exact deleted source.

# CI, Security, Wear, and Release Blueprint

## Verification matrix

Workflow builds/tests:

- FOSS debug;
- GMS debug;
- FOSS tests;
- GMS tests;
- model tests;
- domain tests;
- Calendar tests;
- Wear debug;
- FOSS lint;
- GMS lint;
- minified FOSS release candidate.

Upload separate artifact groups.

## Backup

Confirm actual DataStore path, then exclude credential store from:

- `backup_rules.xml`;
- `data_extraction_rules.xml`.

Add a CI contract check.

## Notifications

Resolve installed package at runtime. Use Kiyori icon and installed variant label.

## Phone shortcuts

Keep shortcut XML in phone app resources. Use main/debug resource overlays for target package when necessary.

## Wear

- compileSdk 37 when required;
- keep targetSdk 36 unless intentionally changed;
- Kiyori-specific phone-required text in Wear module;
- do not edit every shared translation.

## Splash

Theme points to dedicated transparent `kiyori_splash_mark`, not the rimmed launcher bitmap.

## Release

On main/manual gate:

- verify four secrets;
- decode temporary keystore;
- build signed FOSS release;
- verify with `apksigner`;
- stage universal APK;
- create English notes from changelog;
- upload artifact;
- create tag/release;
- remove temporary keystore.
