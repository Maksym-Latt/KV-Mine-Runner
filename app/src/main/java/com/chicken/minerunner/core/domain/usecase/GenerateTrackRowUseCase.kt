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
        val lanes = LanePosition.values().map { lane ->
            if (type == TrackType.SAFE_ZONE) {
                LaneContent(lane = lane, hasTrolley = false, item = null)
            } else {
                val roll = Random.nextInt(100)
                val item = when {
                    roll < 2 -> ItemType.EXTRA_LIFE
                    roll < 6 -> ItemType.MAGNET
                    roll < 10 -> ItemType.HELMET
                    roll < 100 -> ItemType.EGG
                    else -> null
                }
                val trolley = Random.nextBoolean()
                LaneContent(lane = lane, hasTrolley = trolley, item = item)
            }
        }
        return TrackRow(id = id, type = type, lanes = lanes)
    }
}
