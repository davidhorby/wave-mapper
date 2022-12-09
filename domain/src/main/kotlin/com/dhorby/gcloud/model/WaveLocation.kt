package com.dhorby.gcloud.model

import com.dhorby.gcloud.model.com.dhorby.gcloud.model.GeoLocation

data class WaveLocation(
    val id: String,
    val name: String,
    override val geoLocation: GeoLocation,
    val waveDataReadings: List<WaveDataReading>
): Location



