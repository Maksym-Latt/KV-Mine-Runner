package com.chicken.minerunner.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.dropper.ui.components.ChickenButtonStyle
import com.chicken.dropper.ui.components.PrimaryButton
import com.chicken.dropper.ui.components.SecondaryButton
import com.chicken.minerunner.ui.components.GameTitle
import com.chicken.minerunner.ui.components.GradientOutlinedText
import com.chicken.minerunner.ui.theme.OverlayBlue

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
            SecondaryButton(
                icon = rememberVectorPainter(Icons.Default.ArrowBackIosNew),
                onClick = onClose,
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

                GradientOutlinedText(
                    text = "Settings",
                    fontSize = 32.sp,
                    outlineWidth = 5f,
                    outlineColor = Color(0xFF0A3C80),
                    gradient = Brush.horizontalGradient(
                        listOf(
                            Color.White,
                            Color(0xFFEAF7FF),
                            Color(0xFFD4ECFF)
                        )
                    ),
                    fillWidth = false
                )

                PrimaryButton(
                    text = if (musicEnabled) "Music: ON" else "Music: OFF",
                    onClick = { onMusicToggle(!musicEnabled) },
                    style = ChickenButtonStyle.Blue,
                    fontSize = 22.sp,
                    modifier = Modifier.fillMaxWidth(),

                )

                PrimaryButton(
                    text = if (sfxEnabled) "SFX: ON" else "SFX: OFF",
                    onClick = { onSfxToggle(!sfxEnabled) },
                    style = ChickenButtonStyle.Blue,
                    fontSize = 22.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
