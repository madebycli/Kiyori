package com.axiel7.anihyou.core.ui.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.NavKey
import com.axiel7.anihyou.core.resources.R
import com.axiel7.anihyou.core.ui.common.navigation.Route

sealed class BottomDestination(
    val index: Int,
    val route: NavKey,
    @param:StringRes val title: Int,
    @param:DrawableRes val icon: Int,
    @param:DrawableRes val iconSelected: Int,
) {
    data object Home : BottomDestination(0, Route.Home, R.string.home, R.drawable.home_24, R.drawable.home_filled_24)
    data object AnimeList : BottomDestination(1, Route.AnimeTab, R.string.anime, R.drawable.live_tv_24, R.drawable.live_tv_filled_24)
    data object MangaList : BottomDestination(2, Route.MangaTab, R.string.manga, R.drawable.book_24, R.drawable.book_filled_24)
    data object Profile : BottomDestination(3, Route.Profile, R.string.profile, R.drawable.person_24, R.drawable.person_filled_24)
    data object Explore : BottomDestination(4, Route.Explore, R.string.explore, R.drawable.explore_24, R.drawable.explore_filled_24)
    data object Calendar : BottomDestination(5, Route.CalendarMain, R.string.calendar, R.drawable.calendar_month_24, R.drawable.calendar_month_24)

    class Shortcut(
        val stableId: String,
        index: Int,
        route: NavKey,
        @StringRes title: Int,
        @DrawableRes icon: Int,
    ) : BottomDestination(index, route, title, icon, icon)

    @Composable
    fun Icon(selected: Boolean) {
        androidx.compose.material3.Icon(
            painter = painterResource(if (selected) iconSelected else icon),
            contentDescription = stringResource(title),
        )
    }

    companion object {
        val values: List<BottomDestination>
            get() = listOf(Home, AnimeList, MangaList, Profile, Explore, Calendar)
        val routes: Set<NavKey>
            get() = values.mapTo(linkedSetOf()) { it.route }
        val railValues: List<BottomDestination>
            get() = listOf(Home, AnimeList, MangaList, Profile)
        fun Int.toBottomDestinationRoute(): NavKey? = values.find { it.index == this }?.route
        fun NavKey.isBottomDestination() = values.any { it.route == this }
        val BottomDestination.testTag
            get() = when (this) {
                is Home -> "HomeTab"
                is AnimeList -> "AnimeListTab"
                is MangaList -> "MangaListTab"
                is Profile -> "ProfileTab"
                is Explore -> "ExploreTab"
                is Calendar -> "CalendarTab"
                is Shortcut -> stableId
            }
    }
}
