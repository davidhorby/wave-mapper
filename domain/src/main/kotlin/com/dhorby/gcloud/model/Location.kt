package com.dhorby.gcloud.model

interface Location {
    val id: String
    val name: String
    val pieceType: PieceType
    val geoLocation: GeoLocation
    val waveDataReadings: List<WaveDataReading>?
}
