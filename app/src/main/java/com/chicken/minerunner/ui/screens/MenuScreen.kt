package com.chicken.minerunner.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.chicken.dropper.ui.components.ChickenButtonStyle
import com.chicken.dropper.ui.components.PrimaryButton
import com.chicken.dropper.ui.components.SecondaryButton
import com.chicken.minerunner.R
import com.chicken.minerunner.ui.components.EggCounter
import com.chicken.minerunner.ui.components.GameTitle
import com.chicken.minerunner.ui.theme.CopperDark
@Composable
fun MenuScreen(
    eggs: Int,
    onStart: () -> Unit,
    onShop: () -> Unit,
    onMenuClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
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
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SecondaryButton(
                    icon = rememberVectorPainter(Icons.Default.Settings),
                    onClick = onMenuClick,
                    buttonSize = 60.dp,
                    iconSize = 30.dp,
                    cornerRadius = 12.dp
                )

                EggCounter(
                    count = eggs,
                    eggIcon = R.drawable.item_egg,
                    modifier = Modifier
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            GameTitle(
                modifier = Modifier
                    .padding(top = 32.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            PrimaryButton(
                text = "START",
                onClick = onStart,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            PrimaryButton(
                text = "SHOP",
                onClick = onShop,
                style = ChickenButtonStyle.Blue,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
