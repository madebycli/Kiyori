package com.axiel7.anihyou.feature.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axiel7.anihyou.core.domain.repository.DefaultPreferencesRepository
import kotlinx.coroutines.launch

class CalendarHostViewModel(
    private val defaultPreferencesRepository: DefaultPreferencesRepository
): ViewModel() {

    val onMyList = defaultPreferencesRepository.calendarOnMyList
    val displayGrid = defaultPreferencesRepository.calendarDisplayGrid

    fun onMyListChanged(value: Boolean?) = viewModelScope.launch {
        defaultPreferencesRepository.setCalendarOnMyList(value)
    }

    fun onDisplayGridChanged(value: Boolean) = viewModelScope.launch {
        defaultPreferencesRepository.setCalendarDisplayGrid(value)
    }
}
