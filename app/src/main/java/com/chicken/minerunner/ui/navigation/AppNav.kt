package com.chicken.minerunner.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chicken.minerunner.domain.model.SwipeDirection
import com.chicken.minerunner.presentation.game.GameViewModel
import com.chicken.minerunner.presentation.progress.ProgressViewModel
import com.chicken.minerunner.ui.screens.GameOverScreen
import com.chicken.minerunner.ui.screens.GameScreen
import com.chicken.minerunner.ui.screens.MenuScreen
import com.chicken.minerunner.ui.screens.ShopScreen
import com.chicken.minerunner.ui.screens.SplashScreen
import com.chicken.minerunner.ui.sound.rememberSoundManager

sealed class Destinations(val route: String) {
    data object Splash : Destinations("splash")
    data object Menu : Destinations("menu")
    data object Shop : Destinations("shop")
    data object Game : Destinations("game")
    data object GameOver : Destinations("game_over")
}

@Composable
fun AppRootNavigation() {
    val navController = rememberNavController()
    val gameViewModel: GameViewModel = hiltViewModel()
    val progressViewModel: ProgressViewModel = hiltViewModel()
    val progressState by progressViewModel.uiState.collectAsStateWithLifecycle()
    val soundManager = rememberSoundManager()

    NavHost(navController = navController, startDestination = Destinations.Splash.route) {

        composable(Destinations.Splash.route) {
            SplashScreen {
                navController.safeNavigate(Destinations.Menu.route) {
                    popUpTo(Destinations.Splash.route) { inclusive = true }
                }
            }
        }

        composable(Destinations.Menu.route) {
            MenuScreen(
                onStart = {
                    gameViewModel.start()
                    navController.safeNavigate(Destinations.Game.route)
                },
                onShop = {
                    navController.safeNavigate(Destinations.Shop.route)
                },
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

        composable(Destinations.Shop.route) {
            ShopScreen(
                eggs = progressState.eggs,
                currentIndex = progressState.currentItemIndex,
                items = progressState.shopItems,
                onPrev = progressViewModel::previousItem,
                onNext = progressViewModel::nextItem,
                onPurchase = progressViewModel::purchaseOrUpgrade,
                onBack = {
                    navController.popBackStack(
                        Destinations.Menu.route,
                        inclusive = false
                    )
                },
            )
        }

        composable(Destinations.Game.route) {
            val state by gameViewModel.ui.collectAsStateWithLifecycle()

            GameScreen(
                state = state,
                onSwipe = { direction ->
                    if (direction == SwipeDirection.Forward) {
                        soundManager.playSfx(
                            com.chicken.minerunner.R.raw.sfx_jump,
                            progressState.sfxEnabled
                        )
                    }
                    gameViewModel.swipe(direction)
                },
                onPause = gameViewModel::pause,
                onResume = gameViewModel::resume,
                onExit = {
                    navController.popBackStack(
                        Destinations.Menu.route,
                        inclusive = false
                    )
                },
                musicEnabled = progressState.musicEnabled,
                sfxEnabled = progressState.sfxEnabled,
                soundManager = soundManager,
                onGameOver = {
                    navController.safeNavigate(Destinations.GameOver.route) {
                        popUpTo(Destinations.Game.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Destinations.GameOver.route) {
            GameOverScreen(
                onRetry = {
                    gameViewModel.start()
                    navController.safeNavigate(Destinations.Game.route) {
                        popUpTo(Destinations.Menu.route) { inclusive = false }
                    }
                },
                onLobby = {
                    navController.safeNavigate(Destinations.Menu.route) {
                        popUpTo(Destinations.Menu.route) { inclusive = false }
                    }
                }
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


