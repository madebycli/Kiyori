package com.axiel7.anihyou.ui.screens.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.ReportDrawn
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.axiel7.anihyou.core.base.extensions.firstBlocking
import com.axiel7.anihyou.core.model.DeepLink
import com.axiel7.anihyou.core.model.HomeTab
import com.axiel7.anihyou.core.model.Theme
import com.axiel7.anihyou.core.model.navigation.MainNavigationConfig
import com.axiel7.anihyou.core.resources.dark_scrim
import com.axiel7.anihyou.core.resources.light_scrim
import com.axiel7.anihyou.core.ui.common.BottomDestination
import com.axiel7.anihyou.core.ui.common.LocalBlurAdult
import com.axiel7.anihyou.core.ui.common.LocalHideScores
import com.axiel7.anihyou.core.ui.common.LocalScoreFormat
import com.axiel7.anihyou.core.ui.common.navigation.NavActionManager
import com.axiel7.anihyou.core.ui.common.navigation.Navigator
import com.axiel7.anihyou.core.ui.common.navigation.rememberNavigationState
import com.axiel7.anihyou.core.ui.theme.AniHyouTheme
import com.axiel7.anihyou.ui.screens.main.composables.MainBottomNavBar
import com.axiel7.anihyou.ui.screens.main.composables.MainNavigationRail
import org.koin.androidx.viewmodel.ext.android.viewModel

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModel()

    private val appLockProcessObserver = object : DefaultLifecycleObserver {
        override fun onStart(owner: LifecycleOwner) {
            viewModel.onProcessForegrounded()
        }

        override fun onStop(owner: LifecycleOwner) {
            viewModel.onProcessBackgrounded()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        installSplashScreen()
        super.onCreate(savedInstanceState)

        // Get necessary preferences while on the splashscreen.
        viewModel.setToken(viewModel.accessToken.firstBlocking())
        val initialIsLoggedIn = viewModel.isLoggedIn.firstBlocking()
        val initialTheme = viewModel.theme.firstBlocking()
        val initialUseBlackColors = viewModel.useBlackColors.firstBlocking()
        val initialAppColor = viewModel.appColor.firstBlocking()
        val initialAppColorMode = viewModel.appColorMode.firstBlocking()
        val initialPaletteStyle = viewModel.paletteStyle.firstBlocking()
        val initialBlurAdult = viewModel.blurAdultContent.firstBlocking()
        val initialScoreFormat = viewModel.scoreFormat.firstBlocking()
        val initialHideScores = viewModel.hideScores.firstBlocking()
        val homeTab = viewModel.homeTab.firstBlocking() ?: HomeTab.CURRENT
        val initialNavigationConfig = viewModel.mainNavigationConfig.firstBlocking()
        val initialAppLockPreferences = viewModel.appLockPreferences.firstBlocking()
        viewModel.initializeAppLock(initialAppLockPreferences)
        viewModel.queueDeepLink(findDeepLink(intent))
        ProcessLifecycleOwner.get().lifecycle.addObserver(appLockProcessObserver)

        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
            val theme by viewModel.theme.collectAsStateWithLifecycle(initialTheme)
            val isDark = if (theme == Theme.FOLLOW_SYSTEM) isSystemInDarkTheme()
            else theme == Theme.DARK
            val useBlackColors by viewModel.useBlackColors.collectAsStateWithLifecycle(
                initialValue = initialUseBlackColors
            )
            val appColor by viewModel.appColor.collectAsStateWithLifecycle(initialAppColor)
            val appColorMode by viewModel.appColorMode.collectAsStateWithLifecycle(
                initialValue = initialAppColorMode
            )
            val paletteStyle by viewModel.paletteStyle.collectAsStateWithLifecycle(initialPaletteStyle)
            val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle(initialIsLoggedIn)
            val blurAdultContent by viewModel.blurAdultContent.collectAsStateWithLifecycle(initialBlurAdult)
            val scoreFormat by viewModel.scoreFormat.collectAsStateWithLifecycle(initialScoreFormat)
            val hideScores by viewModel.hideScores.collectAsStateWithLifecycle(initialHideScores)
            val navigationConfig by viewModel.mainNavigationConfig.collectAsStateWithLifecycle(
                initialNavigationConfig
            )
            val appLockState by viewModel.appLockState.collectAsStateWithLifecycle()
            val pendingDeepLink by viewModel.pendingDeepLink.collectAsStateWithLifecycle()

            DisposableEffect(isDark) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        android.graphics.Color.TRANSPARENT,
                        android.graphics.Color.TRANSPARENT,
                    ) { isDark },
                    navigationBarStyle = SystemBarStyle.auto(
                        light_scrim.toArgb(),
                        dark_scrim.toArgb(),
                    ) { isDark },
                )
                onDispose {}
            }

            AniHyouTheme(
                darkTheme = isDark,
                blackColors = useBlackColors,
                appColor = appColor,
                appColorMode = appColorMode,
                paletteStyle = paletteStyle,
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    AppLockGate(
                        activity = this,
                        state = appLockState,
                        onAuthenticationSucceeded = viewModel::onAppLockAuthenticationSucceeded,
                    ) {
                        CompositionLocalProvider(
                            LocalBlurAdult provides blurAdultContent,
                            LocalScoreFormat provides scoreFormat,
                            LocalHideScores provides hideScores,
                        ) {
                            MainView(
                                windowSizeClass = windowSizeClass,
                                isLoggedIn = isLoggedIn,
                                navigationConfig = navigationConfig,
                                event = viewModel,
                                homeTab = homeTab,
                                deepLink = pendingDeepLink,
                                onDeepLinkHandled = viewModel::consumeDeepLink,
                                setNavigationBarContrastEnforced = {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                        window.isNavigationBarContrastEnforced = it
                                    }
                                },
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        ProcessLifecycleOwner.get().lifecycle.removeObserver(appLockProcessObserver)
        super.onDestroy()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        viewModel.queueDeepLink(findDeepLink(intent))
    }

    private fun findDeepLink(sourceIntent: Intent): DeepLink? {
        return when {
            // Widget and notification media intent.
            sourceIntent.action == "media_details" -> {
                DeepLink(
                    type = DeepLink.Type.ANIME,
                    id = sourceIntent.getIntExtra("media_id", 0).toString(),
                )
            }
            // Search shortcut.
            sourceIntent.action == "search" -> {
                DeepLink(
                    type = DeepLink.Type.SEARCH,
                    id = "search",
                )
            }
            // Login intent or AniList link.
            sourceIntent.data != null -> {
                viewModel.onIntentDataReceived(sourceIntent.data)
                val anilistSchemeIndex = sourceIntent.dataString?.indexOf("anilist.co")
                if (anilistSchemeIndex != null && anilistSchemeIndex != -1) {
                    val linkSplit = sourceIntent.dataString!!.substring(anilistSchemeIndex).split('/')
                    val type = linkSplit.getOrNull(1)
                        ?.uppercase()
                        ?.let { runCatching { DeepLink.Type.valueOf(it) }.getOrNull() }
                    val id = linkSplit.getOrNull(2)
                    if (type != null && !id.isNullOrBlank()) DeepLink(type = type, id = id) else null
                } else null
            }

            else -> null
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MainView(
    windowSizeClass: WindowSizeClass,
    isLoggedIn: Boolean,
    navigationConfig: MainNavigationConfig,
    event: MainEvent?,
    homeTab: HomeTab,
    deepLink: DeepLink?,
    onDeepLinkHandled: (DeepLink) -> Unit,
    setNavigationBarContrastEnforced: (Boolean) -> Unit,
) {
    val resolvedDestinations = remember(navigationConfig) {
        MainNavigationResolver.destinations(navigationConfig)
    }
    val resolvedRoutes = remember(resolvedDestinations) {
        MainNavigationResolver.routes(navigationConfig)
    }
    val allTopLevelRoutes = remember {
        MainNavigationResolver.allRoutes()
    }
    val navigationState = rememberNavigationState(
        startRoute = BottomDestination.Home.route,
        topLevelRoutes = allTopLevelRoutes,
    )
    val navigator = remember(navigationState) { Navigator(navigationState) }
    val currentTopLevelRoute = navigationState.topLevelRoute
    val currentRoute = navigationState.getCurrentRoute()
    val isBottomDestination = currentTopLevelRoute in resolvedRoutes &&
        currentRoute == currentTopLevelRoute
    val navActionManager = NavActionManager.rememberNavActionManager(navigator)
    val isCompactScreen = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    LaunchedEffect(isBottomDestination) {
        setNavigationBarContrastEnforced(!isBottomDestination)
    }

    LaunchedEffect(resolvedRoutes, currentTopLevelRoute) {
        if (currentTopLevelRoute !in resolvedRoutes) {
            navigator.navigate(BottomDestination.Home.route)
        }
    }

    Scaffold(
        bottomBar = {
            if (isCompactScreen && isBottomDestination) {
                MainBottomNavBar(
                    navigator = navigator,
                    navActionManager = navActionManager,
                    destinations = resolvedDestinations,
                    selectedRoute = currentTopLevelRoute,
                    isVisible = true,
                    onItemSelected = { event?.saveLastTab(it) },
                )
            }
        },
        contentWindowInsets = if (isCompactScreen) WindowInsets.systemBars
            .only(WindowInsetsSides.Horizontal)
        else WindowInsets(0, 0, 0, 0),
    ) { padding ->
        if (isCompactScreen) {
            MainNavigation(
                navigator = navigator,
                navActionManager = navActionManager,
                isCompactScreen = true,
                isLoggedIn = isLoggedIn,
                deepLink = deepLink,
                onDeepLinkHandled = onDeepLinkHandled,
                homeTab = homeTab,
                padding = padding,
            )
        } else {
            Row(
                modifier = Modifier.padding(padding),
            ) {
                if (isBottomDestination) {
                    MainNavigationRail(
                        navigator = navigator,
                        destinations = resolvedDestinations,
                        selectedRoute = currentTopLevelRoute,
                        onItemSelected = { event?.saveLastTab(it) },
                    )
                }
                MainNavigation(
                    navigator = navigator,
                    navActionManager = navActionManager,
                    isCompactScreen = false,
                    isLoggedIn = isLoggedIn,
                    deepLink = deepLink,
                    onDeepLinkHandled = onDeepLinkHandled,
                    homeTab = homeTab,
                )
            }
        }
        ReportDrawn()
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true)
@Composable
private fun MainPreview() {
    AniHyouTheme {
        MainView(
            windowSizeClass = WindowSizeClass.calculateFromSize(
                DpSize(width = 1280.dp, height = 1920.dp),
            ),
            isLoggedIn = false,
            navigationConfig = com.axiel7.anihyou.core.model.navigation.defaultMainNavigationConfig(),
            event = null,
            homeTab = HomeTab.CURRENT,
            deepLink = null,
            onDeepLinkHandled = {},
            setNavigationBarContrastEnforced = {},
        )
    }
}
