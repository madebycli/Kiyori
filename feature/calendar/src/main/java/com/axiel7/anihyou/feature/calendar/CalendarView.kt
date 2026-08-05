package com.axiel7.anihyou.feature.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items as listItems
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.axiel7.anihyou.core.base.UNKNOWN_CHAR
import com.axiel7.anihyou.core.common.utils.DateUtils.timestampToTimeString
import com.axiel7.anihyou.core.network.AiringAnimesQuery
import com.axiel7.anihyou.core.resources.R as CoreR
import com.axiel7.anihyou.core.ui.common.LocalBlurAdult
import com.axiel7.anihyou.core.ui.common.SnackbarManager
import com.axiel7.anihyou.core.ui.common.navigation.NavActionManager
import com.axiel7.anihyou.core.ui.common.rememberSnackbarManager
import com.axiel7.anihyou.core.ui.composables.DefaultScaffoldWithSmallTopAppBar
import com.axiel7.anihyou.core.ui.composables.common.BackIconButton
import com.axiel7.anihyou.core.ui.composables.common.ErrorDialogHandler
import com.axiel7.anihyou.core.ui.composables.list.OnBottomReached
import com.axiel7.anihyou.core.ui.composables.media.MEDIA_POSTER_SMALL_WIDTH
import com.axiel7.anihyou.core.ui.composables.media.MediaItemHorizontal
import com.axiel7.anihyou.core.ui.composables.media.MediaItemHorizontalPlaceholder
import com.axiel7.anihyou.core.ui.composables.media.MediaItemVertical
import com.axiel7.anihyou.core.ui.composables.media.MediaItemVerticalPlaceholder
import com.axiel7.anihyou.core.ui.composables.scores.SmallScoreIndicator
import com.axiel7.anihyou.core.ui.theme.AniHyouTheme
import com.axiel7.anihyou.feature.editmedia.EditMediaSheet
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarView(
    isLoggedIn: Boolean,
    navActionManager: NavActionManager,
    isMainDestination: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(),
    modifier: Modifier = Modifier,
) {
    val viewModel: CalendarHostViewModel = koinViewModel()
    val onMyList by viewModel.onMyList.collectAsStateWithLifecycle(initialValue = null)
    val displayGrid by viewModel.displayGrid.collectAsStateWithLifecycle(initialValue = false)
    val displayAdult by viewModel.displayAdult.collectAsStateWithLifecycle(initialValue = null)
    val dateCounts by viewModel.dateCounts.collectAsStateWithLifecycle()

    CalendarViewContent(
        isLoggedIn = isLoggedIn,
        onMyList = onMyList,
        onMyListChanged = viewModel::onMyListChanged,
        displayGrid = displayGrid,
        onDisplayGridChanged = viewModel::onDisplayGridChanged,
        displayAdult = displayAdult,
        dateCounts = dateCounts,
        onVisibleWeekChanged = viewModel::loadDateCounts,
        navActionManager = navActionManager,
        isMainDestination = isMainDestination,
        contentPadding = contentPadding,
        modifier = modifier,
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
    displayAdult: Boolean?,
    dateCounts: Map<LocalDate, Int?>,
    onVisibleWeekChanged: (List<LocalDate>, Boolean?, Boolean?) -> Unit,
    navActionManager: NavActionManager,
    isMainDestination: Boolean,
    contentPadding: PaddingValues,
    modifier: Modifier,
) {
    val range = remember { CalendarDateRange(LocalDate.now()) }
    var selectedPage by rememberSaveable { mutableIntStateOf(0) }
    val pagerState = rememberPagerState(initialPage = selectedPage) { range.pageCount }
    val scope = rememberCoroutineScope()
    val snackbarManager = rememberSnackbarManager()
    val showEditSheet = remember { mutableStateOf(false) }
    val selectedDate = range.dateForPage(selectedPage)
    val visibleWeek = range.visibleWeek(selectedDate)
    val topAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        rememberTopAppBarState()
    )
    val navigateToPage: (Int) -> Unit = { page ->
        val targetPage = page.coerceIn(0, range.pageCount - 1)
        selectedPage = targetPage
        scope.launch {
            pagerState.animateScrollToPage(targetPage)
        }
    }

    LaunchedEffect(visibleWeek, onMyList, displayAdult) {
        onVisibleWeekChanged(visibleWeek, onMyList, displayAdult)
    }

    LaunchedEffect(pagerState.targetPage) {
        selectedPage = pagerState.targetPage.coerceIn(0, range.pageCount - 1)
    }

    DefaultScaffoldWithSmallTopAppBar(
        title = stringResource(CoreR.string.calendar),
        navigationIcon = {
            if (!isMainDestination) {
                BackIconButton(onClick = navActionManager::goBack)
            }
        },
        actions = {
            CalendarAppBarActions(
                onMyList = onMyList,
                onMyListChanged = onMyListChanged,
                displayGrid = displayGrid,
                onDisplayGridChanged = onDisplayGridChanged,
            )
        },
        snackbarHost = snackbarManager::SnackbarHost,
        scrollBehavior = topAppBarScrollBehavior,
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
                .padding(
                    start = padding.calculateStartPadding(LocalLayoutDirection.current),
                    top = padding.calculateTopPadding(),
                    end = padding.calculateEndPadding(LocalLayoutDirection.current),
                ),
        ) {
            CalendarDateStrip(
                dates = visibleWeek,
                selectedDate = selectedDate,
                dateCounts = dateCounts,
                firstDate = range.firstDate,
                lastDate = range.lastDate,
                onDateSelected = { date ->
                    navigateToPage(range.pageForDate(date))
                },
                onPreviousWeek = {
                    navigateToPage(selectedPage - 7)
                },
                onNextWeek = {
                    navigateToPage(selectedPage + 7)
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

                LaunchedEffect(date) {
                    viewModel.setDate(date)
                }
                LaunchedEffect(onMyList) {
                    if (uiState.onMyList != onMyList) {
                        viewModel.setOnMyList(onMyList)
                    }
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
                        top = 2.dp,
                        bottom = padding.calculateBottomPadding() +
                            contentPadding.calculateBottomPadding(),
                    ),
                )
            }
        }
    }
}

