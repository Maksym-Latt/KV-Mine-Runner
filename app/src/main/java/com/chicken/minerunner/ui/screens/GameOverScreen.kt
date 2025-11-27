package com.chicken.minerunner.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.dropper.ui.components.PrimaryButton
import com.chicken.minerunner.ui.components.GameTitle
import com.chicken.minerunner.ui.components.GradientOutlinedText
import com.chicken.minerunner.ui.theme.CopperDark

@Composable
fun GameOverScreen(
    onRetry: () -> Unit,
    onLobby: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CopperDark),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            GameTitle()
            GradientOutlinedText(
                text = "Blocked tunnel!",
                fontSize = 32.sp,
                outlineColor = Color.Black,
                gradient = Brush.verticalGradient(listOf(Color.White, Color(0xFFFFE082)))
            )
            Spacer(modifier = Modifier.height(16.dp))
            PrimaryButton(text = "Try again", onClick = onRetry)
            Spacer(modifier = Modifier.height(8.dp))
            PrimaryButton(
                text = "Lobby",
                style = com.chicken.dropper.ui.components.ChickenButtonStyle.Blue,
                onClick = onLobby
            )
        }
    }
}
