package com.axiel7.anihyou.feature.calendar

import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters

/**
 * The finite, local-date calendar window used by the top-level Calendar tab.
 * All selections are page indices so saved state stays bounded across midnight
 * and daylight-saving transitions.
 */
data class CalendarDateRange(val today: LocalDate) {
    val firstDate: LocalDate = today
    val lastDate: LocalDate = today.plusDays(LAST_PAGE.toLong())
    val pageCount: Int = LAST_PAGE + 1

    fun dateForPage(page: Int): LocalDate = firstDate.plusDays(page.coerceIn(0, LAST_PAGE).toLong())

    fun pageForDate(date: LocalDate): Int = ChronoUnit.DAYS.between(firstDate, date)
        .toInt()
        .coerceIn(0, LAST_PAGE)

    fun visibleWeek(date: LocalDate): List<LocalDate> {
        val monday = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        return (0L..6L).map(monday::plusDays)
    }

    /** A half-open instant interval; this is correct for local DST gaps and overlaps. */
    fun bounds(date: LocalDate, zoneId: ZoneId): Pair<Instant, Instant> =
        date.atStartOfDay(zoneId).toInstant() to date.plusDays(1).atStartOfDay(zoneId).toInstant()

    companion object {
        const val LAST_PAGE = 14
    }
}