@Composable
private fun CalendarDateStrip(
    dates: List<LocalDate>,
    selectedDate: LocalDate,
    dateCounts: Map<LocalDate, Int?>,
    firstDate: LocalDate,
    lastDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onPreviousWeek: () -> Unit,
    onNextWeek: () -> Unit,
) {
    val locale = Locale.getDefault()
    val dateFormatter = remember(locale) { DateTimeFormatter.ofPattern("d. MMM", locale) }
    val previousEnabled = selectedDate > firstDate
    val nextEnabled = selectedDate < lastDate

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = onPreviousWeek,
                enabled = previousEnabled,
            ) {
                Icon(
                    painter = painterResource(CoreR.drawable.arrow_back_24),
                    contentDescription = stringResource(R.string.calendar_previous_week),
                )
            }
            Text(
                text = "${dates.first().format(dateFormatter)} – ${dates.last().format(dateFormatter)}",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            IconButton(
                onClick = onNextWeek,
                enabled = nextEnabled,
            ) {
                Icon(
                    painter = painterResource(CoreR.drawable.arrow_forward_24),
                    contentDescription = stringResource(R.string.calendar_next_week),
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
        ) {
            dates.forEach { date ->
                CalendarDateTab(
                    date = date,
                    count = dateCounts[date],
                    countLoaded = date in dateCounts,
                    selected = date == selectedDate,
                    enabled = date in firstDate..lastDate,
                    locale = locale,
                    onClick = { onDateSelected(date) },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun CalendarDateTab(
    date: LocalDate,
    count: Int?,
    countLoaded: Boolean,
    selected: Boolean,
    enabled: Boolean,
    locale: Locale,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dayLabel = remember(date, locale) {
        date.dayOfWeek.getDisplayName(TextStyle.SHORT, locale).removeSuffix(".")
    }
    val fullDate = remember(date, locale) {
        date.format(DateTimeFormatter.ofPattern("EEEE, d. MMMM", locale))
    }
    val semanticCount = count?.let { stringResource(R.string.calendar_airing_count, it) }

    Column(
        modifier = modifier
            .height(72.dp)
            .semantics {
                contentDescription = listOfNotNull(fullDate, semanticCount).joinToString(", ")
                this.selected = selected
            }
            .clickable(
                enabled = enabled,
                role = Role.Tab,
                onClick = onClick,
            )
            .alpha(if (enabled) 1f else 0.45f),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(2.dp))
        Text(
            text = dayLabel,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = date.dayOfMonth.toString(),
            style = MaterialTheme.typography.titleMedium,
            color = if (selected) {
                MaterialTheme.colorScheme.onSurface
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
        )
        Text(
            text = when {
                count != null -> count.toString()
                countLoaded -> "—"
                else -> ""
            },
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.weight(1f))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(
                    if (selected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.surface
                ),
        )
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
    if (showEditSheet.value && uiState.selectedItem?.media != null) {
        EditMediaSheet(
            mediaDetails = uiState.selectedItem.media!!.basicMediaDetails,
            listEntry = uiState.selectedItem.media!!.mediaListEntry?.basicMediaListEntry,
            onEntryUpdated = { events?.onUpdateListEntry(it) },
            onDismissed = { showEditSheet.value = false },
        )
    }

    if (displayGrid) {
        CalendarGrid(
            uiState = uiState,
            events = events,
            isLoggedIn = isLoggedIn,
            snackbarManager = snackbarManager,
            showEditSheet = showEditSheet,
            navActionManager = navActionManager,
            modifier = modifier,
            contentPadding = contentPadding,
        )
    } else {
        CalendarList(
            uiState = uiState,
            events = events,
            isLoggedIn = isLoggedIn,
            snackbarManager = snackbarManager,
            showEditSheet = showEditSheet,
            navActionManager = navActionManager,
            modifier = modifier,
            contentPadding = contentPadding,
        )
    }
}

@Composable
private fun CalendarList(
    uiState: CalendarUiState,
    events: CalendarEvent?,
    isLoggedIn: Boolean,
    snackbarManager: SnackbarManager,
    showEditSheet: MutableState<Boolean>,
    navActionManager: NavActionManager,
    modifier: Modifier,
    contentPadding: PaddingValues,
) {
    val blurAdult = LocalBlurAdult.current
    val haptic = LocalHapticFeedback.current
    val listState = rememberLazyListState()
    listState.OnBottomReached(buffer = 3) { events?.onLoadMore() }

    LazyColumn(
        modifier = modifier,
        state = listState,
        contentPadding = contentPadding,
    ) {
        listItems(
            items = uiState.weeklyAnime,
            key = AiringAnimesQuery.AiringSchedule::id,
            contentType = { "calendar-airing" },
        ) { item ->
            MediaItemHorizontal(
                title = item.media?.basicMediaDetails?.title?.userPreferred.orEmpty(),
                imageUrl = item.media?.coverImage?.large,
                blurImage = blurAdult && item.media?.isAdult == true,
                subtitle1 = {
                    Text(
                        text = stringResource(
                            CoreR.string.episode_airing_at,
                            item.episode,
                            item.airingAt.toLong().timestampToTimeString() ?: UNKNOWN_CHAR,
                        ),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
                subtitle2 = {
                    item.media?.meanScore?.let { score ->
                        SmallScoreIndicator(score = score)
                    }
                },
                status = item.media?.mediaListEntry?.basicMediaListEntry?.status,
                onClick = { navActionManager.toMediaDetails(item.mediaId) },
                onLongClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    openEditSheet(
                        item = item,
                        events = events,
                        isLoggedIn = isLoggedIn,
                        snackbarManager = snackbarManager,
                        showEditSheet = showEditSheet,
                    )
                },
            )
        }

        if (uiState.isLoading) {
            items(8, contentType = { "calendar-placeholder" }) {
                MediaItemHorizontalPlaceholder()
            }
        }
    }
}

@Composable
private fun CalendarGrid(
    uiState: CalendarUiState,
    events: CalendarEvent?,
    isLoggedIn: Boolean,
    snackbarManager: SnackbarManager,
    showEditSheet: MutableState<Boolean>,
    navActionManager: NavActionManager,
    modifier: Modifier,
    contentPadding: PaddingValues,
) {
    val blurAdult = LocalBlurAdult.current
    val haptic = LocalHapticFeedback.current
    val gridState = rememberLazyGridState()
    gridState.OnBottomReached(buffer = 3) { events?.onLoadMore() }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = (MEDIA_POSTER_SMALL_WIDTH + 8).dp),
        modifier = modifier,
        state = gridState,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
    ) {
        items(
            items = uiState.weeklyAnime,
            key = AiringAnimesQuery.AiringSchedule::id,
            contentType = { "calendar-airing-grid" },
        ) { item ->
            MediaItemVertical(
                title = item.media?.basicMediaDetails?.title?.userPreferred.orEmpty(),
                imageUrl = item.media?.coverImage?.large,
                blurImage = blurAdult && item.media?.isAdult == true,
                modifier = Modifier.wrapContentWidth(),
                subtitle = {
                    Text(
                        text = stringResource(
                            CoreR.string.episode_airing_at,
                            item.episode,
                            item.airingAt.toLong().timestampToTimeString() ?: UNKNOWN_CHAR,
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp,
                        lineHeight = 17.sp,
                    )
                },
                status = item.media?.mediaListEntry?.basicMediaListEntry?.status,
                minLines = 1,
                onClick = { navActionManager.toMediaDetails(item.mediaId) },
                onLongClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    openEditSheet(
                        item = item,
                        events = events,
                        isLoggedIn = isLoggedIn,
                        snackbarManager = snackbarManager,
                        showEditSheet = showEditSheet,
                    )
                },
            )
        }

        if (uiState.isLoading) {
            items(13, contentType = { "calendar-grid-placeholder" }) {
                MediaItemVerticalPlaceholder()
            }
        }
    }
}

private fun openEditSheet(
    item: AiringAnimesQuery.AiringSchedule,
    events: CalendarEvent?,
    isLoggedIn: Boolean,
    snackbarManager: SnackbarManager,
    showEditSheet: MutableState<Boolean>,
) {
    if (isLoggedIn) {
        events?.selectItem(item)
        showEditSheet.value = true
    } else {
        snackbarManager.showNotLoggedInSnackbar()
    }
}

@Composable
private fun CalendarAppBarActions(
    onMyList: Boolean?,
    onMyListChanged: (Boolean?) -> Unit,
    displayGrid: Boolean,
    onDisplayGridChanged: (Boolean) -> Unit,
) {
    var menuOpened by remember { mutableStateOf(false) }
    val activeFilter = CalendarListFilter.from(onMyList)

    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = { onDisplayGridChanged(!displayGrid) }) {
            Icon(
                painter = painterResource(
                    if (displayGrid) CoreR.drawable.format_list_bulleted_24
                    else CoreR.drawable.grid_view_24
                ),
                contentDescription = stringResource(
                    if (displayGrid) R.string.calendar_show_list
                    else R.string.calendar_show_grid
                ),
            )
        }

        Box {
            IconButton(onClick = { menuOpened = true }) {
                Icon(
                    painter = painterResource(CoreR.drawable.filter_list_24),
                    contentDescription = stringResource(R.string.calendar_filter),
                )
            }
            DropdownMenu(
                expanded = menuOpened,
                onDismissRequest = { menuOpened = false },
            ) {
                CalendarListFilter.entries.forEach { filter ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                when (filter) {
                                    CalendarListFilter.ALL -> stringResource(R.string.calendar_filter_all)
                                    CalendarListFilter.ON_MY_LIST -> stringResource(R.string.calendar_filter_on_list)
                                    CalendarListFilter.NOT_ON_MY_LIST -> stringResource(R.string.calendar_filter_not_on_list)
                                }
                            )
                        },
                        onClick = {
                            onMyListChanged(filter.onMyList)
                            menuOpened = false
                        },
                        leadingIcon = {
                            if (filter == activeFilter) {
                                Icon(
                                    painter = painterResource(CoreR.drawable.check_20),
                                    contentDescription = null,
                                )
                            } else {
                                Spacer(Modifier.width(24.dp))
                            }
                        },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CalendarDateStripPreview() {
    AniHyouTheme {
        Surface {
            val today = LocalDate.now()
            CalendarDateStrip(
                dates = CalendarDateRange(today).visibleWeek(today),
                selectedDate = today,
                dateCounts = emptyMap(),
                firstDate = today,
                lastDate = today.plusDays(14),
                onDateSelected = {},
                onPreviousWeek = {},
                onNextWeek = {},
            )
        }
    }
}
