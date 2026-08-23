# Kiyori Upstream Sync 2026-08-23 — Downstream Handoff

## Zweck

Diese Datei beschreibt den großen Kiyori-Upstream-Sync aus PR #10 und ist gleichzeitig der verbindliche Übergabekontext für Forks von Kiyori.

## Referenzstände

- Kiyori vor diesem Sync: `main` / `c63a1322b6741ca0453b0e6dfba929a8931f9a52` (veröffentlichter Stand v1.6.0.1)
- AniHyou-Upstream-Snapshot: `axiel7/AniHyou-android` / `master` / `f35395594bff13581d4e239181ec4da988439d8e`
- Kiyori-Integrations-PR: #10, Branch `upstream-sync-2026-08-23`
- Ziel: phone-only Kiyori. Wear OS darf nicht wieder eingeführt werden.

## Was aus dem aktuellen AniHyou-Upstream übernommen wurde

Der Sync wurde nicht als pauschales Überschreiben durchgeführt. Network, Model, Domain, Resources, Shared UI und große Teile der Feature-Module wurden auf den aktuellen Snapshot gebracht und Kiyori-spezifische Funktionen danach gezielt wieder darübergelegt.

Wichtige übernommene Änderungen:

- aktualisierte GraphQL-/Network-Modelle und Repository-Anpassungen
- Media-List-Priority inklusive Priority-Anzeige, Bearbeitung und konfigurierbarer Farben
- Score-Step-Unterstützung und Persistenz
- aktuelle Progress-/Current-List-UI und sofortige Aktualisierung nach List-Edits
- Refresh-/Cache-Korrekturen für Media-Listen
- Activity-/Thread-/Nested-Comment-Korrekturen und Result-/Refresh-Flows
- aktualisierte Datums-/Zeitdarstellung (`nonFutureDateToLegibleText` usw.)
- neue paginierte vollständige Media-Characters-Ansicht
- aktuelle Shared-Composables, Preference-Komponenten und Color Picker
- aktuelles Settings-Redesign als Upstream-Basis
- aktuelle Profile-, Notifications-, Search-, Explore-, Staff-, Studio-, Review-, User-Media-List- und weitere Feature-Implementierungen

## Kiyori-Funktionen, die absichtlich Vorrang hatten

Diese Bereiche dürfen bei einem späteren Upstream-Sync nicht versehentlich durch generischen AniHyou-Code ersetzt werden:

1. **Konfigurierbare Hauptnavigation**
   - dynamische Haupttabs
   - Reihenfolge/Sichtbarkeit
   - Main-Tab-Shortcuts für Current-Listen, Charts und Seasons
   - korrekter Selected-State und Bottom-Bar-/Rail-Lebenszyklus
   - `MainNavigationResolver`, `MainNavigationSettingsView` und die zugehörigen Modelle/Persistenz

2. **Kalender**
   - Kiyori-Kalenderverhalten und Main-Tab-Unterstützung
   - Kiyori-spezifische List/Grid-/Filter-/Padding-/Navigation-Integration

3. **App Lock / Security**
   - Systemauthentifizierung (Biometrie oder Gerätesperre)
   - Timeout-Logik
   - Lifecycle-/Resume-Verhalten
   - Deep-Link-/Notification-Verhalten bei gesperrter App
   - App-Lock-Persistenz und deutsch/englische Texte

4. **Kiyori Character/Staff UI**
   - Rollen- und Sprachfilter
   - Team/Characters-Darstellung
   - Voice-Actor-Handling
   - neue Upstream-Seite „Alle Charaktere“ wurde integriert, ohne Kiyoris People-UI zu ersetzen

5. **Kiyori Branding und Distribution**
   - App-Name, Branding und IDs
   - phone-only
   - Signing-/Release-Logik
   - FOSS/GMS-Varianten
   - bestehender Release `v1.6.0.1` darf nicht mutiert oder neu erzeugt werden

## Navigation nach dem Sync

Kiyori verwendet nun eine gemeinsame typed `Route`-Schicht, die aktuelle Upstream-Routen und Kiyoris Top-Level-Main-Routen zusammenführt.

Neue/aktuelle Ziele umfassen unter anderem:

- `Route.MediaCharacters`
- `Route.PriorityColors`
- `Route.MainNavigationSettings`
- `Route.CalendarMain`
- `Route.MediaChartListMain`
- `Route.SeasonAnimeMain`
- `Route.CurrentFullListMain`

`LocalNavActionManager` und `LocalMarkdownUriHandler` werden über CompositionLocals bereitgestellt, damit aktuelle Upstream-Screens nicht wieder mit alten expliziten Navigation-Parametern geforkt werden müssen.

### Absichtliche Kompatibilitätsschicht

