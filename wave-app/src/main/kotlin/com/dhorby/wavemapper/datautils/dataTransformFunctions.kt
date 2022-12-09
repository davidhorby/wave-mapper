package com.dhorby.wavemapper.datautils

import com.dhorby.gcloud.model.Location
import com.dhorby.gcloud.model.PieceLocation
import com.dhorby.gcloud.model.PieceType
import com.dhorby.gcloud.model.WaveLocation
import com.dhorby.wavemapper.mapWaveHeightToIcon


fun List<Location>.toGoogleMapFormat(): String {
    val header = """
            ['Lat', 'Long', 'Name', 'Marker'],
        """.trimIndent()
    val map: List<String> = this.map { location ->
        when (location) {
            is WaveLocation -> {
                val waveHeight: Float = location.waveDataReadings.firstOrNull()?.waveHeight ?: 0F
                val windSpeed = location.waveDataReadings.firstOrNull()?.windSpeed ?: 0F
                val windDirection = location.waveDataReadings.firstOrNull()?.windDirection ?: 0F
                "[${location.geoLocation.lat},${location.geoLocation.lon},'${location.name}  ${waveHeight}m ${windSpeed}km ${windDirection}', '${waveHeight.mapWaveHeightToIcon()}']"
            }
            is PieceLocation -> {
                when (location.pieceType) {
                    PieceType.SHARK -> "[${location.geoLocation.lat},${location.geoLocation.lon},'[${location.name}]', 'shark']"
                    PieceType.BOAT -> "[${location.geoLocation.lat},${location.geoLocation.lon},'[${location.name}]', 'boat']"
                }
            }
            else -> throw Exception("Bad location ${location.toString()}")
        }

    }
    val joinToString = map.joinToString(",")
    return header + joinToString
}
