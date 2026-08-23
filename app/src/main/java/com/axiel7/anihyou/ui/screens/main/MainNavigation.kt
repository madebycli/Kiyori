package com.axiel7.anihyou.ui.screens.main

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.axiel7.anihyou.core.common.utils.ContextUtils.openActionView
import com.axiel7.anihyou.core.model.DeepLink
import com.axiel7.anihyou.core.model.HomeTab
import com.axiel7.anihyou.core.network.type.MediaType
import com.axiel7.anihyou.core.ui.common.LocalMarkdownUriHandler
import com.axiel7.anihyou.core.ui.common.LocalNavActionManager
import com.axiel7.anihyou.core.ui.common.navigation.NavActionManager
import com.axiel7.anihyou.core.ui.common.navigation.Navigator
import com.axiel7.anihyou.core.ui.common.navigation.Route
import com.axiel7.anihyou.core.ui.common.navigation.Routes
import com.axiel7.anihyou.core.ui.composables.FullScreenImageView
import com.axiel7.anihyou.core.ui.composables.markdown.MarkdownUriHandler
import com.axiel7.anihyou.core.ui.composables.markdown.SpoilerSheet
import com.axiel7.anihyou.feature.activitydetails.ActivityDetailsView
import com.axiel7.anihyou.feature.activitydetails.publish.PublishActivityView
import com.axiel7.anihyou.feature.calendar.CalendarView
import com.axiel7.anihyou.feature.characterdetails.CharacterDetailsView
import com.axiel7.anihyou.feature.explore.charts.MediaChartListView
import com.axiel7.anihyou.feature.explore.discover.DiscoverView
import com.axiel7.anihyou.feature.explore.search.SearchView
import com.axiel7.anihyou.feature.explore.season.SeasonAnimeView
import com.axiel7.anihyou.feature.home.HomeView
import com.axiel7.anihyou.feature.home.current.fulllist.CurrentFullListView
import com.axiel7.anihyou.feature.login.LoginView
import com.axiel7.anihyou.feature.mediadetails.MediaDetailsView
import com.axiel7.anihyou.feature.mediadetails.activity.MediaActivityView
import com.axiel7.anihyou.feature.notifications.NotificationsView
import com.axiel7.anihyou.feature.profile.ProfileView
import com.axiel7.anihyou.feature.profile.favorites.reorder.ReorderFavoritesView
import com.axiel7.anihyou.feature.reviewdetails.ReviewDetailsView
import com.axiel7.anihyou.feature.settings.ContributorsView
import com.axiel7.anihyou.feature.settings.MainNavigationSettingsView
import com.axiel7.anihyou.feature.settings.SettingsView
import com.axiel7.anihyou.feature.settings.TranslationsView
import com.axiel7.anihyou.feature.settings.customlists.CustomListsView
import com.axiel7.anihyou.feature.settings.liststyle.ListStyleSettingsView
import com.axiel7.anihyou.feature.staffdetails.StaffDetailsView
import com.axiel7.anihyou.feature.studiodetails.StudioDetailsView
import com.axiel7.anihyou.feature.thread.ThreadDetailsView
import com.axiel7.anihyou.feature.thread.comment.ThreadCommentDetailsView
import com.axiel7.anihyou.feature.thread.publish.PublishCommentView
import com.axiel7.anihyou.feature.usermedialist.UserMediaListHostView

private val topNavigationTransitionSpec = NavDisplay.transitionSpec {
    ContentTransform(fadeIn(animationSpec = tween()), fadeOut(animationSpec = tween()))
} + NavDisplay.popTransitionSpec {
    ContentTransform(fadeIn(animationSpec = tween()), fadeOut(animationSpec = tween()))
} + NavDisplay.predictivePopTransitionSpec {
    ContentTransform(
        fadeIn(spring(dampingRatio = 1f, stiffness = 1600f)),
        fadeOut(spring(dampingRatio = 1f, stiffness = 1600f)),
    )
}

