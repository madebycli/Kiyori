package com.axiel7.anihyou.feature.settings

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.axiel7.anihyou.core.model.CurrentListType
import com.axiel7.anihyou.core.model.media.ChartType
import com.axiel7.anihyou.core.model.navigation.MAX_VISIBLE_MAIN_DESTINATIONS
import com.axiel7.anihyou.core.model.navigation.MainNavigationConfig
import com.axiel7.anihyou.core.model.navigation.MainNavigationDestination
import com.axiel7.anihyou.core.model.navigation.MainNavigationIconKey
import com.axiel7.anihyou.core.model.navigation.MainNavigationItem
import com.axiel7.anihyou.core.model.navigation.MainNavigationShortcut
import com.axiel7.anihyou.core.model.navigation.MainNavigationShortcutCategory
import com.axiel7.anihyou.core.model.navigation.MainNavigationShortcutRegistry
import com.axiel7.anihyou.core.model.navigation.SeasonShortcutMode
import com.axiel7.anihyou.core.model.navigation.canSetVisibility
import com.axiel7.anihyou.core.model.navigation.containsShortcut
import com.axiel7.anihyou.core.model.navigation.iconKey
import com.axiel7.anihyou.core.resources.R as CoreR
import com.axiel7.anihyou.core.ui.common.navigation.NavActionManager
import com.axiel7.anihyou.core.ui.composables.common.BackIconButton
import org.koin.compose.viewmodel.koinViewModel
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.draggableHandle
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
fun MainNavigationSettingsView(navActionManager: NavActionManager) {
    val viewModel: MainNavigationSettingsViewModel = koinViewModel()
    val config by viewModel.config.collectAsStateWithLifecycle(MainNavigationConfig(items = emptyList()))

    MainNavigationSettingsContent(
        config = config,
        onBack = navActionManager::goBack,
        onVisibility = viewModel::setVisibility,
        onMove = viewModel::move,
        onRemove = viewModel::remove,
        onReset = viewModel::reset,
        onAdd = viewModel::add,
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun MainNavigationSettingsContent(
    config: MainNavigationConfig,
    onBack: () -> Unit,
    onVisibility: (MainNavigationItem, Boolean) -> Unit,
    onMove: (Int, Int) -> Unit,
    onRemove: (MainNavigationShortcut) -> Unit,
    onReset: () -> Unit,
    onAdd: (MainNavigationShortcut) -> Unit,
) {
    var showShortcutPicker by remember { mutableStateOf(false) }
    var displayedItems by remember { mutableStateOf(config.items) }
    val hapticFeedback = LocalHapticFeedback.current
    val lazyListState = rememberLazyListState()

    LaunchedEffect(config.items) {
        displayedItems = config.items
    }

    val reorderableState = rememberReorderableLazyListState(lazyListState) { from, to ->
        val fromIndex = from.index
        val toIndex = to.index.coerceAtLeast(1)
        if (fromIndex == 0 || fromIndex !in displayedItems.indices || toIndex !in displayedItems.indices) {
            return@rememberReorderableLazyListState
        }

        displayedItems = displayedItems.toMutableList().apply {
            add(toIndex, removeAt(fromIndex))
        }
        onMove(fromIndex, toIndex)
        hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentFrequentTick)
    }

    if (showShortcutPicker) {
        ModalBottomSheet(
            onDismissRequest = { showShortcutPicker = false },
        ) {
            MainNavigationShortcutPicker(
                config = config,
                onAdd = { shortcut ->
                    onAdd(shortcut)
                    showShortcutPicker = false
                },
                modifier = Modifier.navigationBarsPadding(),
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.main_navigation_editor_title)) },
                navigationIcon = { BackIconButton(onClick = onBack) },
                actions = {
                    TextButton(onClick = onReset) {
                        Text(stringResource(R.string.main_navigation_editor_reset))
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showShortcutPicker = true },
            ) {
                Icon(
                    painter = painterResource(CoreR.drawable.add_24),
                    contentDescription = stringResource(R.string.main_navigation_add),
                )
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            Text(
                text = stringResource(R.string.main_navigation_editor_help),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = stringResource(R.string.main_navigation_editor_rule),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = lazyListState,
                contentPadding = PaddingValues(bottom = 104.dp),
            ) {
                itemsIndexed(
                    items = displayedItems,
                    key = { _, item -> item.stableId },
                ) { _, item ->
                    ReorderableItem(
                        state = reorderableState,
                        key = item.stableId,
                    ) { isDragging ->
                        val elevation by animateDpAsState(
                            targetValue = if (isDragging) 8.dp else 0.dp,
                            label = "navigation-item-elevation",
                        )
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shadowElevation = elevation,
                            color = MaterialTheme.colorScheme.surface,
                        ) {
                            MainNavigationEditorItem(
                                config = config,
                                item = item,
                                onVisibility = { visible -> onVisibility(item, visible) },
                                onRemove = item.shortcut?.let { shortcut ->
                                    { onRemove(shortcut) }
                                },
                                dragHandle = {
                                    val mandatory = item.destination == MainNavigationDestination.HOME
                                    IconButton(
                                        onClick = {},
                                        enabled = !mandatory,
                                        modifier = if (mandatory) {
                                            Modifier
                                        } else {
                                            Modifier.draggableHandle(
                                                onDragStarted = {
                                                    hapticFeedback.performHapticFeedback(
                                                        HapticFeedbackType.GestureThresholdActivate
                                                    )
                                                },
                                                onDragStopped = {
                                                    hapticFeedback.performHapticFeedback(
                                                        HapticFeedbackType.GestureEnd
                                                    )
                                                },
                                            )
                                        },
                                    ) {
                                        Icon(
                                            painter = painterResource(CoreR.drawable.drag_handle_24),
                                            contentDescription = stringResource(
                                                R.string.main_navigation_reorder,
                                                item.label(),
                                            ),
                                            modifier = Modifier.alpha(if (mandatory) 0.35f else 1f),
                                        )
                                    }
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MainNavigationEditorItem(
    config: MainNavigationConfig,
    item: MainNavigationItem,
    onVisibility: (Boolean) -> Unit,
    onRemove: (() -> Unit)?,
    dragHandle: @Composable () -> Unit,
) {
    val mandatory = item.destination == MainNavigationDestination.HOME
    val label = item.label()
    val canToggle = !mandatory && config.canSetVisibility(item, !item.visible)

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 108.dp)
                .padding(start = 24.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(item.iconResource()),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.onSurface,
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleLarge,
                )
                if (mandatory) {
                    Text(
                        text = stringResource(R.string.main_navigation_required),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
            Switch(
                checked = item.visible,
                onCheckedChange = if (canToggle) onVisibility else null,
                enabled = canToggle,
            )
            if (onRemove != null) {
                IconButton(onClick = onRemove) {
                    Icon(
                        painter = painterResource(CoreR.drawable.close_24),
                        contentDescription = stringResource(R.string.main_navigation_remove, label),
                    )
                }
            } else {
                Spacer(Modifier.size(48.dp))
            }
            dragHandle()
        }
        HorizontalDivider(modifier = Modifier.padding(start = 106.dp))
    }
}

@Composable
private fun MainNavigationShortcutPicker(
    config: MainNavigationConfig,
    onAdd: (MainNavigationShortcut) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 680.dp),
        contentPadding = PaddingValues(bottom = 24.dp),
    ) {
        item {
            Text(
                text = stringResource(R.string.main_navigation_add),
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                style = MaterialTheme.typography.headlineMedium,
            )
        }

        MainNavigationShortcutCategory.entries.forEach { category ->
            item(key = "header-${category.name}") {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                ) {
                    Text(
                        text = when (category) {
                            MainNavigationShortcutCategory.HOME -> stringResource(R.string.main_navigation_shortcut_home)
                            MainNavigationShortcutCategory.DISCOVER -> stringResource(R.string.main_navigation_shortcut_discover)
                        },
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }

            val shortcuts = MainNavigationShortcutRegistry.definitions
                .filter { it.category == category }
                .flatMap { it.shortcuts }

            itemsIndexed(
                items = shortcuts,
                key = { _, shortcut -> shortcut.stableId },
            ) { _, shortcut ->
                val alreadyAdded = config.containsShortcut(shortcut)
                ShortcutPickerItem(
                    shortcut = shortcut,
                    alreadyAdded = alreadyAdded,
                    onClick = { onAdd(shortcut) },
                )
            }
        }

        if (config.visibleItems.size >= MAX_VISIBLE_MAIN_DESTINATIONS) {
            item {
                Text(
                    text = stringResource(R.string.main_navigation_shortcut_limit),
                    modifier = Modifier.padding(24.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun ShortcutPickerItem(
    shortcut: MainNavigationShortcut,
    alreadyAdded: Boolean,
    onClick: () -> Unit,
) {
    ListItem(
        headlineContent = {
            Text(shortcut.label())
        },
        supportingContent = {
            Text(
                if (alreadyAdded) {
                    stringResource(R.string.main_navigation_shortcut_already_added)
                } else {
                    shortcut.description()
                }
            )
        },
        leadingContent = {
            Icon(
                painter = painterResource(shortcut.iconKey.iconResource()),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
            )
        },
        modifier = Modifier.clickable(
            enabled = !alreadyAdded,
            onClick = onClick,
        ),
    )
}

@Composable
private fun MainNavigationItem.label(): String = shortcut?.label() ?: when (destination) {
    MainNavigationDestination.HOME -> stringResource(CoreR.string.home)
    MainNavigationDestination.ANIME -> stringResource(CoreR.string.anime)
    MainNavigationDestination.MANGA -> stringResource(CoreR.string.manga)
    MainNavigationDestination.PROFILE -> stringResource(CoreR.string.profile)
    MainNavigationDestination.EXPLORE -> stringResource(CoreR.string.explore)
    MainNavigationDestination.CALENDAR -> stringResource(CoreR.string.calendar)
    null -> stableId
}

@Composable
private fun MainNavigationShortcut.label(): String = when (this) {
    is MainNavigationShortcut.CurrentList -> when (type) {
        CurrentListType.AIRING -> stringResource(CoreR.string.airing)
        CurrentListType.BEHIND -> stringResource(CoreR.string.anime_behind)
        CurrentListType.ANIME -> stringResource(CoreR.string.watching)
        CurrentListType.MANGA -> stringResource(CoreR.string.reading)
        CurrentListType.NEXT_SEASON -> stringResource(CoreR.string.next_season)
    }

    is MainNavigationShortcut.Season -> when (mode) {
        SeasonShortcutMode.CURRENT -> stringResource(CoreR.string.season)
        SeasonShortcutMode.NEXT -> stringResource(CoreR.string.next_season)
    }

    is MainNavigationShortcut.Chart -> when (type) {
        ChartType.TOP_ANIME,
        ChartType.TOP_MANGA -> stringResource(CoreR.string.top_100)

        ChartType.POPULAR_ANIME,
        ChartType.POPULAR_MANGA -> stringResource(CoreR.string.top_popular)

        ChartType.UPCOMING_ANIME,
        ChartType.UPCOMING_MANGA -> stringResource(CoreR.string.upcoming)

        ChartType.AIRING_ANIME -> stringResource(CoreR.string.airing)
        ChartType.TOP_MOVIES -> stringResource(CoreR.string.top_movies)
        ChartType.PUBLISHING_MANGA -> stringResource(CoreR.string.publishing)
    }
}

@Composable
private fun MainNavigationShortcut.description(): String = when (this) {
    is MainNavigationShortcut.CurrentList -> stringResource(R.string.main_navigation_shortcut_existing_home)
    is MainNavigationShortcut.Season -> when (mode) {
        SeasonShortcutMode.CURRENT -> stringResource(R.string.main_navigation_shortcut_current_season)
        SeasonShortcutMode.NEXT -> stringResource(R.string.main_navigation_shortcut_next_season)
    }
    is MainNavigationShortcut.Chart -> stringResource(R.string.main_navigation_shortcut_existing_discover)
}

@DrawableRes
private fun MainNavigationItem.iconResource(): Int = iconKey.iconResource()

@DrawableRes
private fun MainNavigationIconKey.iconResource(): Int = when (this) {
    MainNavigationIconKey.HOME -> CoreR.drawable.home_24
    MainNavigationIconKey.ANIME -> CoreR.drawable.live_tv_24
    MainNavigationIconKey.MANGA -> CoreR.drawable.book_24
    MainNavigationIconKey.PROFILE -> CoreR.drawable.person_24
    MainNavigationIconKey.EXPLORE -> CoreR.drawable.explore_24
    MainNavigationIconKey.CALENDAR -> CoreR.drawable.calendar_month_24
    MainNavigationIconKey.AIRING -> CoreR.drawable.live_tv_24
    MainNavigationIconKey.BEHIND -> CoreR.drawable.schedule_24
    MainNavigationIconKey.WATCHING -> CoreR.drawable.play_arrow_24
    MainNavigationIconKey.READING -> CoreR.drawable.book_24
    MainNavigationIconKey.SEASON,
    MainNavigationIconKey.NEXT_SEASON -> CoreR.drawable.calendar_today_24
    MainNavigationIconKey.TOP -> CoreR.drawable.star_24
    MainNavigationIconKey.POPULAR -> CoreR.drawable.trending_up_24
    MainNavigationIconKey.UPCOMING -> CoreR.drawable.schedule_24
    MainNavigationIconKey.MOVIES -> CoreR.drawable.movie_24
    MainNavigationIconKey.PUBLISHING -> CoreR.drawable.book_24
}
