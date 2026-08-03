# Architecture and Scope Boundaries — Kiyori

## Branches

- `develop`: exact mirror of selected upstream `axiel7/AniHyou-android:develop`.
- `recovery/phase0-backup`: immutable imported backup.
- `main`: Kiyori product/default branch.
- product work: feature branches and reviewed pull requests.

Never add Kiyori-only commits directly to `develop`.

## Upstream policy

Preserve upstream behavior by default for API, OAuth, GraphQL, token storage, MAL metadata, translations, module boundaries and Kotlin namespace. Reapply Kiyori identity and approved features as narrow patches.

Do not resolve conflicts by replacing a newer upstream whole file with an older backup file.

## Authentication

Historically preserved contract to verify against current upstream:

- AniList client ID `8527`;
- authorization endpoint `https://anilist.co/api/v2/oauth/authorize`;
- callback `anihyou://auth-response`;
- Wear callback `anihyou://wear-auth`;
- GraphQL endpoint `https://graphql.anilist.co`.

Do not invent a new callback while using the old client registration.

## Identity target

- release application ID: `app.kiyori`;
- debug application ID: `app.kiyori.debug`;
- release name: `Kiyori`;
- debug name: `Kiyori Debug`;
- internal namespace remains `com.axiel7.anihyou`.

## Reuse rule

Wrap existing upstream screens for top-level hosting. Do not duplicate current-list, chart, Season or media item data layers.

## Security

Never commit:

- `.jks`, `.keystore`, `.p12`;
- `.secrets/`;
- `local.properties`;
- signing passwords;
- Base64 keystore data;
- access tokens;
- private device logs.

Use repository secrets only for signing. Exclude the credential-bearing DataStore from Android cloud backup and device transfer.
