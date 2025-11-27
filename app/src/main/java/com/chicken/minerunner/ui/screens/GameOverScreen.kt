package com.chicken.minerunner.ui.screens

import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.dropper.ui.components.ChickenButtonStyle
import com.chicken.dropper.ui.components.PrimaryButton
import com.chicken.minerunner.R
import com.chicken.minerunner.ui.components.GameTitle
import com.chicken.minerunner.ui.components.GradientOutlinedText
import com.chicken.minerunner.ui.theme.CopperDark
@Composable
fun GameOverScreen(
    reward: Int,
    onRetry: () -> Unit,
    onLobby: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background( Color(0x66000000)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {

            // ---------- Title ----------
            GradientOutlinedText(
                text = "BLOCKED\nTUNNEL!",
                fontSize = 40.sp,
                outlineColor = Color.Black,
                outlineWidth = 7f,
                textAlign = TextAlign.Center,
                gradient = Brush.horizontalGradient(
                    listOf(
                        Color(0xff286298),
                        Color(0xff519ee4),
                        Color(0xff296398)
                    )
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ---------- Reward Row ----------
            RewardRow(reward = reward)

            Spacer(modifier = Modifier.height(32.dp))

            Image(
                painter = painterResource(id = R.drawable.chicken_happy),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.7f).aspectRatio(0.65f),
                contentScale = ContentScale.Fit
            )

            // ---------- Buttons ----------
            PrimaryButton(
                text = "Try again",
                onClick = onRetry,
                fontSize = 22.sp,
                style = ChickenButtonStyle.Blue,
                modifier = Modifier
                    .fillMaxWidth(0.80f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            PrimaryButton(
                text = "Lobby",
                onClick = onLobby,
                fontSize = 28.sp,
                style = ChickenButtonStyle.Green,
                modifier = Modifier
                    .fillMaxWidth(0.85f)
            )
        }
    }
}

@Composable
fun RewardRow(
    reward: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .wrapContentSize(),
        contentAlignment = Alignment.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.item_egg),
            contentDescription = null,
            modifier = Modifier.size(72.dp)
        )


        GradientOutlinedText(
            text = "+$reward",
            fontSize = 36.sp,
            outlineWidth = 15f,
            fillWidth = false,
            outlineColor = Color.Black,
            modifier = Modifier.offset(y = 40.dp),
            gradient = Brush.horizontalGradient(
                listOf(
                    Color(0xff286298),
                    Color(0xff519ee4),
                    Color(0xff296398)
                )
            )
        )
    }
}
