# Kiyori Performance-Test-APK — manueller Gerätetest

Dieser Testplan gilt für den Upstream-Sync aus PR #10.

## Wichtig: Signatur und Installation

Der CI-Workflow erzeugt zwei Test-APKs mit unterschiedlichen Installationsregeln:

- **FOSS Debug APK**: `applicationId = app.kiyori.debug`. Diese APK kann parallel zur stabilen Kiyori-Installation installiert werden und ist der bevorzugte Weg für den funktionalen Gerätetest ohne Datenverlust.
- **FOSS Performance APK**: `applicationId = app.kiyori`, aber mit einem **temporären CI-Zertifikat** signiert. Sie kann deshalb **nicht** über eine permanent signierte Kiyori-v1.6.0.1-Installation installiert werden. Nutze sie nur auf einem Testgerät/Testprofil ohne bestehende stabile Kiyori-Installation oder nach bewusstem Entfernen der vorhandenen Installation.

Ein echter In-place-Upgrade-/Migrationstest über eine vorhandene stabile v1.6.0.1-Installation ist erst mit einem Kandidaten möglich, der mit demselben permanenten Release-Zertifikat signiert wurde. Niemals Signaturprüfung umgehen oder Release-Schlüssel exportieren, um diesen Test zu erzwingen.

## Vor dem Start notieren

- Gerät / Android-Version
- verwendete Test-APK (Debug oder Performance)
- ob eine stabile Kiyori-Installation parallel vorhanden ist
- deine normale Main-Tab-Konfiguration zum Vergleich
- auffällige Calendar-/Notification-Einstellungen, die du im Test nachbauen möchtest

Bei einem Fehler möglichst notieren:

- exakte Aktion direkt vor dem Fehler
- welcher Tab/Screen aktiv war
- ob die App aus dem Hintergrund kam
- Screenshot oder Screenrecording
- App-Logs unmittelbar danach

## 1. Installation / Start

### Sicherer Funktionstest

1. `Kiyori-1.6.0.1-foss-debug.apk` installieren.
2. Prüfen, dass sie als Debug-App parallel zur stabilen Kiyori-App vorhanden ist.
3. Einloggen und die für den Test benötigten Einstellungen nachbilden.
4. App vollständig schließen und erneut cold-starten.
5. Android-Systemsprache einmal Deutsch und, wenn möglich, Englisch testen.

### Performance-Build

1. `Kiyori-1.6.0.1-foss-performance-temp-signed.apk` nur auf einem Gerät/Testprofil ohne bestehende `app.kiyori`-Installation installieren.
2. Start, Login und denselben Smoke-/Stress-Test wie mit Debug durchführen.
3. Nicht versuchen, Androids Signaturprüfung zu umgehen.

## 2. Hauptnavigation — höchste Priorität

Teste verschiedene Konfigurationen, nicht nur deine normale.

- Home, Anime, Manga, Profil, Discover und Calendar ein-/ausblenden soweit erlaubt.
- Reihenfolge mehrfach ändern.
- Zusätzliche Shortcuts hinzufügen: Current-Listen, Top/Popular/Upcoming-Charts, aktuelle/nächste Season.
- App danach neu starten und Persistenz prüfen.
- Jeden Haupttab mehrfach schnell hintereinander wechseln.
- Prüfen, dass der optisch ausgewählte Tab **immer** zum sichtbaren Inhalt gehört.
- Calendar, Profil, Top 100, Season und Current als Haupttab testen.
- Die Bottom-Bar darf bei einem Haupttab nicht verschwinden.
- Von jedem Haupttab in ein Detail navigieren und mit Zurück zum selben Haupttab zurückkehren.
- Nach mehreren Back-Vorgängen erneut zwischen Tabs wechseln.

Besonders melden: falscher Selected-State, verschwundene Bottom-Bar, Tab reagiert erst nach mehreren Klicks, Inhalt/Markierung nicht synchron, falsches Back-Ziel oder Crash bei Season-/Chart-/Current-Shortcut.

## 3. Calendar

- Vertikal durch mehrere Tage scrollen.
- Datumswechsel und Rückkehr zu heute testen.
- List/Grid sofern verfügbar wechseln.
- „Auf meiner Liste“-Filter an/aus.
- Einen Anime öffnen und zurück zum Calendar.
- Calendar als normal geöffnetes Unterziel und als Haupttab vergleichen.
- Nach Background/Resume Scrollposition und aktiven Tag prüfen.

## 4. App Lock

Nur auf einem Gerät mit eingerichteter Biometrie oder sicherer Gerätesperre vollständig testbar.

- App Lock aktivieren; Authentifizierung muss verlangt werden.
- App Lock wieder deaktivieren; ebenfalls Authentifizierung prüfen.
- Alle Timeout-Werte kurz durchschalten und Persistenz nach Neustart prüfen.
- Bei aktivem Lock App in Hintergrund schicken und nach Ablauf zurückkehren.
- Während gesperrter App einen unterstützten Deep Link / eine Notification öffnen.
- Prüfen, dass zuerst entsperrt wird und danach das eigentliche Ziel korrekt geöffnet wird.
- Abbrechen/Fehlschlag des Auth-Dialogs testen.

