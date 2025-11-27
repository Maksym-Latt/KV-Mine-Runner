package com.chicken.minerunner.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chicken.minerunner.presentation.game.GameViewModel
import com.chicken.minerunner.ui.screens.GameOverScreen
import com.chicken.minerunner.ui.screens.GameScreen
import com.chicken.minerunner.ui.screens.MenuScreen
import com.chicken.minerunner.ui.screens.ShopScreen
import com.chicken.minerunner.ui.screens.SplashScreen

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

    NavHost(navController = navController, startDestination = Destinations.Splash.route) {
        composable(Destinations.Splash.route) {
            SplashScreen {
                navController.navigate(Destinations.Menu.route) {
                    popUpTo(Destinations.Splash.route) { inclusive = true }
                }
            }
        }

        composable(Destinations.Menu.route) {
            MenuScreen(
                onStart = {
                    gameViewModel.startGame()
                    navController.navigate(Destinations.Game.route)
                },
                onShop = { navController.navigate(Destinations.Shop.route) },
                eggs = 0,
                onMenuClick = {}
            )
        }

        composable(Destinations.Shop.route) {
            ShopScreen(
                onBack = { navController.popBackStack() },
            )
        }

        composable(Destinations.Game.route) {
            val state by gameViewModel.uiState.collectAsStateWithLifecycle()
            GameScreen(
                state = state,
                onSwipe = gameViewModel::swipe,
                onPause = gameViewModel::pauseGame,
                onResume = gameViewModel::resumeGame,
                onExit = {
                    navController.popBackStack()
                },
                onGameOver = {
                    navController.navigate(Destinations.GameOver.route) {
                        popUpTo(Destinations.Game.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Destinations.GameOver.route) {
            GameOverScreen(
                onRetry = {
                    gameViewModel.startGame()
                    navController.navigate(Destinations.Game.route) {
                        popUpTo(Destinations.Menu.route)
                    }
                },
                onLobby = { navController.navigate(Destinations.Menu.route) }
            )
        }
    }
}
