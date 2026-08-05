package com.axiel7.anihyou.core.model.navigation

/** APK-recovered editor operations expressed on the current persistence model. */
fun MainNavigationConfig.canSetVisibility(
    item: MainNavigationItem,
    visible: Boolean,
): Boolean {
    if (item.destination == MainNavigationDestination.HOME) return false
    if (item.visible == visible) return true

    val visibleCount = visibleItems.size
    return if (visible) {
        visibleCount < MAX_VISIBLE_MAIN_DESTINATIONS
    } else {
        visibleCount > MIN_VISIBLE_MAIN_DESTINATIONS
    }
}

fun MainNavigationConfig.canSetVisibility(
    destination: MainNavigationDestinationId,
    visible: Boolean,
): Boolean = items.firstOrNull { it.destination == destination }
    ?.let { canSetVisibility(it, visible) }
    ?: false

fun MainNavigationConfig.setVisibility(
    item: MainNavigationItem,
    visible: Boolean,
): MainNavigationConfig = if (canSetVisibility(item, visible)) {
    withVisibility(item.stableId, visible)
} else {
    normalized()
}

fun MainNavigationConfig.setVisibility(
    destination: MainNavigationDestinationId,
    visible: Boolean,
): MainNavigationConfig = items.firstOrNull { it.destination == destination }
    ?.let { setVisibility(it, visible) }
    ?: normalized()

fun MainNavigationConfig.move(fromIndex: Int, toIndex: Int): MainNavigationConfig {
    if (fromIndex !in items.indices || toIndex !in items.indices || fromIndex == toIndex) {
        return normalized()
    }

    val source = items[fromIndex]
    if (source.destination == MainNavigationDestination.HOME) return normalized()

    val target = toIndex.coerceIn(1, items.lastIndex)
    val reordered = items.toMutableList().apply {
        add(target, removeAt(fromIndex))
    }
    return copy(items = reordered).normalized()
}

fun MainNavigationConfig.containsShortcut(shortcut: MainNavigationShortcut): Boolean =
    items.any { it.shortcut == shortcut }

fun MainNavigationConfig.containsShortcut(type: MainNavigationShortcutType): Boolean =
    items.any { it.shortcut?.type == type }

val MainNavigationConfig.shortcuts: List<MainNavigationShortcut>
    get() = items.mapNotNull(MainNavigationItem::shortcut)

val MainNavigationConfig.visibleDestinations: List<MainNavigationDestinationId>
    get() = visibleItems.mapNotNull(MainNavigationItem::destination)

val MainNavigationConfig.seasonShortcut: MainNavigationShortcut.Season?
    get() = shortcuts.filterIsInstance<MainNavigationShortcut.Season>().firstOrNull()

fun MainNavigationConfig.resolveVisibleDestination(
    requested: MainNavigationDestinationId,
): MainNavigationDestinationId = when {
    visibleItems.any { it.destination == requested } -> requested
    else -> visibleDestinations.firstOrNull() ?: MainNavigationDestination.HOME
}
