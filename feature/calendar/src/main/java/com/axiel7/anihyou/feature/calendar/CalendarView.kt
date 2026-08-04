package com.axiel7.anihyou.feature.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.items as listItems
import androidx.compose.material3.DropdownMenuGroup
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.DropdownMenuPopup
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.axiel7.anihyou.core.base.UNKNOWN_CHAR
import com.axiel7.anihyou.core.common.utils.DateUtils.timestampToTimeString
import com.axiel7.anihyou.core.resources.R
import com.axiel7.anihyou.core.ui.common.LocalBlurAdult
import com.axiel7.anihyou.core.ui.common.SnackbarManager
import com.axiel7.anihyou.core.ui.common.navigation.NavActionManager
import com.axiel7.anihyou.core.ui.common.rememberSnackbarManager
import com.axiel7.anihyou.core.ui.composables.DefaultScaffoldWithSmallTopAppBar
import com.axiel7.anihyou.core.ui.composables.TabRowWithPager
import com.axiel7.anihyou.core.ui.composables.common.BackIconButton
import com.axiel7.anihyou.core.ui.composables.common.ErrorDialogHandler
import com.axiel7.anihyou.core.ui.composables.list.OnBottomReached
import com.axiel7.anihyou.core.ui.composables.media.MEDIA_POSTER_SMALL_WIDTH
import com.axiel7.anihyou.core.ui.composables.media.MediaItemVertical
import com.axiel7.anihyou.core.ui.composables.media.MediaItemVerticalPlaceholder
import com.axiel7.anihyou.core.ui.composables.media.MediaItemHorizontal
import com.axiel7.anihyou.core.ui.composables.media.MediaItemHorizontalPlaceholder
import com.axiel7.anihyou.core.ui.theme.AniHyouTheme
import com.axiel7.anihyou.feature.editmedia.EditMediaSheet
import org.koin.compose.viewmodel.koinViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.launch

