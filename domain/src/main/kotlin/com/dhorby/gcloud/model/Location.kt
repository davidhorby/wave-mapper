package com.dhorby.gcloud.model

import com.dhorby.gcloud.model.com.dhorby.gcloud.model.GeoLocation

interface Location {
    val id: String
    val name: String
    val pieceType: PieceType
    val geoLocation: GeoLocation
    val waveDataReadings: List<WaveDataReading>?
}
