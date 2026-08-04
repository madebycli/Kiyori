> **Reconstruction status:** implementation guidance adapted for Kiyori from the surviving project record. It is not a byte-identical copy of the deleted source.

# APK Forensics Guide

## Is the latest APK useful?

Yes, as supplementary evidence.

It can confirm:

- application ID;
- version name/code;
- resources and strings;
- manifest routes and activities;
- launcher/splash resources;
- packaged class names;
- route constants when not obfuscated;
- preference keys;
- Compose labels;
- Phase-4 assets;
- certificate fingerprint;
- differences between historical APKs.

It cannot reliably recover:

- original Kotlin formatting;
- comments;
- exact variable names after optimization;
- commit history;
- clean Compose structure;
- tests;
- code removed by R8;
- signing private key.

## Best evidence set

When available, provide:

1. latest accepted FOSS debug APK;
2. Phase-2 APK;
3. corrected Phase-3 APK;
4. Phase-4 FOSS debug APK;
5. signed or unsigned release candidate.

Record filenames and SHA-256.

## NixOS tool shell

```bash
nix-shell -p jadx apktool android-tools apksigner unzip zip file ripgrep
```

Package availability can vary by Nix channel.

## Metadata

```bash
apkanalyzer manifest application-id app.apk
apkanalyzer manifest version-name app.apk
apkanalyzer manifest version-code app.apk
apksigner verify --print-certs app.apk
```

## Resources

```bash
apktool d -f app.apk -o decoded
rg -n "Haupttab hinzufügen|Kalender|Nächste Saison|app.kiyori" decoded
```

## Decompiled code

```bash
jadx -d jadx-out app.apk
rg -n "CalendarMain|SeasonMain|MainNavigationShortcut|calendar_list_view" jadx-out
```

## Compare APKs

For each APK record:

- SHA-256;
- version;
- package;
- certificate;
- manifest diff;
- resources diff;
- class-name diff;
- asset diff.

## Use of findings

Use APK evidence to refine and confirm behavior. Do not paste decompiled code blindly. Reimplement cleanly against current upstream and the GPL source base.

## Highest-value next input

The latest accepted FOSS debug APK is the most useful single additional artifact.
