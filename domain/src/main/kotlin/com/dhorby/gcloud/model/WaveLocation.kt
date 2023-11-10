package com.dhorby.gcloud.model

data class WaveLocation(
    override val id: String,
    override val name: String,
    override val geoLocation: GeoLocation,
    override val pieceType: PieceType = PieceType.WAVE,
    override val waveDataReadings: List<WaveDataReading>,
): Location



