package com.chicken.minerunner.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.minerunner.R
import com.chicken.minerunner.ui.components.MineButton
import com.chicken.minerunner.ui.theme.CopperDark
import com.chicken.minerunner.ui.theme.Gold

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
            Image(
                painter = painterResource(id = R.drawable.chicken_happy),
                contentDescription = null,
                modifier = Modifier.size(220.dp)
            )
            Text(
                text = "Blocked tunnel!",
                color = Gold,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            MineButton(text = "Try again", onClick = onRetry)
            Spacer(modifier = Modifier.height(8.dp))
            MineButton(text = "Lobby", colors = listOf(Color.White, Gold), onClick = onLobby)
        }
    }
}