@Composable
fun CalendarView(
    isLoggedIn: Boolean,
    navActionManager: NavActionManager,
    isMainDestination: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val viewModel: CalendarHostViewModel = koinViewModel()
    val onMyList by viewModel.onMyList.collectAsStateWithLifecycle(initialValue = null)
    val displayGrid by viewModel.displayGrid.collectAsStateWithLifecycle(initialValue = false)

    CalendarViewContent(
        isLoggedIn = isLoggedIn,
        onMyList = onMyList,
        onMyListChanged = viewModel::onMyListChanged,
        displayGrid = displayGrid,
        onDisplayGridChanged = viewModel::onDisplayGridChanged,
        navActionManager = navActionManager,
        isMainDestination = isMainDestination,
        contentPadding = contentPadding,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CalendarViewContent(
    isLoggedIn: Boolean,
    onMyList: Boolean?,
    onMyListChanged: (Boolean?) -> Unit,
    displayGrid: Boolean,
    onDisplayGridChanged: (Boolean) -> Unit,
    navActionManager: NavActionManager,
    isMainDestination: Boolean,
    contentPadding: PaddingValues,
) {
    if (isMainDestination) {
        DateCalendarContent(
            isLoggedIn = isLoggedIn,
            onMyList = onMyList,
            onMyListChanged = onMyListChanged,
            displayGrid = displayGrid,
            onDisplayGridChanged = onDisplayGridChanged,
            navActionManager = navActionManager,
            contentPadding = contentPadding,
        )
        return
    }
    val topAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        rememberTopAppBarState()
    )
    val snackbarManager = rememberSnackbarManager()
    val showEditSheet = remember { mutableStateOf(false) }

    DefaultScaffoldWithSmallTopAppBar(
        title = stringResource(R.string.calendar),
        navigationIcon = if (isMainDestination) ({}) else { BackIconButton(onClick = navActionManager::goBack) },
        actions = {
            AppBarActions(
                onMyList = onMyList,
                onMyListChanged = onMyListChanged,
                displayGrid = displayGrid,
                onDisplayGridChanged = onDisplayGridChanged,
            )
        },
        snackbarHost = snackbarManager::SnackbarHost,
        scrollBehavior = topAppBarScrollBehavior
    ) { padding ->
        TabRowWithPager(
            tabs = CalendarTab.tabRows,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = padding.calculateStartPadding(LocalLayoutDirection.current),
                    top = padding.calculateTopPadding(),
                    end = padding.calculateEndPadding(LocalLayoutDirection.current),
                ),
            initialPage = LocalDate.now().dayOfWeek.value - 1,
            isTabScrollable = true,
        ) { page ->
            val weekday = CalendarTab.tabRows[page].value.ordinal + 1
            val viewModel: CalendarViewModel = koinViewModel(key = weekday.toString())
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            ErrorDialogHandler(uiState, onDismiss = viewModel::onErrorDisplayed)

            LaunchedEffect(weekday) {
                viewModel.setWeekday(weekday)
            }
            LaunchedEffect(onMyList) {
                if (uiState.onMyList != onMyList)
                    viewModel.setOnMyList(onMyList)
            }

            CalendarDayView(
                isLoggedIn = isLoggedIn,
                snackbarManager = snackbarManager,
                uiState = uiState,
                events = viewModel,
                displayGrid = displayGrid,
                showEditSheet = showEditSheet,
                navActionManager = navActionManager,
                modifier = Modifier
                    .fillMaxHeight()
                    .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
                contentPadding = PaddingValues(
                    top = 16.dp,
                    bottom = padding.calculateBottomPadding() + contentPadding.calculateBottomPadding()
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateCalendarContent(
    isLoggedIn: Boolean,
    onMyList: Boolean?,
    onMyListChanged: (Boolean?) -> Unit,
    displayGrid: Boolean,
    onDisplayGridChanged: (Boolean) -> Unit,
    navActionManager: NavActionManager,
    contentPadding: PaddingValues,
) {
    val range = remember { CalendarDateRange(LocalDate.now()) }
    var selectedPage by rememberSaveable { mutableIntStateOf(0) }
    val pagerState = rememberPagerState(initialPage = selectedPage) { range.pageCount }
    val scope = rememberCoroutineScope()
    val snackbarManager = rememberSnackbarManager()
    val showEditSheet = remember { mutableStateOf(false) }

    LaunchedEffect(pagerState.settledPage) {
        selectedPage = pagerState.settledPage.coerceIn(0, range.pageCount - 1)
    }

    DefaultScaffoldWithSmallTopAppBar(
        title = stringResource(R.string.calendar),
        actions = {
            AppBarActions(
                onMyList = onMyList,
                onMyListChanged = onMyListChanged,
                displayGrid = displayGrid,
                onDisplayGridChanged = onDisplayGridChanged,
            )
        },
        snackbarHost = snackbarManager::SnackbarHost,
        scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState()),
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = padding.calculateStartPadding(LocalLayoutDirection.current),
                    top = padding.calculateTopPadding(),
                    end = padding.calculateEndPadding(LocalLayoutDirection.current),
                ),
        ) {
            CalendarWeekHeader(
                dates = range.visibleWeek(range.dateForPage(selectedPage)),
                selectedDate = range.dateForPage(selectedPage),
                onDateSelected = { date ->
                    scope.launch { pagerState.animateScrollToPage(range.pageForDate(date)) }
                },
                onPreviousWeek = {
                    scope.launch { pagerState.animateScrollToPage((selectedPage - 7).coerceAtLeast(0)) }
                },
                onNextWeek = {
                    scope.launch { pagerState.animateScrollToPage((selectedPage + 7).coerceAtMost(range.pageCount - 1)) }
                },
            )
            HorizontalPager(
                state = pagerState,
                beyondViewportPageCount = 1,
                modifier = Modifier.weight(1f),
            ) { page ->
                val date = range.dateForPage(page)
                val viewModel: CalendarViewModel = koinViewModel(key = "calendar-$date")
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                ErrorDialogHandler(uiState, onDismiss = viewModel::onErrorDisplayed)
                LaunchedEffect(date) { viewModel.setDate(date) }
                LaunchedEffect(onMyList) {
                    if (uiState.onMyList != onMyList) viewModel.setOnMyList(onMyList)
                }
                CalendarDayView(
                    isLoggedIn = isLoggedIn,
                    snackbarManager = snackbarManager,
                    uiState = uiState,
                    events = viewModel,
                    displayGrid = displayGrid,
                    showEditSheet = showEditSheet,
                    navActionManager = navActionManager,
                    modifier = Modifier.fillMaxHeight(),
                    contentPadding = PaddingValues(
                        top = 16.dp,
                        bottom = padding.calculateBottomPadding() + contentPadding.calculateBottomPadding(),
                    ),
                )
            }
        }
    }
}

@Composable
private fun CalendarWeekHeader(
    dates: List<LocalDate>,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onPreviousWeek: () -> Unit,
    onNextWeek: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onPreviousWeek, modifier = Modifier.size(48.dp)) {
            Icon(painterResource(R.drawable.arrow_back_24), contentDescription = "Previous week")
        }
        dates.forEach { date ->
            val selected = date == selectedDate
            TextButton(
                onClick = { onDateSelected(date) },
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = date.format(DateTimeFormatter.ofPattern("EE\nd")),
                    color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                )
            }
        }
        IconButton(onClick = onNextWeek, modifier = Modifier.size(48.dp)) {
            Icon(painterResource(R.drawable.arrow_forward_24), contentDescription = "Next week")
        }
    }
}

