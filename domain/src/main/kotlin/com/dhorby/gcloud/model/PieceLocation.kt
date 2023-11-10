package com.dhorby.gcloud.model

import kotlinx.serialization.Serializable

@Serializable
data class PieceLocation(
    override val id: String,
    override val name: String,
    override val pieceType: PieceType,
    override val geoLocation: GeoLocation,
    override val waveDataReadings: List<WaveDataReading>? = null
): Location