Die Season-Main-Tab-UI verwendet derzeit noch den kleinen Kiyori-Wrapper `Routes.SeasonAnime`, weil dieser `isMainDestination`, Back-Affordance und Main-Tab-Padding gemeinsam mit dem bestehenden Kiyori-Layout steuert. `SeasonAnimeView` und `SeasonAnimeViewModel` verwenden bewusst denselben Argumenttyp, damit keine Koin-Runtime-Mismatch entsteht.

Diese Schicht darf später entfernt werden, aber nur wenn folgende Punkte zusammen getestet werden:

- Season als normales Unterziel
- aktuelle und nächste Season als Haupttab
- Back-Button nur im Unterziel
- Bottom-Bar bleibt beim Haupttab sichtbar
- ViewModel-Koin-Argumente identisch
- Rotation/Process-Restore und Tab-Wechsel ohne Crash

## Settings nach dem Sync

Upstreams aktuelles Settings-Design bleibt die Basis. Kiyori hängt seine eigenen Einstellungen über einen kleinen, separat markierten Extension-Seam in den General-Bereich ein:

- Hauptnavigation
- App-Sperre

Upstream-Funktionen wie Score Steps, Priority Colors, Theme/Palette, List Styles, Logout-Bestätigung und Notification-Einstellungen bleiben erhalten.

## Regeln für einen Downstream-Fork

### Priorität bei Konflikten

**Funktionierende Downstream-Integration > Kiyori-spezifische Standardimplementierung > generischer AniHyou-Upstream.**

Ausnahme: Eine Downstream-Lösung darf nicht bevorzugt werden, wenn sie nachweislich Security, Datenintegrität, API-Kompatibilität, Migrationen oder Build-/Signing-Korrektheit verletzt.

### Niemals blind überschreiben

Vor einer Integration müssen insbesondere geprüft werden:

- Application ID / Namespace
- Branding, App-Name, Icons und Links
- Signing und Release-Pipelines
- eigene API-/Backend-/Provider-Integrationen
- eigene Navigation und Deep Links
- eigene Settings und DataStore-Keys
- Calendar-Anpassungen
- Worker/Notifications
- Build Flavors und Distribution
- Fork-spezifische Services, Repositories oder Feature Flags

## Empfohlener Downstream-Upgrade-Ablauf

1. Downstream-`main` und alle Release-/Integrationsbranches sichern.
2. Fork-spezifische Commits und Dateien seit dem letzten Kiyori-Abgleich inventarisieren.
3. Drei Stände vergleichen:
   - Downstream aktuell
   - Kiyori vor PR #10 (`c63a1322...`)
   - Kiyori nach PR #10 / aktuelles `main`
4. Konflikte kategorisieren:
   - Upstream-only
   - Kiyori-only
   - Downstream-only
   - gleiche Funktion, unterschiedliche Implementierung
5. Konfliktfreie Upstream-/Kiyori-Dateien übernehmen.
6. Bei gemeinsamen Integrationspunkten zuerst das gewünschte Downstream-Verhalten dokumentieren und danach semantisch mergen.
7. Persistenz-/DataStore-Keys niemals ohne Migration umbenennen oder löschen.
8. Navigation nicht durch Route-Namen allein migrieren; Top-Level-/Back-/Selected-State-Verhalten testen.
9. Security und Deep Links mit gesperrter sowie entsperrter App testen.
10. FOSS/GMS bzw. alle Downstream-Flavors separat bauen.
11. Erst nach CI + physischem Gerätetest mergen/releasen.

## Pflicht-Testbereiche für Downstream

- Cold Start und Upgrade über vorhandene Installation ohne Datenlöschung
- Login/Logout und bestehende Session
- dynamische Main Navigation
- Calendar
- App Lock
- Notifications + Deep Links
- Media Lists, Refresh und Edit
- Priority + Score Steps
- Current/Progress
- Search/Explore/Season/Charts
- MediaDetails + Kiyori People UI + Alle Charaktere
- Activity/Threads/Replies
- Profile/Favorites/Stats
- Settings und Persistenz nach Neustart
- Deutsch + primäre Downstream-Sprache(n)
- schneller Tab-Wechsel, Scrollen, Background/Resume, Process-Restore
- alle Build-Flavors

---

# Vollständiger Prompt für eine AI, die einen Downstream-Fork aktualisiert

Arbeite im Repository des Downstream-Forks von `madebycli/Kiyori`.

