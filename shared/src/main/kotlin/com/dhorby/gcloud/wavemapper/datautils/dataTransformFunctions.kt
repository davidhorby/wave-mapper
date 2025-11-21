package com.dhorby.gcloud.wavemapper.datautils

import model.Location
import model.PieceType
import com.dhorby.gcloud.wavemapper.mapWaveHeightToIcon
import java.util.*

fun <T, U> combine(first: Array<T>, second: Array<U>): Array<Any> {
    val list: MutableList<Any> = first.map { i -> i as Any }.toMutableList()
    list.addAll(second.map { i -> i as Any })
    return list.toTypedArray()
}

fun List<Location>.toGoogleMapFormatList(): Array<Array<Any>> {
    val header:Array<Any> = arrayOf("Lat", "Long", "Name", "Marker")
    val mutableList: MutableList<Array<Any>> = mutableListOf<Array<Any>>()
    mutableList.add(header)
    this.forEach() { location ->
        val value: Array<Any> = when (location.pieceType) {
            PieceType.WAVE -> {
                val waveHeight: Float = location.waveDataReadings?.firstOrNull()?.waveHeight ?: 0F
                val windSpeed = location.waveDataReadings?.firstOrNull()?.windSpeed ?: 0F
                val windDirection = location.waveDataReadings?.firstOrNull()?.windDirection ?: 0F
                arrayOf(
                    location.geoLocation.lat,
                    location.geoLocation.lon,
                    "${location.name}  ${waveHeight}m ${windSpeed}km ${windDirection}",
                    "${waveHeight.mapWaveHeightToIcon()}"
                )
            }

            else -> arrayOf(
                location.geoLocation.lat, location.geoLocation.lon, "${location.name}", "${
                    location.pieceType.name.lowercase(
                        Locale.getDefault()
                    )
                }"
            )
        }
        mutableList.add(value)
    }
    val toTypedArray: Array<Array<Any>> = mutableList.toTypedArray()
    return toTypedArray
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
