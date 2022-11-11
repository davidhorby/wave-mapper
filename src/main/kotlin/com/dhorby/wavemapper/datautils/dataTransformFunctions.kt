package com.dhorby.wavemapper.datautils

import com.dhorby.wavemapper.mapWaveHeightToIcon
import com.dhorby.wavemapper.model.Location
import com.dhorby.wavemapper.model.SharkLocation
import com.dhorby.wavemapper.model.WaveLocation



fun List<Location>.toGoogleMapFormat():String {
    val header = """
            ['Lat', 'Long', 'Name', 'Marker'],
        """.trimIndent()
    val map: List<String> = this.map { location ->
        when (location) {
            is WaveLocation -> {
                val waveHeight: Float = location.waveDataReadings.firstOrNull()?.waveHeight?:0F
                val windSpeed = location.waveDataReadings.firstOrNull()?.windSpeed?:0F
                val windDirection = location.waveDataReadings.firstOrNull()?.windDirection?:0F
                "[${location.lat},${location.lon},'${location.name}  ${waveHeight}m ${windSpeed}km ${windDirection}', '${waveHeight.mapWaveHeightToIcon()}']"
            }
            is SharkLocation -> {
                "[${location.lat},${location.lon},'${location.species} ', 'shark']"
            }
        }

    }
    val joinToString = map.joinToString ( "," )
    return header + joinToString
}
