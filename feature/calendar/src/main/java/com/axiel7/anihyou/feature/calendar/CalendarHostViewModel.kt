package com.axiel7.anihyou.feature.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axiel7.anihyou.core.base.PagedResult
import com.axiel7.anihyou.core.domain.repository.DefaultPreferencesRepository
import com.axiel7.anihyou.core.domain.repository.MediaRepository
import com.axiel7.anihyou.core.network.AiringAnimesQuery
import java.time.LocalDate
import java.time.ZoneId
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CalendarHostViewModel(
    private val defaultPreferencesRepository: DefaultPreferencesRepository,
    private val mediaRepository: MediaRepository,
): ViewModel() {

    val onMyList = defaultPreferencesRepository.calendarOnMyList
    val displayGrid = defaultPreferencesRepository.calendarDisplayGrid
    val displayAdult = defaultPreferencesRepository.displayAdult

    private val mutableDateCounts = MutableStateFlow<Map<LocalDate, Int?>>(emptyMap())
    val dateCounts = mutableDateCounts
    private var countRequest: CountRequest? = null
    private var countJob: Job? = null

    fun onMyListChanged(value: Boolean?) = viewModelScope.launch {
        defaultPreferencesRepository.setCalendarOnMyList(value)
    }

    fun onDisplayGridChanged(value: Boolean) = viewModelScope.launch {
        defaultPreferencesRepository.setCalendarDisplayGrid(value)
    }

    /**
     * Header counts deliberately use the exact same bounded local-day query and filters as the
     * selected-day pager.  Results are cached for the visible Monday–Sunday week only.
     */
    fun loadDateCounts(dates: List<LocalDate>, onMyList: Boolean?, displayAdult: Boolean?) {
        val request = CountRequest(dates, onMyList, displayAdult == true)
        if (request == countRequest) return
        countRequest = request
        countJob?.cancel()
        mutableDateCounts.value = emptyMap()
        countJob = viewModelScope.launch {
            dates.map { date ->
                async {
                    date to countAiringForDate(
                        date = date,
                        onMyList = onMyList,
                        displayAdult = displayAdult == true,
                    )
                }
            }.awaitAll().forEach { (date, count) ->
                mutableDateCounts.value = mutableDateCounts.value + (date to count)
            }
        }
    }

    private suspend fun countAiringForDate(
        date: LocalDate,
        onMyList: Boolean?,
        displayAdult: Boolean,
    ): Int? {
        val zone = ZoneId.systemDefault()
        val start = date.atStartOfDay(zone).toEpochSecond() - 1
        val end = date.plusDays(1).atStartOfDay(zone).toEpochSecond()
        var page = 1
        var total = 0
        var hasNextPage: Boolean
        do {
            val result = mediaRepository.getAiringAnimesPage(
                airingAtGreater = start,
                airingAtLesser = end,
                onMyList = onMyList,
                isAdult = displayAdult,
                page = page,
                perPage = 50,
            ).filterNot { it is PagedResult.Loading }.first()
            val success = result as? PagedResult.Success<AiringAnimesQuery.AiringSchedule> ?: return null
            total += success.list.size
            hasNextPage = success.hasNextPage
            page++
        } while (hasNextPage)
        return total
    }

    private data class CountRequest(
        val dates: List<LocalDate>,
        val onMyList: Boolean?,
        val displayAdult: Boolean,
    )
}