@Composable
private fun CalendarDayView(
    isLoggedIn: Boolean,
    snackbarManager: SnackbarManager,
    uiState: CalendarUiState,
    events: CalendarEvent?,
    displayGrid: Boolean,
    showEditSheet: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    navActionManager: NavActionManager,
) {
    val blurAdult = LocalBlurAdult.current
    val haptic = LocalHapticFeedback.current

    val gridState = rememberLazyGridState()
    gridState.OnBottomReached(buffer = 3) {
        events?.onLoadMore()
    }

    if (showEditSheet.value && uiState.selectedItem?.media != null) {
        EditMediaSheet(
            mediaDetails = uiState.selectedItem.media!!.basicMediaDetails,
            listEntry = uiState.selectedItem.media!!.mediaListEntry?.basicMediaListEntry,
            onEntryUpdated = {
                events?.onUpdateListEntry(it)
            },
            onDismissed = {
                showEditSheet.value = false
            }
        )
    }

    if (!displayGrid) {
        val listState = rememberLazyListState()
        listState.OnBottomReached(buffer = 3) { events?.onLoadMore() }
        LazyColumn(
            modifier = modifier,
            state = listState,
            contentPadding = contentPadding,
        ) {
            listItems(uiState.weeklyAnime, contentType = { it }) { item ->
                MediaItemHorizontal(
                    title = item.media?.basicMediaDetails?.title?.userPreferred.orEmpty(),
                    imageUrl = item.media?.coverImage?.large,
                    blurImage = blurAdult && item.media?.isAdult == true,
                    subtitle1 = {
                        Text(
                            text = stringResource(
                                R.string.episode_airing_at,
                                item.episode,
                                item.airingAt.toLong().timestampToTimeString() ?: UNKNOWN_CHAR,
                            ),
                            color = MaterialTheme.colorScheme.outline,
                        )
                    },
                    status = item.media?.mediaListEntry?.basicMediaListEntry?.status,
                    onClick = { navActionManager.toMediaDetails(item.mediaId) },
                    onLongClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        if (isLoggedIn) {
                            events?.selectItem(item)
                            showEditSheet.value = true
                        } else snackbarManager.showNotLoggedInSnackbar()
                    },
                )
            }
            if (uiState.isLoading) listItems(8) { MediaItemHorizontalPlaceholder() }
        }
        return
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = (MEDIA_POSTER_SMALL_WIDTH + 8).dp),
        modifier = modifier,
        state = gridState,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
    ) {
        items(
            items = uiState.weeklyAnime,
            contentType = { it }
        ) { item ->
            MediaItemVertical(
                title = item.media?.basicMediaDetails?.title?.userPreferred.orEmpty(),
                imageUrl = item.media?.coverImage?.large,
                blurImage = blurAdult && item.media?.isAdult == true,
                modifier = Modifier.wrapContentWidth(),
                subtitle = {
                    Text(
                        text = stringResource(
                            R.string.episode_airing_at,
                            item.episode,
                            item.airingAt.toLong().timestampToTimeString() ?: UNKNOWN_CHAR
                        ),
                        color = MaterialTheme.colorScheme.outline,
                        fontSize = 14.sp,
                        lineHeight = 17.sp
                    )
                },
                status = item.media?.mediaListEntry?.basicMediaListEntry?.status,
                minLines = 1,
                onClick = {
                    navActionManager.toMediaDetails(item.mediaId)
                },
                onLongClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    if (isLoggedIn) {
                        events?.selectItem(item)
                        showEditSheet.value = true
                    } else {
                        snackbarManager.showNotLoggedInSnackbar()
                    }
                }
            )
        }
        if (uiState.isLoading) {
            items(13) {
                MediaItemVerticalPlaceholder()
            }
        }
    }//: LazyVerticalGrid
}

