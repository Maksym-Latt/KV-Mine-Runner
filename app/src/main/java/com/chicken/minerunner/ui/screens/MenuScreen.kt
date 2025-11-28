package com.chicken.minerunner.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.chicken.dropper.ui.components.ChickenButtonStyleVariant
import com.chicken.dropper.ui.components.ActionButton
import com.chicken.dropper.ui.components.IconAccentButton
import com.chicken.minerunner.R
import com.chicken.minerunner.ui.components.EggCounterBox
import com.chicken.minerunner.ui.components.GameTitleColumn

@Composable
fun MenuScreen(
    eggs: Int,
    onStart: () -> Unit,
    onShop: () -> Unit,
    onMenuClick: () -> Unit,
    settingsVisible: Boolean,
    musicEnabled: Boolean,
    sfxEnabled: Boolean,
    onMusicToggle: (Boolean) -> Unit,
    onSfxToggle: (Boolean) -> Unit,
    onCloseSettings: () -> Unit
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
                IconAccentButton(
                    iconPainter = rememberVectorPainter(Icons.Default.Settings),
                    onPress = onMenuClick,
                )

                EggCounterBox(
                    amount = eggs,
                    iconRes = R.drawable.item_egg,
                    modifier = Modifier
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            GameTitleColumn(
                modifier = Modifier
                    .padding(top = 32.dp)
            )

            Spacer(modifier = Modifier.weight(5f))

            Image(
                painter = painterResource(R.drawable.chicken_title),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .aspectRatio(0.85f)
                    .weight(1f, fill = false),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.weight(5f))

            ActionButton(
                label = "START",
                onPress = onStart,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            ActionButton(
                label = "SHOP",
                onPress = onShop,
                variant = ChickenButtonStyleVariant.Blue,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1.5f))
        }

        if (settingsVisible) {
            SettingsOverlay(
                musicEnabled = musicEnabled,
                sfxEnabled = sfxEnabled,
                onMusicToggle = onMusicToggle,
                onSfxToggle = onSfxToggle,
                onClose = onCloseSettings
            )
        }
    }
}