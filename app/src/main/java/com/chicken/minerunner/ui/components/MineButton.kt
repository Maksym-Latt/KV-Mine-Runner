package com.chicken.minerunner.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.chicken.minerunner.ui.theme.Copper
import com.chicken.minerunner.ui.theme.CopperLight
import com.chicken.minerunner.ui.theme.SoftBrown

@Composable
fun MineButton(
    text: String,
    modifier: Modifier = Modifier,
    colors: List<Color> = listOf(CopperLight, Copper),
    contentPadding: PaddingValues = PaddingValues(vertical = 14.dp, horizontal = 24.dp),
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(colors),
                shape = RoundedCornerShape(18.dp)
            )
            .clickable(onClick = onClick)
            .padding(contentPadding),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = SoftBrown,
            textAlign = TextAlign.Center
        )
    }
}
