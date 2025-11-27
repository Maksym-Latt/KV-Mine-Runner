package com.chicken.minerunner.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.chicken.dropper.ui.components.PrimaryButton
import com.chicken.minerunner.ui.components.GameTitle
import com.chicken.minerunner.ui.theme.CopperDark

@Composable
fun MenuScreen(
    onStart: () -> Unit,
    onShop: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(CopperDark, CopperDark)))
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            GameTitle(modifier = Modifier.padding(top = 32.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                PrimaryButton(text = "START", onClick = onStart)
                Spacer(modifier = Modifier.height(12.dp))
                PrimaryButton(
                    text = "SHOP",
                    onClick = onShop,
                    style = com.chicken.dropper.ui.components.ChickenButtonStyle.Blue
                )
            }
        }
    }
}
