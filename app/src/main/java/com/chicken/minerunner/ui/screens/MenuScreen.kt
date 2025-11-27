package com.chicken.minerunner.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.minerunner.R
import com.chicken.minerunner.ui.components.MineButton
import com.chicken.minerunner.ui.theme.CopperDark
import com.chicken.minerunner.ui.theme.Gold
import com.chicken.minerunner.ui.theme.SkyAccent
import com.chicken.minerunner.ui.theme.TealAccent

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
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.chicken_happy),
                    contentDescription = null,
                    modifier = Modifier.size(260.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "CHICKEN\nMINE RUNNER",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Gold,
                    lineHeight = 30.sp
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                MineButton(text = "START", onClick = onStart)
                Spacer(modifier = Modifier.height(12.dp))
                MineButton(
                    text = "SHOP",
                    colors = listOf(SkyAccent, TealAccent),
                    onClick = onShop
                )
            }
        }
    }
}
