package com.chicken.minerunner.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.dropper.ui.components.ChickenButtonStyle
import com.chicken.dropper.ui.components.PrimaryButton
import com.chicken.dropper.ui.components.SecondaryButton
import com.chicken.minerunner.R
import com.chicken.minerunner.presentation.progress.ShopItemState
import com.chicken.minerunner.ui.components.EggCounter
import com.chicken.minerunner.ui.components.GradientOutlinedText

@Composable
fun ShopScreen(
    eggs: Int,
    currentIndex: Int,
    items: List<ShopItemState>,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    onPurchase: () -> Unit,
    onBack: () -> Unit
) {
    val item = items.getOrNull(currentIndex) ?: return

    Box(
        modifier = Modifier
            .fillMaxSize(),
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
                    icon = rememberVectorPainter(Icons.Default.Home),
                    onClick = onBack,
                    buttonSize = 48.dp,
                    iconSize = 24.dp,
                    cornerRadius = 12.dp
                )

                EggCounter(
                    count = eggs,
                    eggIcon = R.drawable.item_egg
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            GradientOutlinedText(
                text = "MINE",
                fontSize = 38.sp,
                outlineWidth = 6f,
                outlineColor = Color(0xFF5D3B1D),
                gradient = Brush.horizontalGradient(
                    listOf(
                        Color(0xFFFFEB62),
                        Color(0xFFFFC726),
                        Color(0xFFFF9F00)
                    )
                )
            )

            GradientOutlinedText(
                text = item.title,
                fontSize = 40.sp,
                outlineWidth = 6f,
                outlineColor = Color(0xFF2B3C8A),
                gradient = Brush.horizontalGradient(
                    listOf(
                        Color(0xFF84B8FF),
                        Color(0xFF5AA2FF),
                        Color(0xFF2867AA)
                    )
                )
            )

            Text(
                text = item.subtitle,
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
                contentAlignment = Alignment.Center
            ) {

                Image(
                    painter = painterResource(id = item.image),
                    contentDescription = null,
                    modifier = Modifier.size(220.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ShopArrow(
                        direction = ArrowDirection.Left,
                        onClick = onPrev
                    )
                    ShopArrow(
                        direction = ArrowDirection.Right,
                        onClick = onNext
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Level ${'$'}{item.level}/${'$'}{item.maxLevel}",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (item.nextPrice == null) {
                PrimaryButton(
                    text = "MAXED",
                    onClick = {},
                    style = ChickenButtonStyle.Blue,
                    modifier = Modifier.fillMaxWidth(0.7f)
                )
            } else if (item.owned) {
                PrimaryButton(
                    text = "UPGRADE ${'$'}{item.nextPrice}",
                    onClick = onPurchase,
                    style = ChickenButtonStyle.Blue,
                    modifier = Modifier.fillMaxWidth(0.7f),
                    fontSize = 22.sp,
                    content = {
                        Text(
                            text = "UPGRADE",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Image(
                            painter = painterResource(id = R.drawable.item_egg),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = item.nextPrice.toString(),
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    PrimaryButton(
                        text = item.basePrice.toString(),
                        onClick = onPurchase,
                        style = ChickenButtonStyle.Blue,
                        modifier = Modifier.fillMaxWidth(0.7f),
                        fontSize = 26.sp,
                        content = {
                            Text(
                                text = item.basePrice.toString(),
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Image(
                                painter = painterResource(id = R.drawable.item_egg),
                                contentDescription = null,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ShopArrow(
    direction: ArrowDirection,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rotation = if (direction == ArrowDirection.Left) 180f else 0f

    Box(
        modifier = modifier
            .size(46.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.65f))
            .clickable { onClick() }
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    rotationY = rotation
                }
        ) {
            val w = size.width
            val h = size.height

            val path = Path().apply {
                moveTo(w * 0.25f, h * 0.5f)
                lineTo(w * 0.65f, h * 0.2f)
                lineTo(w * 0.65f, h * 0.8f)
                close()
            }

            drawPath(
                path = path,
                color = Color(0xFF4A4A4A)
            )
        }
    }
}

enum class ArrowDirection { Left, Right }

