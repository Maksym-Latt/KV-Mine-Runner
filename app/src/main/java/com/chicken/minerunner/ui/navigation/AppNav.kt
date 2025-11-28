package com.chicken.minerunner.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chicken.minerunner.R
import com.chicken.minerunner.domain.model.SwipeDirection
import com.chicken.minerunner.presentation.game.GameViewModel
import com.chicken.minerunner.presentation.progress.ProgressViewModel
import com.chicken.minerunner.sound.SoundManager
import com.chicken.minerunner.ui.screens.GameScreen
import com.chicken.minerunner.ui.screens.MenuScreen
import com.chicken.minerunner.ui.screens.ShopScreen
import com.chicken.minerunner.ui.screens.SplashScreen

sealed class Destinations(val route: String) {

    object Splash : Destinations("splash")

    object Menu : Destinations("menu")

    object Shop : Destinations("shop")

    object Game : Destinations("game")
}

@Composable
fun AppRootNavigation(soundManager: SoundManager) {
    val navController = rememberNavController()

    val gameViewModel: GameViewModel = hiltViewModel()
    val progressViewModel: ProgressViewModel = hiltViewModel()

    val progressState by progressViewModel.uiState.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = Destinations.Splash.route
    ) {

        // SPLASH
        composable(Destinations.Splash.route) {
            SplashScreen {
                navController.safeNavigate(Destinations.Menu.route) {
                    popUpTo(Destinations.Splash.route) { inclusive = true }
                }
            }
        }

        // MENU
        composable(Destinations.Menu.route) {
            MenuScreen(
                onStart = {
                    gameViewModel.start()
                    navController.safeNavigate(Destinations.Game.route)
                },
                onShop = { navController.safeNavigate(Destinations.Shop.route) },

                eggs = progressState.eggs,

                onMenuClick = { progressViewModel.toggleSettings(true) },
                settingsVisible = progressState.settingsVisible,

                musicEnabled = progressState.musicEnabled,
                sfxEnabled = progressState.sfxEnabled,

                onMusicToggle = progressViewModel::setMusicEnabled,
                onSfxToggle = progressViewModel::setSfxEnabled,
                onCloseSettings = { progressViewModel.toggleSettings(false) }
            )

            LaunchedEffect(progressState.musicEnabled) {
                soundManager.playMenuMusic(progressState.musicEnabled)
            }
        }

        // SHOP
        composable(Destinations.Shop.route) {
            ShopScreen(
                eggs = progressState.eggs,
                currentIndex = progressState.currentItemIndex,
                items = progressState.shopItems,
                message = progressState.message,

                onPrev = progressViewModel::previousItem,
                onNext = progressViewModel::nextItem,
                onPurchase = progressViewModel::purchaseOrUpgrade,
                onDismissMessage = progressViewModel::dismissMessage,

                onBack = {
                    navController.popBackStack(Destinations.Menu.route, false)
                }
            )
        }

        // GAME
        composable(Destinations.Game.route) {
            val state by gameViewModel.ui.collectAsStateWithLifecycle()

            GameScreen(
                state = state,
                onSwipe = { direction ->
                    gameViewModel.swipe(direction)
                },
                onPause = gameViewModel::pause,
                onResume = gameViewModel::resume,

                onExit = {
                    navController.popBackStack(Destinations.Menu.route, false)
                },

                onRetry = {
                    gameViewModel.start()
                    gameViewModel.begin()
                },

                musicEnabled = progressState.musicEnabled,
                sfxEnabled = progressState.sfxEnabled,
                soundManager = soundManager,
            )
        }
    }
}

fun NavController.safeNavigate(
    route: String,
    builder: NavOptionsBuilder.() -> Unit = {}
) {
    val currentRoute = currentDestination?.route
    if (currentRoute == route) return

    navigate(route) {
        launchSingleTop = true
        builder()
    }
}


