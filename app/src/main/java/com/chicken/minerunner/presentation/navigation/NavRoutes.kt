package com.chicken.minerunner.presentation.navigation

sealed class NavRoute(val route: String) {
    data object Main : NavRoute("main")
    data object Game : NavRoute("game")
    data object Pause : NavRoute("pause")
    data object GameOver : NavRoute("game_over")
    data object Upgrade : NavRoute("upgrade")
}
