package com.chicken.minerunner.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chicken.minerunner.presentation.screen.GameOverScreen
import com.chicken.minerunner.presentation.screen.GameScreen
import com.chicken.minerunner.presentation.screen.MainMenuScreen
import com.chicken.minerunner.presentation.screen.PauseScreen
import com.chicken.minerunner.presentation.screen.UpgradeScreen
import com.chicken.minerunner.presentation.viewmodel.GameViewModel

@Composable
fun MineRunnerNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    gameViewModel: GameViewModel
) {
    NavHost(
        navController = navController,
        startDestination = NavRoute.Main.route,
        modifier = modifier
    ) {
        composable(NavRoute.Main.route) {
            MainMenuScreen(
                onStart = { navController.navigate(NavRoute.Game.route) },
                onUpgrade = { navController.navigate(NavRoute.Upgrade.route) },
                gameViewModel = gameViewModel
            )
        }
        composable(NavRoute.Game.route) {
            GameScreen(
                onPause = {
                    gameViewModel.pause()
                    navController.navigate(NavRoute.Pause.route)
                },
                onFinish = {
                    gameViewModel.finish()
                    navController.navigate(NavRoute.GameOver.route)
                },
                onUpgrade = { navController.navigate(NavRoute.Upgrade.route) },
                gameViewModel = gameViewModel
            )
        }
        composable(NavRoute.Pause.route) {
            PauseScreen(
                onPlay = { navController.popBackStack() },
                onRetry = {
                    gameViewModel.resetRun()
                    navController.popBackStack(NavRoute.Game.route, inclusive = false)
                },
                onMainMenu = {
                    navController.popBackStack(NavRoute.Main.route, inclusive = false)
                    navController.navigate(NavRoute.Main.route) {
                        popUpTo(NavRoute.Main.route) { inclusive = true }
                    }
                }
            )
        }
        composable(NavRoute.GameOver.route) {
            GameOverScreen(
                onRetry = {
                    gameViewModel.resetRun()
                    navController.popBackStack(NavRoute.GameOver.route, inclusive = true)
                    navController.navigate(NavRoute.Game.route)
                },
                onLobby = {
                    navController.popBackStack(NavRoute.Main.route, inclusive = false)
                    navController.navigate(NavRoute.Main.route) {
                        popUpTo(NavRoute.Main.route) { inclusive = true }
                    }
                },
                gameViewModel = gameViewModel
            )
        }
        composable(NavRoute.Upgrade.route) {
            UpgradeScreen(onBack = { navController.popBackStack() })
        }
    }
}
