package com.dhorby.gcloud.model

import com.dhorby.gcloud.model.com.dhorby.gcloud.model.GeoLocation

data class WaveLocation(
    override val id: String,
    override val name: String,
    override val geoLocation: GeoLocation,
    override val pieceType: PieceType = PieceType.WAVE,
    override val waveDataReadings: List<WaveDataReading>,
): Location



