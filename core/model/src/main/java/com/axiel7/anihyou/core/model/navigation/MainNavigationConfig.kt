package com.axiel7.anihyou.core.model.navigation

import com.axiel7.anihyou.core.model.CurrentListType
import com.axiel7.anihyou.core.model.media.ChartType

/**
 * The persisted, phone-only main navigation contract. It deliberately stores
 * stable technical identifiers, never Compose routes or date-dependent values.
 */
enum class MainNavigationDestination(
    val stableId: String,
    val defaultVisible: Boolean,
) {
    HOME("home", true),
    ANIME("anime", true),
    MANGA("manga", true),
    EXPLORE("explore", true),
    CALENDAR("calendar", true),
    PROFILE("profile", false),
    ;

    companion object {
        fun fromStableId(value: String) = entries.firstOrNull { it.stableId == value.lowercase() }
    }
}

enum class SeasonShortcutMode { CURRENT, NEXT }

sealed interface MainNavigationShortcut {
    val stableId: String

    data class Season(val mode: SeasonShortcutMode) : MainNavigationShortcut {
        override val stableId = "shortcut_season_${mode.name.lowercase()}"
    }

    data class CurrentList(val type: CurrentListType) : MainNavigationShortcut {
        override val stableId = "shortcut_current_list_${type.name.lowercase()}"
    }

    data class Chart(val type: ChartType) : MainNavigationShortcut {
        override val stableId = "shortcut_chart_${type.name.lowercase()}"
    }
}

data class MainNavigationItem(
    val stableId: String,
    val visible: Boolean,
    val destination: MainNavigationDestination? = null,
    val shortcut: MainNavigationShortcut? = null,
) {
    init {
        require((destination == null) != (shortcut == null))
    }
}

const val MAIN_NAVIGATION_CONFIG_VERSION = 4
const val MIN_VISIBLE_MAIN_DESTINATIONS = 2
const val MAX_VISIBLE_MAIN_DESTINATIONS = 5

data class MainNavigationConfig(
    val version: Int = MAIN_NAVIGATION_CONFIG_VERSION,
    val items: List<MainNavigationItem>,
) {
    val visibleItems get() = items.filter(MainNavigationItem::visible)

    fun normalized(): MainNavigationConfig {
        val kept = items
            .filter { it.destination != null || it.shortcut != null }
            .distinctBy(MainNavigationItem::stableId)
            .toMutableList()

        MainNavigationDestination.entries.forEach { destination ->
            if (kept.none { it.destination == destination }) {
                kept += staticItem(destination, destination.defaultVisible)
            }
        }
        kept.replaceAll { item ->
            if (item.destination == MainNavigationDestination.HOME) item.copy(visible = true) else item
        }

        val firstSeason = kept.indexOfFirst { it.shortcut is MainNavigationShortcut.Season }
        kept.removeAll { item ->
            item.shortcut is MainNavigationShortcut.Season && kept.indexOf(item) != firstSeason
        }

        while (kept.count(MainNavigationItem::visible) > MAX_VISIBLE_MAIN_DESTINATIONS) {
            val item = kept.lastOrNull {
                it.visible && it.destination != MainNavigationDestination.HOME
            } ?: break
            kept[kept.indexOf(item)] = item.copy(visible = false)
        }
        MainNavigationDestination.entries
            .filter { it != MainNavigationDestination.HOME }
            .forEach { destination ->
                if (kept.count(MainNavigationItem::visible) < MIN_VISIBLE_MAIN_DESTINATIONS) {
                    val index = kept.indexOfFirst { it.destination == destination }
                    if (index >= 0) kept[index] = kept[index].copy(visible = true)
                }
            }
        return copy(version = MAIN_NAVIGATION_CONFIG_VERSION, items = kept)
    }

    fun addShortcut(shortcut: MainNavigationShortcut): MainNavigationConfig {
        if (items.any { it.shortcut == shortcut } ||
            (shortcut is MainNavigationShortcut.Season && items.any { it.shortcut is MainNavigationShortcut.Season })
        ) return normalized()
        return copy(
            items = items + MainNavigationItem(
                stableId = shortcut.stableId,
                visible = visibleItems.size < MAX_VISIBLE_MAIN_DESTINATIONS,
                shortcut = shortcut,
            )
        ).normalized()
    }

    fun withVisibility(stableId: String, visible: Boolean): MainNavigationConfig = copy(
        items = items.map { item ->
            if (item.stableId == stableId && item.destination != MainNavigationDestination.HOME) {
                item.copy(visible = visible)
            } else item
        }
    ).normalized()

    fun move(stableId: String, toIndex: Int): MainNavigationConfig {
        val fromIndex = items.indexOfFirst { it.stableId == stableId }
        if (fromIndex < 0 || items[fromIndex].destination == MainNavigationDestination.HOME) {
            return normalized()
        }
        val reordered = items.toMutableList()
        val item = reordered.removeAt(fromIndex)
        reordered.add(toIndex.coerceIn(1, reordered.size), item)
        return copy(items = reordered).normalized()
    }

    fun removeShortcut(stableId: String): MainNavigationConfig = copy(
        items = items.filterNot { it.stableId == stableId && it.shortcut != null }
    ).normalized()

    fun reset(): MainNavigationConfig = defaultMainNavigationConfig()
}

