package com.axiel7.anihyou.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.axiel7.anihyou.core.model.base.Localizable
import com.axiel7.anihyou.core.model.navigation.MainNavigationConfig
import com.axiel7.anihyou.core.model.navigation.MainNavigationDestination
import com.axiel7.anihyou.core.model.navigation.MainNavigationItem
import com.axiel7.anihyou.core.model.navigation.MainNavigationShortcut
import com.axiel7.anihyou.core.resources.R
import com.axiel7.anihyou.core.ui.common.navigation.NavActionManager
import com.axiel7.anihyou.core.ui.composables.DefaultScaffoldWithSmallTopAppBar
import com.axiel7.anihyou.core.ui.composables.common.BackIconButton
import org.koin.compose.viewmodel.koinViewModel

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
private fun MainNavigationSettingsContent(
    config: MainNavigationConfig,
    onBack: () -> Unit,
    onVisibility: (String, Boolean) -> Unit,
    onMove: (String, Int) -> Unit,
    onRemove: (String) -> Unit,
    onReset: () -> Unit,
    onAdd: (MainNavigationShortcut) -> Unit,
) {
    DefaultScaffoldWithSmallTopAppBar(
        title = "Main navigation",
        navigationIcon = { BackIconButton(onClick = onBack) },
        actions = { TextButton(onClick = onReset) { Text("Reset") } },
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            item {
                Text(
                    text = "Visible tabs can be reordered. Home is always available.",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            items(config.items, key = MainNavigationItem::stableId) { item ->
                NavigationRow(
                    item = item,
                    label = item.label(),
                    canMoveUp = config.items.indexOf(item) > 1,
                    canMoveDown = config.items.indexOf(item) < config.items.lastIndex,
                    onVisibility = { onVisibility(item.stableId, it) },
                    onMove = { onMove(item.stableId, it) },
                    onRemove = { onRemove(item.stableId) },
                )
            }
            item { AddShortcuts(onAdd = onAdd) }
        }
    }
}

@Composable
private fun NavigationRow(
    item: MainNavigationItem,
    label: String,
    canMoveUp: Boolean,
    canMoveDown: Boolean,
    onVisibility: (Boolean) -> Unit,
    onMove: (Int) -> Unit,
    onRemove: () -> Unit,
) {
    val mandatory = item.destination == MainNavigationDestination.HOME
    Row(
        modifier = Modifier.fillMaxWidth().heightIn(min = 72.dp).padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(label, style = MaterialTheme.typography.titleMedium)
            if (mandatory) Text("Always visible", style = MaterialTheme.typography.bodySmall)
        }
        Box(modifier = Modifier.size(48.dp), contentAlignment = Alignment.Center) {
            Switch(checked = item.visible, onCheckedChange = if (mandatory) null else onVisibility)
        }
        if (item.shortcut != null) {
            IconButton(onClick = onRemove, modifier = Modifier.size(48.dp)) {
                Icon(painterResource(R.drawable.close_24), contentDescription = "Remove $label")
            }
        } else Box(modifier = Modifier.size(48.dp))
        Column(modifier = Modifier.size(48.dp), verticalArrangement = Arrangement.Center) {
            IconButton(onClick = { onMove(-1) }, enabled = canMoveUp, modifier = Modifier.size(24.dp)) {
                Icon(painterResource(R.drawable.arrow_upward_24), contentDescription = "Move $label up")
            }
            IconButton(onClick = { onMove(1) }, enabled = canMoveDown, modifier = Modifier.size(24.dp)) {
                Icon(painterResource(R.drawable.arrow_downward_24), contentDescription = "Move $label down")
            }
        }
    }
}

@Composable
private fun AddShortcuts(onAdd: (MainNavigationShortcut) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Add shortcuts", style = MaterialTheme.typography.titleMedium)
        Text("Shortcuts are hidden automatically when five tabs are already visible.", style = MaterialTheme.typography.bodySmall)
        ShortcutButtons(
            listOf(
                "Current season" to MainNavigationShortcut.Season(com.axiel7.anihyou.core.model.navigation.SeasonShortcutMode.CURRENT),
                "Next season" to MainNavigationShortcut.Season(com.axiel7.anihyou.core.model.navigation.SeasonShortcutMode.NEXT),
            ),
            onAdd,
        )
        ShortcutButtons(
            com.axiel7.anihyou.core.model.CurrentListType.entries.map { it.name.replace('_', ' ') to MainNavigationShortcut.CurrentList(it) },
            onAdd,
        )
        ShortcutButtons(
            com.axiel7.anihyou.core.model.media.ChartType.entries.map { it.name.replace('_', ' ') to MainNavigationShortcut.Chart(it) },
            onAdd,
        )
    }
}

@Composable
private fun ShortcutButtons(
    shortcuts: List<Pair<String, MainNavigationShortcut>>,
    onAdd: (MainNavigationShortcut) -> Unit,
) {
    shortcuts.chunked(2).forEach { row ->
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            row.forEach { (label, shortcut) ->
                TextButton(onClick = { onAdd(shortcut) }) { Text(label.lowercase().replaceFirstChar(Char::uppercase)) }
            }
        }
    }
}

@Composable
private fun MainNavigationItem.label(): String = when (val value = destination ?: shortcut) {
    MainNavigationDestination.HOME -> stringResource(R.string.home)
    MainNavigationDestination.ANIME -> stringResource(R.string.anime)
    MainNavigationDestination.MANGA -> stringResource(R.string.manga)
    MainNavigationDestination.PROFILE -> stringResource(R.string.profile)
    MainNavigationDestination.EXPLORE -> stringResource(R.string.explore)
    MainNavigationDestination.CALENDAR -> stringResource(R.string.calendar)
    is Localizable -> value.localized()
    is MainNavigationShortcut.Season -> if (value.mode.name == "CURRENT") "Current season" else "Next season"
    else -> stableId
}
