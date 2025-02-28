package com.dhorby.gcloud.wavemapper

import com.dhorby.gcloud.model.*
import com.fasterxml.jackson.databind.JsonNode
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt


typealias SiteListFunction = () -> MutableList<Site>
typealias DataForSiteFunction = (site: String) -> WaveLocation?

fun sailMove(geoLocation: GeoLocation): GeoLocation {
    return GeoLocation(geoLocation.lat  + (0..5).random().toDouble(),
        geoLocation.lon  + (0..10).random().toDouble())
}


fun MutableList<Location>.withAddedShark(): MutableList<Location> {
    this.add(
        PieceLocation(
            id = "1234",
            name = "Susan",
            geoLocation = GeoLocation(lat = 53.506397, lon = 0.928163),
            pieceType = PieceType.SHARK
        )
    )
    this.add(
        PieceLocation(
            id = "12345",
            name = "Alan",
            geoLocation = GeoLocation(lat = 51.108184, lon = -5.016133),
            pieceType = PieceType.SHARK
        )
    )
    return this
}

fun JsonNode.getSiteLocations(): List<Site> {
    return this.getLocationPath().map {
        Site(
            id = it.path("id").textValue(),
            geoLocation = GeoLocation(
                lat = it.path("latitude").doubleValue(),
                lon = it.path("longitude").doubleValue()
            ),
            name = it.path("name").textValue(),
            obsLocationType = it.path("obsLocationType").textValue(),
            obsRegion = it.path("obsRegion").textValue(),
            obsSource = it.path("obsSource").textValue()
        )
    }
}

fun JsonNode.getLocation(): WaveLocation {
    val id: String = this.getDataValue().getLocationPath().path("i")?.toString()?.removeQuotes() ?: "Unknown"
    val name: String =
        this.getDataValue().getLocationPath().path("name")?.toString()?.removeQuotes() ?: "Unknown name"
    val lat: Double = this.getDataValue().getLocationPath().path("lat")?.toString()?.parseToDouble() ?: 0.0
    val lon: Double = this.getDataValue().getLocationPath().path("lon")?.toString()?.parseToDouble() ?: 0.0
    val waveDataReadings: List<WaveDataReading> = getDatePeriods(this)
    return WaveLocation(
        id = id,
        name = name,
        geoLocation = GeoLocation(lat = lat, lon = lon),
        waveDataReadings = waveDataReadings
    )
}

private fun getDatePeriods(jsonNode: JsonNode): List<WaveDataReading> {
    val periods: JsonNode = jsonNode.getDataValue().getLocationPath().path("Period")
    return when {
        periods.isArray -> {
            periods.mapNotNull { period ->
                val waveHeight: Float = getWaveReps(period).maxOrNull() ?: 0.0F
                val windSpeed: Int = getWindSpeedInKm(period).maxOrNull() ?: 0
                val windDirection: String = getWindDirection(period).maxOrNull() ?: ""
                val dateStr: String = period.get("value").toString()

                dateStr.removeQuotes().parseToDate()?.let {
                    WaveDataReading(it, waveHeight, windSpeed, windDirection)
                }
            }
        }
        else -> emptyList()
    }
}

private fun getWaveReps(jsonNode: JsonNode): List<Float> {
    val reps: JsonNode = jsonNode.path("Rep")
    return reps.map { rep ->
        val waveHeight = rep.get("Wh")?.toString()?.parseToFloat()
        waveHeight
    }.filterNotNull()
}

private fun getWindSpeedInKm(jsonNode: JsonNode): List<Int> {
    val reps: JsonNode = jsonNode.path("Rep")
    return reps.map { rep ->
        val waveHeight = rep.get("S")?.toString()?.parseToInt()
        val waveHeightKph: Double = waveHeight?.let {
            it * 1.85
        } ?: 0.0
        waveHeightKph.roundToInt()
    }
}

private fun getWindDirection(jsonNode: JsonNode): List<String> {
    val reps: JsonNode = jsonNode.path("Rep")
    return reps.map { rep ->
        rep.get("D")?.toString()?.removeQuotes() ?: ""
    }
}


private fun String.parseToFloat(): Float {
    if (this.isEmpty()) return 0.0F
    else
        return try {
            this.removeQuotes().toFloat()
        } catch (e: Exception) {
            println("Error parsing $this")
            return 0.0F
        }
}

private fun String.parseToDouble(): Double {
    if (this.isEmpty()) return 0.0
    else
        return try {
            this.removeQuotes().toDouble()
        } catch (e: Exception) {
            println("Error parsing $this")
            return 0.0
        }
}

private fun String.parseToInt(): Int {
    if (this.isEmpty()) return 0
    else
        return try {
            this.removeQuotes().toInt()
        } catch (e: Exception) {
            println("Error parsing $this")
            return 0
        }
}

fun String.removeQuotes(): String = this.toString().replace("\"", "")
fun String.parseToDate(): LocalDate? {
    // 2021-06-24Z
    val dFormat = DateTimeFormatter.ofPattern("yyyy-MM-ddX")
    return LocalDate.parse(this, dFormat)
}

fun Float.mapWaveHeightToIcon(): String {
    return when {
        this > 0.0 && this <= 0.4 -> "verysmall"
        this > 0.4 && this <= 0.8 -> "small"
        this > 0.8 && this <= 1.0 -> "medium"
        this > 1.0 && this <= 3.0 -> "big"
        this >= 3.0 -> "verybig"
        else -> "notavailable"
    }
}

private fun JsonNode.getDataValue() = this.path("DV")
private fun JsonNode.getLocationPath() = this.path("Location")






