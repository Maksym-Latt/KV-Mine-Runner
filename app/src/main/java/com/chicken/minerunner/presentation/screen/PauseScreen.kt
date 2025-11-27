package com.chicken.minerunner.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.minerunner.presentation.ui.components.PrimaryButton
import com.chicken.minerunner.ui.theme.OverlayDark
import com.chicken.minerunner.ui.theme.TextLight

@Composable
fun PauseScreen(
    onPlay: () -> Unit,
    onRetry: () -> Unit,
    onMainMenu: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OverlayDark.copy(alpha = 0.7f)),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .shadow(24.dp, RoundedCornerShape(24.dp)),
            color = Color(0xFF31251C),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 28.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Paused",
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 28.sp,
                    color = TextLight
                )
                PrimaryButton(text = "Play", onClick = onPlay, modifier = Modifier.fillMaxWidth())
                PrimaryButton(text = "Retry", onClick = onRetry, modifier = Modifier.fillMaxWidth())
                PrimaryButton(
                    text = "Main Menu",
                    onClick = onMainMenu,
                    modifier = Modifier.fillMaxWidth(),
                    gradient = Brush.verticalGradient(listOf(Color(0xFF6C5B4A), Color(0xFF4C3B2C)))
                )
            }
        }
    }
}
