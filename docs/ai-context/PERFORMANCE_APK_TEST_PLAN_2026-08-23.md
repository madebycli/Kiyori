# Kiyori Performance-Test-APK — manueller Gerätetest

Dieser Testplan gilt für den Upstream-Sync aus PR #10. Der Test soll möglichst als **Update über eine vorhandene Kiyori-v1.6.0.1-Installation ohne App-Daten zu löschen** durchgeführt werden. Danach kann optional noch ein Clean-Install-Test folgen.

## Vor dem Start notieren

- Gerät / Android-Version
- installierte Variante (FOSS oder GMS)
- ob du vor dem Update eingeloggt warst
- deine aktuelle Main-Tab-Konfiguration
- ob App Lock vorher aktiviert war
- auffällige vorhandene Calendar-/Notification-Einstellungen

Bei einem Fehler möglichst notieren:

- exakte Aktion direkt vor dem Fehler
- welcher Tab/Screen aktiv war
- ob die App aus dem Hintergrund kam
- Screenshot oder Screenrecording
- App-Logs unmittelbar danach

## 1. Upgrade / Start

1. Performance-APK über die vorhandene Installation installieren.
2. App normal öffnen.
3. Prüfen, dass vorhandene Session und Einstellungen erhalten sind.
4. App vollständig schließen und erneut cold-starten.
5. Android-Systemsprache einmal Deutsch und, wenn möglich, Englisch testen.

Fehler wären insbesondere Crash, verlorener Login, zurückgesetzte Main-Tabs oder verlorene App-Lock-/Calendar-Einstellungen.

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

Besonders melden:

- falscher Selected-State
- Bottom-Bar verschwindet
- Tab reagiert erst nach mehreren Klicks
- Inhalt wechselt, Markierung aber nicht
- Back führt auf falschen Haupttab
- Crash beim Season-/Chart-/Current-Shortcut

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

- Anime-Liste öffnen und manuell aktualisieren.
- Prüfen, dass keine doppelten Einträge entstehen.
- Status eines Eintrags ändern.
- Score ändern; bei unterstützten Score-Formaten verschiedene Score Steps testen.
- Progress +1/-1 bzw. normalen Progress-Edit testen.
- Priority ändern (Low/Medium/High bzw. vorhandene Stufen).
- Eintrag aus der Liste entfernen und wieder hinzufügen, falls für deinen Testaccount okay.
- Direkt danach Home/Current und die betroffene Liste kontrollieren: Änderungen sollen ohne unnötigen Neustart sichtbar sein.

## 6. Current / Progress

- Airing, Behind, Watching/Anime, Reading/Manga und Next Season öffnen, soweit befüllt.
- +1-Progress mehrfach testen.
- Langdruck/Edit öffnen und Wert ändern.
- Current-Liste als normalen Screen und als Haupttab-Shortcut testen.
- Prüfen, dass Änderung, Sortierung und sichtbarer Eintrag sofort konsistent sind.

## 7. MediaDetails / Characters / Staff

- Mehrere Anime und Manga öffnen.
- Relations, Stats, Reviews/Threads und Info-Tabs öffnen.
- Kiyori-People-Bereich testen:
  - Characters / Team
  - Rollenfilter
  - Sprachfilter
  - Voice-Actor-Auswahl
- „Alle Charaktere“ öffnen.
- In der vollständigen Character-Liste scrollen, Filter/Sortierung falls vorhanden nutzen und Character/Staff-Details öffnen.
- Mehrfach zurück navigieren.

Wichtig: Kiyoris People-UI darf nicht durch die einfachere alte/upstream Grid-Ansicht ersetzt worden sein.

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

Teste:

- Hauptnavigation aus Settings öffnen und zurück.
- App Lock aus Settings konfigurieren.
- Priority Colors ändern und anschließend Priority-Indikatoren kontrollieren.
- Score Step ändern; danach Score Edit öffnen.
- Score Format wechseln und prüfen, ob Score-Step-Reset sinnvoll erfolgt.
- Theme/Palette wechseln und App neu starten.

## 9. Activity / Threads

- Activity Feed öffnen und Filter testen.
- Activity-Details öffnen.
- Reply erstellen/bearbeiten, soweit du dafür einen Testpost verwenden möchtest.
- Thread öffnen, Parent/Child Comments laden.
- Reply auf Child Comment und normalen Comment testen.
- Nach Rückkehr prüfen, ob Inhalt aktualisiert wurde und keine Duplikate entstehen.

## 10. Search / Discover / Season / Charts

- Search mit mehreren Begriffen.
- Genre/Tag-Suche.
- Discover öffnen und mehrere Sektionen nutzen.
- aktuelle und nächste Season öffnen, Filter/List/Grid testen.
- Season zusätzlich als Haupttab testen.
- Top 100 / Popular / Upcoming / Airing Chart öffnen.
- Chart zusätzlich als Haupttab testen.

## 11. Profile / Favorites / Stats

- eigenes Profil und ein fremdes Profil öffnen.
- Favorites öffnen und Details aufrufen.
- Reorder Favorites testen, wenn du die Reihenfolge ohne Risiko ändern kannst.
- Stats-Unterseiten öffnen und scrollen.
- Social/Following/Follower öffnen.

## 12. Notifications / Deep Links

- Notifications-Screen öffnen.
- Falls Push/Worker aktiv ist: Notification-Einstellung an/aus und Intervall ändern.
- Vorhandene Notification öffnen und korrektes Ziel prüfen.
- AniList-Link für Anime/Manga/User/Character/Staff/Thread/Activity testen, soweit einfach möglich.
- Dasselbe einmal mit aktiviertem App Lock.

## 13. Stress / Performance

Diese Runde soll gezielt Dinge provozieren, die bei normalem Testen oft nicht auffallen.

- 30–60 Sekunden sehr schnell zwischen Haupttabs wechseln.
- Lange Listen schnell hoch/runter flingen.
- 10–20 MediaDetails nacheinander öffnen/zurück.
- Während Laden mehrfach Tab wechseln.
- App mehrfach Background -> Foreground.
- Display drehen, falls du Rotation nutzt.
- Android „Nicht im Speicher behalten“ nur optional als harter Restore-Test verwenden.
- Auf Ruckler, ANR („Kiyori reagiert nicht“), leere Screens, verlorene Bottom-Bar, falschen Selected-State und ungewöhnlich lange Ladevorgänge achten.

## 14. FOSS / GMS

Wenn möglich beide Varianten wenigstens smoke-testen:

- Installation / Start
- Login
- Home
- Main Navigation
- Calendar
- MediaDetails
- Settings

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
- vorhandene Benutzerdaten/Settings beim Upgrade erhalten bleiben
