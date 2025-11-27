package com.chicken.minerunner.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.chicken.minerunner.R
import com.chicken.minerunner.domain.model.SwipeDirection
import com.chicken.minerunner.presentation.game.GameViewModel
import com.chicken.minerunner.presentation.progress.ProgressViewModel
import com.chicken.minerunner.ui.screens.GameOverScreen
import com.chicken.minerunner.ui.screens.GameScreen
import com.chicken.minerunner.ui.screens.MenuScreen
import com.chicken.minerunner.ui.screens.ShopScreen
import com.chicken.minerunner.ui.screens.SplashScreen
import com.chicken.minerunner.sound.rememberSoundManager

sealed class Destinations(val route: String) {

    object Splash : Destinations("splash")

    object Menu : Destinations("menu")

    object Shop : Destinations("shop")

    object Game : Destinations("game")

    object GameOver : Destinations("game_over?reward={reward}") {
        fun routeWithReward(reward: Int): String {
            return "game_over?reward=$reward"
        }
    }
}

@Composable
fun AppRootNavigation() {
    val navController = rememberNavController()

    val gameViewModel: GameViewModel = hiltViewModel()
    val progressViewModel: ProgressViewModel = hiltViewModel()

    val progressState by progressViewModel.uiState.collectAsStateWithLifecycle()
    val soundManager = rememberSoundManager()

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

                onPrev = progressViewModel::previousItem,
                onNext = progressViewModel::nextItem,
                onPurchase = progressViewModel::purchaseOrUpgrade,

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
                    if (direction == SwipeDirection.Forward) {
                        soundManager.playSfx(
                            R.raw.sfx_jump,
                            progressState.sfxEnabled
                        )
                    }
                    gameViewModel.swipe(direction)
                },
                onPause = gameViewModel::pause,
                onResume = gameViewModel::resume,

                onExit = {
                    navController.popBackStack(Destinations.Menu.route, false)
                },

                musicEnabled = progressState.musicEnabled,
                sfxEnabled = progressState.sfxEnabled,
                soundManager = soundManager,

                // ------ KEY PART ------
                onGameOver = {
                    val reward = state.stats.eggs
                    navController.safeNavigate(
                        Destinations.GameOver.routeWithReward(reward)
                    ) {
                        popUpTo(Destinations.Game.route) { inclusive = true }
                    }
                }
                // ------------------------
            )
        }

        // GAME OVER
        composable(
            route = Destinations.GameOver.route,
            arguments = listOf(
                navArgument("reward") { type = NavType.IntType }
            )
        ) { backStackEntry ->

            val reward = backStackEntry.arguments?.getInt("reward") ?: 0

            GameOverScreen(
                reward = reward,

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


