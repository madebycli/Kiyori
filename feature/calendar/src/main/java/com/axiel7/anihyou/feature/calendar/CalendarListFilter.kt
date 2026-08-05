package com.axiel7.anihyou.feature.calendar

/**
 * APK-recovered tri-state list filter. The repository API still represents the
 * filter as nullable Boolean, while the UI uses explicit, readable states.
 */
enum class CalendarListFilter(
    val onMyList: Boolean?,
) {
    ALL(null),
    ON_MY_LIST(true),
    NOT_ON_MY_LIST(false),
    ;

    companion object {
        fun from(value: Boolean?): CalendarListFilter = entries.first { it.onMyList == value }
    }
}
