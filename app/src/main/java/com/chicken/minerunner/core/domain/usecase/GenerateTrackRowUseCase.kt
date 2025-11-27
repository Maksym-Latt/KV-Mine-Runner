package com.chicken.minerunner.core.domain.usecase

import com.chicken.minerunner.core.domain.model.ItemType
import com.chicken.minerunner.core.domain.model.LaneContent
import com.chicken.minerunner.core.domain.model.LanePosition
import com.chicken.minerunner.core.domain.model.TrackRow
import com.chicken.minerunner.core.domain.model.TrackType
import kotlin.random.Random

class GenerateTrackRowUseCase {
    fun invoke(id: Int, isSafeZone: Boolean): TrackRow {
        val type = if (isSafeZone) TrackType.SAFE_ZONE else TrackType.RAILWAY
        val trolleyPattern = when (id % 4) {
            0 -> setOf(LanePosition.LEFT)
            1 -> setOf(LanePosition.CENTER)
            2 -> setOf(LanePosition.RIGHT)
            else -> setOf(LanePosition.LEFT, LanePosition.RIGHT)
        }

        val lanes = LanePosition.values().map { lane ->
            val hasTrolley = type == TrackType.RAILWAY && lane in trolleyPattern
            val item = when {
                hasTrolley -> null
                isSafeZone -> pickItem(rareBonuses = true)
                else -> pickItem(rareBonuses = false)
            }
            LaneContent(lane = lane, hasTrolley = hasTrolley, item = item)
        }
        return TrackRow(id = id, type = type, lanes = lanes)
    }

    private fun pickItem(rareBonuses: Boolean): ItemType? {
        val roll = Random.nextInt(100)
        return if (rareBonuses) {
            when {
                roll < 10 -> ItemType.EXTRA_LIFE
                roll < 25 -> ItemType.HELMET
                roll < 40 -> ItemType.MAGNET
                roll < 80 -> ItemType.EGG
                else -> null
            }
        } else {
            when {
                roll < 3 -> ItemType.EXTRA_LIFE
                roll < 8 -> ItemType.HELMET
                roll < 14 -> ItemType.MAGNET
                roll < 90 -> ItemType.EGG
                else -> null
            }
        }
    }
}
