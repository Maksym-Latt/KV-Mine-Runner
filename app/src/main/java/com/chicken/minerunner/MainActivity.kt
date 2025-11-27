package com.chicken.minerunner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chicken.minerunner.ui.game.GameViewModel
import com.chicken.minerunner.ui.navigation.Routes
import com.chicken.minerunner.ui.screen.GameOverScreen
import com.chicken.minerunner.ui.screen.GameScreen
import com.chicken.minerunner.ui.screen.MenuScreen
import com.chicken.minerunner.ui.screen.ShopScreen
import com.chicken.minerunner.ui.screen.SplashScreen
import com.chicken.minerunner.ui.shop.ShopViewModel
import com.chicken.minerunner.ui.theme.MineRunnerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MineRunnerTheme {
                val navController = rememberNavController()
                val gameViewModel: GameViewModel = hiltViewModel()
                NavHost(navController = navController, startDestination = Routes.Splash) {
                    composable(Routes.Splash) {
                        SplashScreen { navController.navigate(Routes.Menu) { popUpTo(Routes.Splash) { inclusive = true } } }
                    }
                    composable(Routes.Menu) {
                        MenuScreen(onStart = { navController.navigate(Routes.Game) }, onOpenShop = { navController.navigate(Routes.Shop) })
                    }
                    composable(Routes.Game) {
                        GameScreen(
                            viewModel = gameViewModel,
                            onGameOver = {
                                navController.navigate(Routes.GameOver) {
                                    popUpTo(Routes.Game) { inclusive = true }
                                }
                            },
                            onBackToMenu = {
                                gameViewModel.pause()
                                navController.popBackStack(Routes.Menu, inclusive = false)
                            }
                        )
                    }
                    composable(Routes.GameOver) {
                        val state by gameViewModel.uiState.collectAsState()
                        GameOverScreen(
                            distance = state.distance,
                            eggs = state.eggs,
                            onRetry = {
                                gameViewModel.reset()
                                navController.navigate(Routes.Game) {
                                    popUpTo(Routes.GameOver) { inclusive = true }
                                }
                            },
                            onLobby = {
                                gameViewModel.reset()
                                navController.navigate(Routes.Menu) {
                                    popUpTo(0)
                                }
                            }
                        )
                    }
                    composable(Routes.Shop) {
                        val shopViewModel: ShopViewModel = hiltViewModel()
                        ShopScreen(viewModel = shopViewModel) { navController.popBackStack() }
                    }
                }
            }
        }
    }
}