@Composable
fun MainNavigation(
    navigator: Navigator,
    navActionManager: NavActionManager,
    isCompactScreen: Boolean,
    isLoggedIn: Boolean,
    homeTab: HomeTab,
    deepLink: DeepLink?,
    onDeepLinkHandled: (DeepLink) -> Unit,
    padding: PaddingValues = PaddingValues(),
) {
    val context = LocalContext.current
    val bottomPadding by animateDpAsState(
        targetValue = padding.calculateBottomPadding(),
        label = "bottom_bar_padding",
    )
    val mainDestinationModifier: (Boolean) -> Modifier = { main ->
        if (isCompactScreen && main) Modifier.padding(bottom = bottomPadding) else Modifier
    }

    var spoilerText by remember { mutableStateOf<String?>(null) }
    val markdownUriHandler = remember {
        MarkdownUriHandler(
            onSpoilerClicked = { spoilerText = it },
            onLinkClicked = { context.openActionView(it) },
        )
    }

    spoilerText?.let {
        SpoilerSheet(text = it, uriHandler = markdownUriHandler, onDismiss = { spoilerText = null })
    }

    LaunchedEffect(deepLink) {
        deepLink?.let { link ->
            when (link.type) {
                DeepLink.Type.ANIME, DeepLink.Type.MANGA -> link.id.toIntOrNull()?.let(navActionManager::toMediaDetails)
                DeepLink.Type.USER -> navActionManager.toUserDetails(link.id.toIntOrNull(), link.id)
                DeepLink.Type.SEARCH -> navActionManager.toSearch()
                DeepLink.Type.CHARACTER -> link.id.toIntOrNull()?.let(navActionManager::toCharacterDetails)
                DeepLink.Type.STAFF -> link.id.toIntOrNull()?.let(navActionManager::toStaffDetails)
                DeepLink.Type.STUDIO -> link.id.toIntOrNull()?.let(navActionManager::toStudioDetails)
                DeepLink.Type.THREAD -> link.id.toIntOrNull()?.let(navActionManager::toThreadDetails)
                DeepLink.Type.ACTIVITY -> link.id.toIntOrNull()?.let(navActionManager::toActivityDetails)
            }
            onDeepLinkHandled(link)
        }
    }

    val entryProvider = entryProvider<NavKey> {
        entry<Route.Home>(metadata = topNavigationTransitionSpec) {
            HomeView(
                isLoggedIn = isLoggedIn,
                defaultHomeTab = homeTab,
                modifier = if (isCompactScreen) Modifier.padding(bottom = bottomPadding) else Modifier,
            )
        }
        entry<Route.AnimeTab>(metadata = topNavigationTransitionSpec) {
            if (isLoggedIn) {
                UserMediaListHostView(
                    arguments = Route.UserMediaList(mediaType = MediaType.ANIME.rawValue),
                    isCompactScreen = isCompactScreen,
                    modifier = Modifier.padding(bottom = bottomPadding),
                )
            } else LoginView()
        }
        entry<Route.MangaTab>(metadata = topNavigationTransitionSpec) {
            if (isLoggedIn) {
                UserMediaListHostView(
                    arguments = Route.UserMediaList(mediaType = MediaType.MANGA.rawValue),
                    isCompactScreen = isCompactScreen,
                    modifier = Modifier.padding(bottom = bottomPadding),
                )
            } else LoginView()
        }
        entry<Route.Profile>(metadata = topNavigationTransitionSpec) {
            if (isLoggedIn) {
                ProfileView(
                    arguments = Route.UserDetails(null, null),
                    modifier = if (isCompactScreen) Modifier.padding(bottom = bottomPadding) else Modifier,
                )
            } else {
                LoginView(showSettingsButton = true, navigateToSettings = navActionManager::toSettings)
            }
        }
        entry<Route.Explore>(metadata = topNavigationTransitionSpec) {
            DiscoverView(
                isLoggedIn = isLoggedIn,
                contentPadding = if (isCompactScreen) PaddingValues(bottom = bottomPadding) else PaddingValues(),
            )
        }

        entry<Route.UserDetails> { ProfileView(arguments = it) }
        entry<Route.UserMediaList> {
            UserMediaListHostView(
                arguments = it,
                isCompactScreen = isCompactScreen,
                modifier = Modifier.padding(bottom = bottomPadding),
            )
        }
        entry<Route.Search> {
            SearchView(arguments = it, isLoggedIn = isLoggedIn, modifier = Modifier.padding(bottom = bottomPadding))
        }
        entry<Route.Notifications> { if (isLoggedIn) NotificationsView(arguments = it) else LoginView() }

        // Media details is still a Kiyori-merged screen and is adapted until its upstream merge below is complete.
        entry<Route.MediaDetails> {
            MediaDetailsView(
                arguments = Routes.MediaDetails(it.id, isLoggedIn),
                navActionManager = navActionManager,
            )
        }

        entry<Route.MediaChartList> {
            MediaChartListView(
                arguments = Routes.MediaChartList(it.type, it.isMainDestination),
                isLoggedIn = isLoggedIn,
                navActionManager = navActionManager,
                modifier = mainDestinationModifier(it.isMainDestination),
            )
        }
        entry<Route.MediaChartListMain>(metadata = topNavigationTransitionSpec) {
            MediaChartListView(
                arguments = Routes.MediaChartList(it.type, true),
                isLoggedIn = isLoggedIn,
                navActionManager = navActionManager,
                modifier = mainDestinationModifier(true),
            )
        }

        entry<Route.SeasonAnime> {
            SeasonAnimeView(
                isLoggedIn = isLoggedIn,
                arguments = Routes.SeasonAnime(it.season, it.year, it.isMainDestination),
                navActionManager = navActionManager,
                modifier = mainDestinationModifier(it.isMainDestination),
            )
        }
        entry<Route.SeasonAnimeMain>(metadata = topNavigationTransitionSpec) {
            SeasonAnimeView(
                isLoggedIn = isLoggedIn,
                arguments = Routes.SeasonAnime(it.season, it.year, true),
                navActionManager = navActionManager,
                modifier = mainDestinationModifier(true),
            )
        }

        entry<Route.Calendar> { CalendarView(isLoggedIn = isLoggedIn, navActionManager = navActionManager) }
        entry<Route.CalendarMain>(metadata = topNavigationTransitionSpec) {
            CalendarView(
                isLoggedIn = isLoggedIn,
                navActionManager = navActionManager,
                isMainDestination = true,
                contentPadding = if (isCompactScreen) PaddingValues(bottom = bottomPadding) else PaddingValues(),
            )
        }

        entry<Route.CharacterDetails> { CharacterDetailsView(isLoggedIn = isLoggedIn, arguments = it) }
        entry<Route.StaffDetails> { StaffDetailsView(isLoggedIn = isLoggedIn, arguments = it) }
        entry<Route.ReviewDetails> { ReviewDetailsView(arguments = it) }
        entry<Route.ThreadDetails> { ThreadDetailsView(arguments = it) }
        entry<Route.ThreadCommentDetails> { ThreadCommentDetailsView(arguments = it) }
        entry<Route.StudioDetails> { StudioDetailsView(arguments = it) }

        entry<Route.Settings> { SettingsView(navActionManager = navActionManager) }
        entry<Route.MainNavigationSettings> { MainNavigationSettingsView(navActionManager = navActionManager) }
        entry<Route.ListStyleSettings> { ListStyleSettingsView(navActionManager = navActionManager) }
        entry<Route.CustomLists> { CustomListsView(navActionManager = navActionManager) }
        entry<Route.Translations> { TranslationsView(navActionManager = navActionManager) }
        entry<Route.Contributors> { ContributorsView(navActionManager = navActionManager) }

        entry<Route.FullScreenImage> {
            FullScreenImageView(arguments = it, isCompactScreen = isCompactScreen, onDismiss = navActionManager::goBack)
        }
        entry<Route.ActivityDetails> { ActivityDetailsView(arguments = it) }
        entry<Route.PublishActivity> { if (isLoggedIn) PublishActivityView(arguments = it) else LoginView() }
        entry<Route.PublishComment> { if (isLoggedIn) PublishCommentView(arguments = it) else LoginView() }
        entry<Route.MediaActivity> {
            MediaActivityView(
                arguments = Routes.MediaActivity(it.mediaId),
                uriHandler = markdownUriHandler,
                navActionManager = navActionManager,
            )
        }

        entry<Route.CurrentFullList> {
            CurrentFullListView(
                isLoggedIn = isLoggedIn,
                listType = it.listType,
                navActionManager = navActionManager,
                isMainDestination = it.isMainDestination,
                modifier = mainDestinationModifier(it.isMainDestination),
            )
        }
        entry<Route.CurrentFullListMain>(metadata = topNavigationTransitionSpec) {
            CurrentFullListView(
                isLoggedIn = isLoggedIn,
                listType = it.listType,
                navActionManager = navActionManager,
                isMainDestination = true,
                modifier = mainDestinationModifier(true),
            )
        }
        entry<Route.ReorderFavorites> { ReorderFavoritesView(arguments = it) }
    }

    CompositionLocalProvider(
        LocalNavActionManager provides navActionManager,
        LocalMarkdownUriHandler provides markdownUriHandler,
    ) {
        NavDisplay(
            entries = navigator.state.toDecoratedEntries(entryProvider),
            modifier = Modifier.padding(
                start = padding.calculateStartPadding(LocalLayoutDirection.current),
                top = padding.calculateTopPadding(),
                end = padding.calculateEndPadding(LocalLayoutDirection.current),
            ),
            transitionSpec = {
                slideInHorizontally(initialOffsetX = { it }) togetherWith
                    (slideOutHorizontally(targetOffsetX = { -it }) + fadeOut(animationSpec = tween()))
            },
            popTransitionSpec = {
                (slideInHorizontally(initialOffsetX = { -it }) + fadeIn()) togetherWith
                    slideOutHorizontally(targetOffsetX = { it })
            },
            predictivePopTransitionSpec = {
                (slideInHorizontally(initialOffsetX = { -it }) + fadeIn(animationSpec = tween())) togetherWith
                    slideOutHorizontally(targetOffsetX = { it })
            },
            onBack = navigator::goBack,
        )
    }
}
