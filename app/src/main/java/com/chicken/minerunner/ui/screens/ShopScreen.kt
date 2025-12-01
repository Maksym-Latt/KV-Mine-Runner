package com.chicken.minerunner.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.dropper.ui.components.ChickenButtonStyleVariant
import com.chicken.dropper.ui.components.ActionButton
import com.chicken.dropper.ui.components.IconAccentButton
import com.chicken.minerunner.R
import com.chicken.minerunner.presentation.progress.ShopItemState
import com.chicken.minerunner.ui.components.EggCounterBox
import com.chicken.minerunner.ui.components.GradientText

@Composable
fun ShopScreen(
    eggs: Int,
    currentIndex: Int,
    items: List<ShopItemState>,
    message: String?,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    onPurchase: () -> Unit,
    onDismissMessage: () -> Unit,
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
                .padding(horizontal = 24.dp)
                .windowInsetsPadding(WindowInsets.displayCutout),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconAccentButton(
                    iconPainter = rememberVectorPainter(Icons.Default.Home),
                    onPress = onBack,
                )

                EggCounterBox(
                    amount = eggs,
                    iconRes = R.drawable.item_egg
                )
            }

            if (message != null) {
                AlertDialog(
                    onDismissRequest = onDismissMessage,
                    title = { Text(text = "Not enough eggs") },
                    text = { Text(text = message) },
                    confirmButton = {
                        TextButton(onClick = onDismissMessage) {
                            Text(text = "OK")
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.weight(2f))

            GradientText(
                text = item.title,
                size = 40.sp,
                stroke = 6f,
                strokeColor = Color(0xFF2B3C8A),
                brush = Brush.horizontalGradient(
                    listOf(
                        Color(0xFF84B8FF),
                        Color(0xFF5AA2FF),
                        Color(0xFF2867AA)
                    )
                )
            )

            Text(
                text = item.dynamicSubtitle,
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    ShopArrow(
                        direction = ArrowDirection.Left,
                        onClick = onPrev
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.62f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = item.image),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth(1f).aspectRatio(0.65f),
                            contentScale = ContentScale.Fit
                        )
                    }

                    ShopArrow(
                        direction = ArrowDirection.Right,
                        onClick = onNext
                    )

                }
            }

            Spacer(modifier = Modifier.weight(1f))

            GradientText(
                text = "Level ${item.level}/${item.maxLevel}",
                size = 28.sp,
                stroke = 6f,
                strokeColor = Color(0xFF1B2F6B),
                brush = Brush.horizontalGradient(
                    listOf(
                        Color(0xFF6EB4FF),
                        Color(0xFFA8D3FF),
                        Color(0xFF6EB4FF)
                    )
                ),
                expand = false,
                modifier = Modifier.padding(top = 4.dp)
            )



            Spacer(modifier = Modifier.weight(1f))

            val price = item.nextPrice

            if (price == null) {
                ActionButton(
                    label = "MAXED",
                    onPress = {},
                    variant = ChickenButtonStyleVariant.Blue,
                    modifier = Modifier.fillMaxWidth(0.7f)
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    ActionButton(
                        label = price.toString(),
                        onPress = onPurchase,
                        variant = ChickenButtonStyleVariant.Blue,
                        modifier = Modifier.fillMaxWidth(0.7f),
                        labelSize = 26.sp,
                        extraContent = {
                            Text(
                                text = price.toString(),
                                color = Color.White,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Image(
                                painter = painterResource(id = R.drawable.item_egg),
                                contentDescription = null,
                                modifier = Modifier.size(38.dp)
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
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
            .clickable { onClick() }
            .padding(6.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { rotationY = rotation }
        ) {
            val stroke = size.width * 0.20f
            val w = size.width
            val h = size.height

            val path = Path().apply {
                val midY = h * 0.5f
                val topY = h * 0.25f
                val botY = h * 0.75f
                val startX = w * 0.30f
                val endX = w * 0.70f

                moveTo(startX, topY)
                lineTo(endX, midY)
                lineTo(startX, botY)
            }

            drawPath(
                path = path,
                color = Color(0xFFE6E6E6),
                style = Stroke(width = stroke, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
        }
    }
}


enum class ArrowDirection { Left, Right }

