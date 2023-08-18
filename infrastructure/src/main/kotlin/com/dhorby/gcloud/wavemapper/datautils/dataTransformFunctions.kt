package com.dhorby.gcloud.wavemapper.datautils

import com.dhorby.gcloud.model.Location
import com.dhorby.gcloud.model.PieceType
import com.dhorby.gcloud.wavemapper.mapWaveHeightToIcon
import java.util.*


fun List<Location>.toGoogleMapFormatList(): List<List<String>> {
    val header = listOf<String>("Lat", "Long", "Name", "Marker")
    val mutableList: MutableList<List<String>> = mutableListOf(header)
    val stringList = this.map { location ->
        when (location.pieceType) {
            PieceType.WAVE -> {
                val waveHeight: Float = location.waveDataReadings?.firstOrNull()?.waveHeight ?: 0F
                val windSpeed = location.waveDataReadings?.firstOrNull()?.windSpeed ?: 0F
                val windDirection = location.waveDataReadings?.firstOrNull()?.windDirection ?: 0F
                listOf("${location.geoLocation.lat}","${location.geoLocation.lon}","${location.name}  ${waveHeight}m ${windSpeed}km ${windDirection}", "${waveHeight.mapWaveHeightToIcon()}")
            }

            else -> listOf("${location.geoLocation.lat}","${location.geoLocation.lon}","${location.name}", "${
                location.pieceType.name.lowercase(
                    Locale.getDefault()
                )
            }")
        }
    }
    mutableList.addAll(stringList)
    return mutableList
}

fun List<Location>.toGoogleMapFormat(): String {
    val header = """
            ['Lat', 'Long', 'Name', 'Marker'],
        """.trimIndent()
    val map: List<String> = this.map { location ->
        when (location.pieceType) {
            PieceType.WAVE -> {
                val waveHeight: Float = location.waveDataReadings?.firstOrNull()?.waveHeight ?: 0F
                val windSpeed = location.waveDataReadings?.firstOrNull()?.windSpeed ?: 0F
                val windDirection = location.waveDataReadings?.firstOrNull()?.windDirection ?: 0F
                "[${location.geoLocation.lat},${location.geoLocation.lon},'${location.name}  ${waveHeight}m ${windSpeed}km ${windDirection}', '${waveHeight.mapWaveHeightToIcon()}']"
            }
            else -> "[${location.geoLocation.lat},${location.geoLocation.lon},'[${location.name}]', '${location.pieceType.name.lowercase(
                Locale.getDefault()
            )}']"
        }
    }
    val joinToString = map.joinToString(",")
    return header + joinToString
}
