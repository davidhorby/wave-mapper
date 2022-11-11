package com.dhorby.wavemapper.datautils

import com.dhorby.wavemapper.mapWaveHeight
import com.dhorby.wavemapper.model.Location



fun List<Location>.toGoogleMapFormat():String {
    val header = """
            ['Lat', 'Long', 'Name', 'Marker'],
        """.trimIndent()
    val map: List<String> = this.map { location ->
        val waveHeight: Float = location.datePeriods.firstOrNull()?.waveHeight?:0F
        val windSpeed = location.datePeriods.firstOrNull()?.windSpeed?:0F
        val windDirection = location.datePeriods.firstOrNull()?.windDirection?:0F
        "[${location.lat},${location.lon},'${location.name}  ${waveHeight}m ${windSpeed}km ${windDirection} ', '${waveHeight?.mapWaveHeight()}']"
    }
    val joinToString = map.joinToString ( "," )
    return header + joinToString
}
