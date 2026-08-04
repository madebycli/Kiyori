package com.axiel7.anihyou.feature.calendar

import java.time.LocalDate
import java.time.ZoneId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CalendarDateRangeTest {
    private val range = CalendarDateRange(LocalDate.of(2026, 3, 8))

    @Test
    fun `range is today through today plus fourteen inclusive`() {
        assertEquals(15, range.pageCount)
        assertEquals(LocalDate.of(2026, 3, 8), range.dateForPage(0))
        assertEquals(LocalDate.of(2026, 3, 22), range.dateForPage(14))
        assertEquals(0, range.pageForDate(LocalDate.of(2026, 1, 1)))
        assertEquals(14, range.pageForDate(LocalDate.of(2026, 12, 1)))
    }

    @Test
    fun `weeks always start on monday`() {
        val week = range.visibleWeek(LocalDate.of(2026, 3, 8))
        assertEquals(LocalDate.of(2026, 3, 2), week.first())
        assertEquals(LocalDate.of(2026, 3, 8), week.last())
    }

    @Test
    fun `range exposes safe week-control boundaries`() {
        assertTrue(range.contains(range.firstDate))
        assertTrue(range.contains(range.lastDate))
        assertTrue(!range.contains(range.firstDate.minusDays(1)))
        assertTrue(!range.canMoveToPreviousWeek(range.firstDate))
        assertTrue(range.canMoveToNextWeek(range.firstDate))
        assertTrue(range.canMoveToPreviousWeek(range.lastDate))
        assertTrue(!range.canMoveToNextWeek(range.lastDate))
    }

    @Test
    fun `local date bounds survive DST`() {
        val (start, end) = range.bounds(LocalDate.of(2026, 3, 29), ZoneId.of("Europe/Berlin"))
        assertTrue(end.isAfter(start))
        assertEquals(23 * 60 * 60, end.epochSecond - start.epochSecond)
    }
}