fun defaultMainNavigationConfig() = MainNavigationConfig(
    items = MainNavigationDestination.entries.map { staticItem(it, it.defaultVisible) }
).normalized()

private fun staticItem(destination: MainNavigationDestination, visible: Boolean) = MainNavigationItem(
    stableId = destination.stableId,
    visible = visible,
    destination = destination,
)

/** A compact, forward-compatible codec with explicit migration of the old static-only values. */
object MainNavigationConfigCodec {
    private const val LEGACY_V3_DEFAULT =
        "home:1,anime:1,manga:1,profile:0,explore:1,calendar:0"

    fun encode(config: MainNavigationConfig): String = buildString {
        append("v4;")
        append(config.normalized().items.joinToString(",") { "${it.stableId}:${if (it.visible) 1 else 0}" })
    }

    fun decode(raw: String?): MainNavigationConfig = when {
        raw.isNullOrBlank() -> defaultMainNavigationConfig()
        raw.startsWith("v4;") -> decodeEntries(raw.removePrefix("v4;"))
        raw.startsWith("v3;") -> decodeVersion3(raw.removePrefix("v3;"))
        raw.startsWith("v2;") -> decodeEntries(raw.removePrefix("v2;"))
        else -> decodeLegacy(raw)
    }.normalized()

    private fun decodeVersion3(raw: String): MainNavigationConfig =
        if (raw == LEGACY_V3_DEFAULT) defaultMainNavigationConfig() else decodeEntries(raw)

    private fun decodeLegacy(raw: String): MainNavigationConfig {
        val visible = raw.split(',', ';', '|')
            .mapNotNull { MainNavigationDestination.fromStableId(it.trim()) }
            .toSet()
        return MainNavigationConfig(items = MainNavigationDestination.entries.map { destination ->
            staticItem(destination, destination == MainNavigationDestination.HOME || destination in visible)
        })
    }

    private fun decodeEntries(raw: String): MainNavigationConfig = MainNavigationConfig(
        items = raw.split(',').mapNotNull { encoded ->
            val parts = encoded.trim().split(':', limit = 2)
            if (parts.size != 2 || parts[1] !in setOf("0", "1")) return@mapNotNull null
            itemFromStableId(parts[0], parts[1] == "1")
        }
    )

    private fun itemFromStableId(stableId: String, visible: Boolean): MainNavigationItem? {
        MainNavigationDestination.fromStableId(stableId)?.let { return staticItem(it, visible) }
        val shortcut = when {
            stableId.startsWith("shortcut_season_") -> when (stableId.removePrefix("shortcut_season_")) {
                "current" -> MainNavigationShortcut.Season(SeasonShortcutMode.CURRENT)
                "next" -> MainNavigationShortcut.Season(SeasonShortcutMode.NEXT)
                else -> null
            }
            stableId.startsWith("shortcut_current_list_") ->
                CurrentListType.entries.firstOrNull {
                    it.name.equals(stableId.removePrefix("shortcut_current_list_"), true)
                }?.let(MainNavigationShortcut::CurrentList)
            stableId.startsWith("shortcut_chart_") ->
                ChartType.entries.firstOrNull {
                    it.name.equals(stableId.removePrefix("shortcut_chart_"), true)
                }?.let(MainNavigationShortcut::Chart)
            else -> null
        }
        return shortcut?.let {
            MainNavigationItem(stableId = it.stableId, visible = visible, shortcut = it)
        }
    }
}
