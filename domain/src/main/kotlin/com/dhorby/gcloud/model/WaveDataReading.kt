package com.dhorby.gcloud.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate
@Serializable
data class WaveDataReading(
    @Contextual
    val date: LocalDate,
    val waveHeight: Float,
    val windSpeed: Int,
    val windDirection: String
)
