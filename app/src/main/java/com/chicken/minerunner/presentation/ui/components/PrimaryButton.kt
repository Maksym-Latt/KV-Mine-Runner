package com.chicken.minerunner.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chicken.minerunner.ui.theme.Amber
import com.chicken.minerunner.ui.theme.AmberDark
import com.chicken.minerunner.ui.theme.DeepBlue
import com.chicken.minerunner.ui.theme.TextLight

@Composable
fun PrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    gradient: Brush = Brush.verticalGradient(listOf(Amber, AmberDark)),
    borderColor: Color = DeepBlue
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues()
    ) {
        Box(
            modifier = Modifier
                .shadow(12.dp, RoundedCornerShape(16.dp))
                .background(gradient, RoundedCornerShape(16.dp))
                .border(3.dp, borderColor, RoundedCornerShape(16.dp))
                .padding(horizontal = 24.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text.uppercase(),
                color = TextLight,
                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 20.sp)
            )
        }
    }
}
