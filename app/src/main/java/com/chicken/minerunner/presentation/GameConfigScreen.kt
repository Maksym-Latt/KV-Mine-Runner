package com.chicken.minerunner.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun GameConfigScreen(
    viewModel: GameConfigViewModel,
    modifier: Modifier = Modifier
) {
    val config by viewModel.config.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Game tuning",
            style = MaterialTheme.typography.headlineSmall
        )

        ConfigCard(title = "Collider coefficients") {
            ColliderSlider(
                label = "Chicken",
                value = config.colliderCoefficients.chicken,
                onValueChange = viewModel::updateChickenCollider
            )
            ColliderSlider(
                label = "Trolley",
                value = config.colliderCoefficients.trolley,
                onValueChange = viewModel::updateTrolleyCollider
            )
            ColliderSlider(
                label = "Egg",
                value = config.colliderCoefficients.egg,
                onValueChange = viewModel::updateEggCollider
            )
            ColliderSlider(
                label = "Helmet",
                value = config.colliderCoefficients.helmet,
                onValueChange = viewModel::updateHelmetCollider
            )
            ColliderSlider(
                label = "Magnet",
                value = config.colliderCoefficients.magnet,
                onValueChange = viewModel::updateMagnetCollider
            )
            ColliderSlider(
                label = "Extra life",
                value = config.colliderCoefficients.extraLife,
                onValueChange = viewModel::updateExtraLifeCollider
            )
        }

        ConfigCard(title = "Trolley settings") {
            SliderRow(
                label = "Speed",
                value = config.trolleySpeed,
                valueRange = 0f..6f,
                steps = 5,
                onValueChange = viewModel::updateTrolleySpeed,
                format = { "${"%.1f".format(it)}x" }
            )
            SliderRow(
                label = "Max on screen",
                value = config.maxTrolleysOnScreen.toFloat(),
                valueRange = 1f..5f,
                steps = 3,
                onValueChange = { viewModel.updateMaxTrolleys(it.roundToInt()) },
                format = { it.roundToInt().toString() }
            )
        }
    }
}

@Composable
private fun ConfigCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            content()
        }
    }
}

@Composable
private fun ColliderSlider(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit
) {
    SliderRow(
        label = label,
        value = value,
        valueRange = 0f..1f,
        steps = 8,
        onValueChange = onValueChange,
        format = { "${"%.2f".format(it)}" }
    )
}

@Composable
private fun SliderRow(
    label: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    onValueChange: (Float) -> Unit,
    format: (Float) -> String
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = label, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = format(value),
                style = MaterialTheme.typography.labelLarge
            )
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            steps = steps
        )
    }
}
