package com.chicken.minerunner.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.chicken.dropper.ui.components.PrimaryButton
import com.chicken.minerunner.ui.theme.OverlayBlue


@Composable
 fun PauseOverlay(onResume: () -> Unit, onExit: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = OverlayBlue.copy(alpha = 0.6f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Paused",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(12.dp))
            PrimaryButton(text = "Resume", onClick = onResume)
            Spacer(modifier = Modifier.height(8.dp))
            PrimaryButton(text = "Main menu", style = com.chicken.dropper.ui.components.ChickenButtonStyle.Blue, onClick = onExit)
        }
    }
}
