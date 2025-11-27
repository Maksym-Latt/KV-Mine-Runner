package com.chicken.minerunner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.chicken.minerunner.presentation.navigation.MineRunnerNavHost
import com.chicken.minerunner.presentation.viewmodel.GameViewModel
import com.chicken.minerunner.ui.theme.MineRunnerTheme

class MainActivity : ComponentActivity() {
    private val gameViewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MineRunnerTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MineRunnerNavHost(gameViewModel = gameViewModel)
                }
            }
        }
    }
}