## 5. Media Lists / Edit / Priority

- Anime-Liste öffnen und manuell aktualisieren; keine doppelten Einträge.
- Status, Score, Score Steps, Progress und Priority ändern.
- Eintrag entfernen/wieder hinzufügen, sofern für den Testaccount okay.
- Direkt danach Home/Current und die betroffene Liste prüfen: Änderungen sollen ohne Neustart sichtbar sein.

## 6. Current / Progress

- Airing, Behind, Watching/Anime, Reading/Manga und Next Season öffnen, soweit befüllt.
- +1-Progress und normalen Edit testen.
- Current-Liste als normalen Screen und als Haupttab-Shortcut testen.
- Änderung, Sortierung und sichtbarer Eintrag müssen sofort konsistent sein.

## 7. MediaDetails / Characters / Staff

- Mehrere Anime und Manga öffnen.
- Relations, Stats, Reviews/Threads und Info-Tabs öffnen.
- Kiyori-People-Bereich mit Rollenfilter, Sprachfilter und Voice-Actor-Auswahl testen.
- „Alle Charaktere“ öffnen, lange Liste scrollen und Character/Staff-Details öffnen.
- Mehrfach zurück navigieren.

Wichtig: Kiyoris People-UI darf nicht durch eine einfachere alte/upstream Grid-Ansicht ersetzt worden sein.

## 8. Settings

Im neuen Settings-Design müssen gleichzeitig vorhanden sein:

- Hauptnavigation
- App-Sperre
- Theme / Black Theme / Farben / Palette
- Score Format + Score Steps
- List Styles
- Show Low Priority
- Priority Colors
- Notifications
- Logout mit Bestätigungsdialog

Hauptnavigation öffnen/zurück, App Lock konfigurieren, Priority Colors ändern, Score Step/Score Format testen und Theme/Palette nach Neustart prüfen.

## 9. Activity / Threads

- Activity Feed und Filter testen.
- Activity-Details öffnen.
- Thread mit Parent/Child Comments laden.
- Reply auf Child Comment und normalen Comment testen.
- Nach Rückkehr auf Aktualisierung und Duplikate achten.

## 10. Search / Discover / Season / Charts

- Search mit mehreren Begriffen sowie Genre/Tag-Suche.
- Discover-Sektionen öffnen.
- aktuelle/nächste Season mit Filter/List/Grid testen.
- Season als Haupttab testen.
- Top 100 / Popular / Upcoming / Airing Chart öffnen und Chart als Haupttab testen.

## 11. Profile / Favorites / Stats

- eigenes und fremdes Profil öffnen.
- Favorites öffnen; Reorder nur wenn für den Account okay.
- Stats-Unterseiten scrollen.
- Social/Following/Follower öffnen.

## 12. Notifications / Deep Links

- Notifications-Screen öffnen.
- Notification-Einstellungen/Intervall testen, sofern aktiv.
- vorhandene Notification öffnen und korrektes Ziel prüfen.
- AniList-Links für Anime/Manga/User/Character/Staff/Thread/Activity testen.
- Dasselbe einmal mit aktiviertem App Lock.

## 13. Stress / Performance

- 30–60 Sekunden schnell zwischen Haupttabs wechseln.
- Lange Listen schnell flingen.
- 10–20 MediaDetails nacheinander öffnen/zurück.
- Während Laden mehrfach Tab wechseln.
- App mehrfach Background -> Foreground.
- Rotation optional testen.
- Android „Nicht im Speicher behalten“ nur optional als harter Restore-Test.
- Auf Ruckler, ANR („Kiyori reagiert nicht“), leere Screens, verlorene Bottom-Bar, falschen Selected-State und ungewöhnlich lange Ladevorgänge achten.

## 14. FOSS / GMS

Die bereitgestellte Performance-Test-Pipeline validiert FOSS. Android CI baut und testet zusätzlich FOSS und GMS. Wenn später ein GMS-Geräteartefakt bereitgestellt wird, mindestens Installation/Start, Login, Home, Main Navigation, Calendar, MediaDetails und Settings smoke-testen.

## Mindestfreigabe für Merge/Release

Ein manueller Test ist erfolgreich, wenn:

- kein reproduzierbarer Crash/ANR auftritt
- Main-Tab-Inhalt und Selected-State immer synchron sind
- Bottom-Bar bei allen Main-Destinationen stabil bleibt
- Calendar stabil funktioniert
- App Lock nicht umgangen wird und Deep Links danach korrekt weiterleiten
- Media-List-Edits/Progress ohne Duplikate und mit sofortiger UI-Aktualisierung funktionieren
- Kiyori Character/Staff UI erhalten ist
- neue Upstream-Funktionen (Priority, Score Steps, Alle Charaktere) funktionieren
- Settings sowohl Kiyori- als auch Upstream-Einträge enthält

Ein separater In-place-Upgrade-Test über die stabile App wird mit einem permanent signierten Kandidaten durchgeführt; er ist **nicht** Aufgabe der temporär signierten Performance-APK.
