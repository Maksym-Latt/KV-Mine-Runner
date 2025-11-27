package com.chicken.minerunner.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.chicken.minerunner.R
import com.chicken.minerunner.ui.components.GradientButton
import com.chicken.minerunner.ui.components.GradientText
import com.chicken.minerunner.ui.theme.MineShadow

@Composable
fun GameOverScreen(distance: Int, eggs: Int, onRetry: () -> Unit, onLobby: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MineShadow)
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_menu),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.7f
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GradientText(text = "BLOCKED TUNNEL!", style = MaterialTheme.typography.displayLarge)
            Image(painter = painterResource(id = R.drawable.chicken_happy), contentDescription = null)
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Distance: ${distance}m", style = MaterialTheme.typography.bodyLarge, color = Color.White)
            Text(text = "Eggs: $eggs", style = MaterialTheme.typography.bodyLarge, color = Color.White)
            Spacer(modifier = Modifier.height(16.dp))
            GradientButton(text = "TRY AGAIN", modifier = Modifier.fillMaxWidth(0.8f)) { onRetry() }
            Spacer(modifier = Modifier.height(8.dp))
            GradientButton(text = "LOBBY", modifier = Modifier.fillMaxWidth(0.8f)) { onLobby() }
        }
    }
}
