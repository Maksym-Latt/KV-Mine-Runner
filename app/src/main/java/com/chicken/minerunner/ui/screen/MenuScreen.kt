package com.chicken.minerunner.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.chicken.minerunner.R
import com.chicken.minerunner.ui.components.GradientButton
import com.chicken.minerunner.ui.components.GradientText
import com.chicken.minerunner.ui.theme.MineShadow

@Composable
fun MenuScreen(onStart: () -> Unit, onOpenShop: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MineShadow)
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_menu),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painter = painterResource(id = R.drawable.chicken_title), contentDescription = null)
            GradientText(
                text = "CHICKEN MINE RUNNER",
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(top = 16.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            GradientButton(text = "START", modifier = Modifier.fillMaxWidth(0.8f)) {
                onStart()
            }
            Spacer(modifier = Modifier.height(12.dp))
            GradientButton(text = "SHOP", modifier = Modifier.fillMaxWidth(0.8f)) {
                onOpenShop()
            }
        }
    }
}