Deine Aufgabe ist, den Fork auf den Kiyori-Stand nach dem Upstream-Sync vom 23.08.2026 (Kiyori PR #10, Upstream-Snapshot `axiel7/AniHyou-android@f35395594bff13581d4e239181ec4da988439d8e`) zu aktualisieren, **ohne die eigenen Integrationen des Forks zu verlieren**.

Lies zuerst vollständig:

- diese Datei `docs/ai-context/UPSTREAM_SYNC_2026-08-23_DOWNSTREAM_HANDOFF.md`
- README/CHANGELOG/Architektur-/Recovery-/Context-Dateien des Downstream-Forks
- dessen Build-Konfiguration, Flavors, Application IDs, Signing- und Release-Workflows
- Navigation, Settings, Calendar, Security/App-Lock, Notifications/Deep-Links
- alle fork-spezifischen Provider/API-/Backend-/Service-Integrationen
- die Commits bzw. den Diff des Forks gegenüber dem Kiyori-Basisstand, von dem er ursprünglich abgezweigt wurde

Behandle folgende Prioritätsregel als verbindlich:

**Eine bereits funktionierende Downstream-Integration hat bei Konflikten grundsätzlich Vorrang vor Kiyoris Standardimplementierung; Kiyoris Integration hat Vorrang vor generischem AniHyou-Upstream.** Davon darfst du nur abweichen, wenn die bestehende Downstream-Lösung Security, Datenintegrität, API-Kompatibilität, Migrationen oder die Build-/Release-Korrektheit verletzt. Begründe solche Abweichungen ausdrücklich.

Wichtig:

- Nicht einfach Kiyori-`main` über den Fork kopieren oder einen großen Merge ungeprüft akzeptieren.
- Keine Downstream-Application-ID, Namespace-, Branding-, Signing-, Release-, Backend- oder Provider-Konfiguration überschreiben.
- Keine DataStore-/Preference-Keys löschen oder umbenennen, ohne Persistenz/Migration zu prüfen.
- Wear OS nicht einführen, sofern der Downstream nicht ausdrücklich selbst Wear OS pflegt.
- Den bestehenden Downstream-Release nicht verändern oder neu taggen.
- Kiyoris dynamische Hauptnavigation, Calendar-Integration, App Lock, Notification-Deep-Links und Kiyori People UI nur dort übernehmen, wo der Fork keine bewusst abweichende eigene Integration hat.
- Die neue typed `Route`-/CompositionLocal-Navigation semantisch mit Downstream-Routen zusammenführen; keine parallelen konkurrierenden Navigatoren erzeugen.
- Die aktuelle Season-Kompatibilitätsschicht (`Routes.SeasonAnime`) nur entfernen, wenn Main-Tab-/Back-/Padding-/Koin-Verhalten vollständig ersetzt und getestet wurde.

Arbeite in kleinen, nachvollziehbaren Commits auf einem neuen Integrationsbranch. Sichere regelmäßig remote. Keine Force-Pushes und keine direkten Änderungen an produktiven Release-Tags.

Führe nach jeder größeren Schicht mindestens einen Compile-/Unit-Test aus. Vor dem Merge müssen alle normalen CI-Gates sowie alle Downstream-Flavors erfolgreich sein.

Erstelle zuerst eine Konfliktmatrix mit mindestens den Kategorien `Downstream behalten`, `Kiyori übernehmen`, `Upstream übernehmen`, `semantisch mergen`, `Migration nötig`. Setze anschließend die Integration tatsächlich um; stoppe nicht nach der Analyse.

Besonders genau prüfen:

1. Network/Model/Domain-Konsistenz inklusive Priority, Score Steps, Progress und neuen GraphQL-Feldern.
2. Navigation und Main-Tab-Selected-State.
3. Calendar als Haupttab und als Unterziel.
4. App Lock inklusive Lifecycle und Deep Links.
5. Notifications/Worker.
6. Settings: Downstream-Einträge plus Kiyori App Lock/Hauptnavigation plus Upstream Score Steps/Priority Colors.
7. MediaDetails: Downstream-/Kiyori-People-UI nicht durch die einfachere Upstream-Darstellung ersetzen; neue vollständige Character-Seite integrieren.
8. Listen-Refresh und unmittelbare UI-Aktualisierung nach Edits.
9. Branding, IDs, URLs, Signing, Flavors und Release-Automation des Forks.
10. Physischer Gerätetest nach `PERFORMANCE_APK_TEST_PLAN_2026-08-23.md` bzw. einem äquivalenten Downstream-Testplan.

Am Ende liefere:

- finalen Integrations-Commit/PR
- vollständige Liste der übernommenen und bewusst nicht übernommenen Änderungen
- Konfliktentscheidungen mit Begründung
- CI-/Build-Ergebnisse
- noch erforderliche manuelle Gerätetests
- eine aktualisierte Downstream-Context-Datei für den nächsten Sync

Ein Merge ist erst zulässig, wenn der exakte zu mergende Commit grün gebaut wurde und keine bekannte fork-spezifische Integration verloren gegangen ist.
