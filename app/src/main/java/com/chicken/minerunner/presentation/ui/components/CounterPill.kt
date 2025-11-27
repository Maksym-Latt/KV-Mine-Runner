package com.chicken.minerunner.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.minerunner.ui.theme.Amber
import com.chicken.minerunner.ui.theme.AmberDark
import com.chicken.minerunner.ui.theme.TextLight

@Composable
fun CounterPill(
    iconRes: Int,
    value: Int,
    modifier: Modifier = Modifier,
    background: Brush = Brush.horizontalGradient(listOf(Amber, AmberDark)),
    contentColor: Color = TextLight
) {
    Box(
        modifier = modifier
            .shadow(8.dp, RoundedCornerShape(16.dp))
            .background(background, RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Image(painter = painterResource(id = iconRes), contentDescription = null)
            Text(text = value.toString(), color = contentColor, style = MaterialTheme.typography.labelLarge.copy(fontSize = 16.sp))
        }
    }
}
