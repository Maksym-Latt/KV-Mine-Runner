package com.chicken.minerunner.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.dropper.ui.components.ChickenButtonStyleVariant
import com.chicken.dropper.ui.components.ActionButton
import com.chicken.dropper.ui.components.IconAccentButton
import com.chicken.minerunner.ui.components.GradientText

@Composable
fun SettingsOverlay(
    musicEnabled: Boolean,
    sfxEnabled: Boolean,
    onMusicToggle: (Boolean) -> Unit,
    onSfxToggle: (Boolean) -> Unit,
    onClose: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.55f))
    ) {

        Box(
            modifier = Modifier
                .padding(top = 24.dp, start = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            IconAccentButton(
                iconPainter = rememberVectorPainter(Icons.Default.ArrowBackIosNew),
                onPress = onClose,
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.8f)
                .wrapContentHeight()
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xff286298),
                            Color(0xff519ee4),
                            Color(0xff296398)
                        )
                    )
                )
                .border(
                    3.dp,
                    Color(0xFF0A3C80),
                    RoundedCornerShape(20.dp)
                )
                .padding(vertical = 30.dp, horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                GradientText(
                    text = "Settings",
                    size = 32.sp,
                    stroke = 5f,
                    strokeColor = Color(0xFF0A3C80),
                    brush = Brush.horizontalGradient(
                        listOf(
                            Color.White,
                            Color(0xFFEAF7FF),
                            Color(0xFFD4ECFF)
                        )
                    ),
                    expand = false
                )

                ActionButton(
                    label = if (musicEnabled) "Music: ON" else "Music: OFF",
                    onPress = { onMusicToggle(!musicEnabled) },
                    variant = ChickenButtonStyleVariant.Blue,
                    labelSize = 22.sp,
                    modifier = Modifier.fillMaxWidth(),

                )

                ActionButton(
                    label = if (sfxEnabled) "SFX: ON" else "SFX: OFF",
                    onPress = { onSfxToggle(!sfxEnabled) },
                    variant = ChickenButtonStyleVariant.Blue,
                    labelSize = 22.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