@Composable
private fun AppBarActions(
    onMyList: Boolean?,
    onMyListChanged: (Boolean?) -> Unit,
    displayGrid: Boolean,
    onDisplayGridChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var menuOpened by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
            .wrapContentSize(Alignment.TopStart)
    ) {
        IconButton(onClick = { onDisplayGridChanged(!displayGrid) }) {
            Icon(
                painter = painterResource(
                    if (displayGrid) R.drawable.format_list_bulleted_24 else R.drawable.grid_view_24
                ),
                contentDescription = if (displayGrid) "Show list" else "Show grid",
            )
        }
        IconButton(
            onClick = { menuOpened = !menuOpened },
            shapes = IconButtonDefaults.shapes(),
        ) {
            Icon(
                painter = painterResource(R.drawable.more_vert_24),
                contentDescription = stringResource(R.string.show_more),
            )
        }
        DropdownMenuPopup(
            expanded = menuOpened,
            onDismissRequest = { menuOpened = false },
        ) {
            DropdownMenuGroup(
                shapes = MenuDefaults.groupShapes(),
            ) {
                DropdownMenuItem(
                    checked = onMyList != null,
                    onCheckedChange = {
                        onMyListChanged(
                            when (onMyList) {
                                null -> true
                                true -> false
                                false -> null
                            }
                        )
                        menuOpened = false
                    },
                    text = { Text(text = stringResource(R.string.on_my_list)) },
                    shapes = MenuDefaults.itemShape(0, 1),
                    checkedLeadingIcon = {
                        if (onMyList != null) {
                            Icon(
                                painter = painterResource(
                                    id = if (onMyList) R.drawable.check_20 else R.drawable.close_20
                                ),
                                contentDescription = null,
                                modifier = Modifier.size(MenuDefaults.LeadingIconSize)
                            )
                        }
                    },
                )
            }
        }
    }
}

@Preview
@Composable
private fun CalendarViewPreview() {
    AniHyouTheme {
        Surface {
            CalendarDayView(
                isLoggedIn = true,
                snackbarManager = rememberSnackbarManager(),
                uiState = CalendarUiState(),
                events = null,
                displayGrid = false,
                showEditSheet = remember { mutableStateOf(false) },
                navActionManager = NavActionManager.rememberNavActionManager()
            )
        }
    }
}
