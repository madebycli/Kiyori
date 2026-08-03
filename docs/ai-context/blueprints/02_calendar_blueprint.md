> **Reconstructed blueprint:** adapt to the current upstream APIs and compile; do not claim this is the exact deleted source.

# Reconstructed Calendar Blueprint with Swipe v2

> Adapt API names to current Compose/upstream versions.

## Date range

```kotlin
data class CalendarDateRange(
    val today: LocalDate,
) {
    val firstDate = today
    val lastDate = today.plusDays(14)
    val pageCount = 15

    fun dateForPage(page: Int): LocalDate =
        firstDate.plusDays(page.coerceIn(0, pageCount - 1).toLong())

    fun pageForDate(date: LocalDate): Int =
        ChronoUnit.DAYS.between(firstDate, date)
            .toInt()
            .coerceIn(0, pageCount - 1)

    fun weekStart(date: LocalDate): LocalDate =
        date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

    fun visibleWeek(date: LocalDate): List<LocalDate> =
        (0L..6L).map { weekStart(date).plusDays(it) }
}
```

## Time bounds

```kotlin
fun LocalDate.toInstantBounds(zoneId: ZoneId): ClosedRange<Instant> {
    val start = atStartOfDay(zoneId).toInstant()
    val endExclusive = plusDays(1).atStartOfDay(zoneId).toInstant()
    return start..endExclusive.minusNanos(1)
}
```

Prefer half-open ranges internally where possible:

```text
[startInclusive, nextDayStartExclusive)
```

## Filter

```kotlin
enum class CalendarListFilter {
    ALL,
    ONLY_ON_LIST,
    HIDE_ON_LIST;

    fun toPreference(): Boolean? = when (this) {
        ALL -> null
        ONLY_ON_LIST -> true
        HIDE_ON_LIST -> false
    }

    companion object {
        fun fromPreference(value: Boolean?): CalendarListFilter = when (value) {
            null -> ALL
            true -> ONLY_ON_LIST
            false -> HIDE_ON_LIST
        }
    }
}
```

## Host state

```kotlin
data class CalendarHostUiState(
    val range: CalendarDateRange,
    val selectedPage: Int = 0,
    val listMode: Boolean = true,
    val filter: CalendarListFilter = CalendarListFilter.ALL,
) {
    val selectedDate get() = range.dateForPage(selectedPage)
    val visibleWeek get() = range.visibleWeek(selectedDate)
}
```

## Swipe pager

```kotlin
@Composable
fun SwipeableCalendarContent(
    state: CalendarHostUiState,
    onPageSelected: (Int) -> Unit,
    pageContent: @Composable (LocalDate) -> Unit,
) {
    val pagerState = rememberPagerState(
        initialPage = state.selectedPage,
        pageCount = { state.range.pageCount },
    )

    LaunchedEffect(state.selectedPage) {
        if (pagerState.currentPage != state.selectedPage) {
            pagerState.animateScrollToPage(state.selectedPage)
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }
            .distinctUntilChanged()
            .collect(onPageSelected)
    }

    HorizontalPager(
        state = pagerState,
        beyondViewportPageCount = 1,
    ) { page ->
        pageContent(state.range.dateForPage(page))
    }
}
```

If current Compose uses another pager API, adapt without upgrading Compose solely for this.

## Header actions

All actions change selected page.

```kotlin
fun selectDate(date: LocalDate) =
    updatePage(range.pageForDate(date))

fun previousWeek() =
    updatePage((selectedPage - 7).coerceAtLeast(0))

fun nextWeek() =
    updatePage((selectedPage + 7).coerceAtMost(range.pageCount - 1))
```

Historical arrows are week arrows. Swipe changes one day.

## ViewModel identity

Selected-day content ViewModel key:

```kotlin
val key = "calendar-$selectedDate"
```

Counts/content receive same filter.

## Performance

- one page visible;
- one adjacent page prefetched;
- avoid 15 permanent heavy ViewModels;
- cache repository results;
- cancel stale requests;
- stable keys.

## Tests

- page 0 = today;
- page 14 = today+14;
- swipe 6→7 changes week;
- reverse 7→6 returns week;
- arrows clamp;
- saved page clamps;
- DST bounds;
- filter consistency.
