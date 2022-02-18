package com.dhorby.wavemapper.datautils

import com.dhorby.wavemapper.mapWaveHeight
import com.dhorby.wavemapper.model.Location



fun List<Location>.toGoogleMapFormat():String {
    val header = """
            ['Lat', 'Long', 'Name', 'Marker'],
        """.trimIndent()
    val map: List<String> = this.map { location ->
        val waveHeight = location.datePeriods.firstOrNull()?.waveHeight
        val windSpeed = location.datePeriods.firstOrNull()?.windSpeed
        val windDirection = location.datePeriods.firstOrNull()?.windDirection
        "[${location.lat},${location.lon},'${location.name}  ${waveHeight}m ${windSpeed}km ${windDirection} ', '${waveHeight?.mapWaveHeight()}']"
    }
    val joinToString = map.joinToString ( "," )
    return header + joinToString
}
